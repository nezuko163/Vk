package com.nezuko.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nezuko.domain.repository.Navigation
import com.nezuko.main.databinding.BottomSheetWithTextFieldBinding
import com.nezuko.main.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val vm: MainViewModel by activityViewModels()

    companion object {
        private val TAG = "MainFragment"
    }

    @Inject
    lateinit var navigation: Navigation

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: FilesAdapter

    private lateinit var openFileLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var createFileLauncher: ActivityResultLauncher<String>

    private val bottomSheetWithText = BottomSheetWithTextField({})

    private val bottomSheet = MyBottomSheetFragment(
        onOpenExitedFile = {
            openFileLauncher.launch(arrayOf("text/*"))
        },
        onSearchFileOnInternet = {
            bottomSheetWithText.show(parentFragmentManager, bottomSheetWithText.tag)
        },
        onCreateNewFile = {
            createFileLauncher.launch("file.md")
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openFileLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    requireContext().contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Log.i(TAG, "onViewCreated: Открыт файл - uri: $it")
                    vm.processUri(it) { id -> navigation.navigateFromMainToMdReader(id) }
                }
            }

        createFileLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument("text/markdown")) { uri ->
                uri?.let {
                    Log.i(TAG, "onViewCreated: Создан файл - uri: $it")
                    vm.processUri(it) { id -> navigation.navigateFromMainToMdReader(id) }
                }
            }

        binding.open.setOnClickListener {
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.rcView.layoutManager = LinearLayoutManager(context)
        adapter = FilesAdapter(onItemClick = { file ->
            vm.updateLastOpen(file.fileId)
            navigation.navigateFromMainToMdReader(file.fileId)
        })
        binding.rcView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, {
                vm.fileFlow.collectLatest {
                    adapter.submitData(it)
                }
            })
        }
    }
}