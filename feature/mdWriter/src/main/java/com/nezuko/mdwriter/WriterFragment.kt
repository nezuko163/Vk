package com.nezuko.mdwriter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nezuko.domain.model.Result
import com.nezuko.domain.repository.Navigation
import com.nezuko.mdwriter.databinding.MdWriterLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WriterFragment : Fragment() {
    private lateinit var binding: MdWriterLayoutBinding
    private val vm: WriterViewModel by activityViewModels()

    private val fileId: Int by lazy {
        arguments?.getInt("fileId")!!
    }

    companion object {
        private const val TAG = "WriterFragment"
    }

    @Inject
    lateinit var navigation: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MdWriterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            lifecycleScope.launch {
                vm.save(fileId, binding.editTextNote.text.toString(), requireContext())
                navigation.navigateBack()
            }
        }

        binding.edit.setOnClickListener {
            lifecycleScope.launch {
                vm.save(fileId, binding.editTextNote.text.toString(), requireContext())
                navigation.navigateFromWriterToMdReader(fileId)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            lifecycleScope.launch {
                Log.e(TAG, "onViewCreated: ASLDSALS:A:LDS")
                vm.save(fileId, binding.editTextNote.text.toString(), requireContext())
                navigation.navigateBack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, {
                vm.load(fileId)

                vm.content.collect { res ->
                    when (res) {
                        is Result.Error<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - error")
                            Toast.makeText(context, "Ошибка при открытии", Toast.LENGTH_SHORT)
                                .show()
                            res.exception.printStackTrace()
                            navigation.navigateBack()
                        }

                        is Result.Loading<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - loading")
                        }

                        is Result.None<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - none")
                        }

                        is Result.Success<String> -> {
                            Log.i(TAG, "onViewCreated: success")
                            binding.editTextNote.setText(res.data)
                        }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.setNone()
    }
}