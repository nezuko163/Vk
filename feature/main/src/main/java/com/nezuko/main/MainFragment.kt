package com.nezuko.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nezuko.domain.repository.Navigation
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.add.setOnClickListener {
            navigation.navigateToMdReader(-1)
        }

        binding.rcView.layoutManager = LinearLayoutManager(context)


        adapter = FilesAdapter(onItemClick = { file ->
            vm.updateLastOpen(file.fileId)
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