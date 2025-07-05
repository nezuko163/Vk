package com.nezuko.mdreader

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nezuko.domain.model.Result
import com.nezuko.domain.repository.Navigation
import com.nezuko.mdreader.databinding.MdReaderLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReaderFragment : Fragment() {
    private val vm: ReaderViewModel by activityViewModels()
    private lateinit var binding: MdReaderLayoutBinding

    companion object {
        private const val TAG = "ReaderFragment"
    }

    private val fileId: Int by lazy {
        arguments?.getInt("fileId")!!
    }

    @Inject
    lateinit var navigation: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MdReaderLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            navigation.navigateBack()
        }

        binding.edit.setOnClickListener {
            navigation.navigateFromReaderToMdWriter(fileId)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, {
                vm.load(fileId)

                vm.content.collect { res ->
                    when (res) {
                        is Result.Error<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - error")
                            res.exception.printStackTrace()
                        }
                        is Result.Loading<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - loading")
                        }
                        is Result.None<*> -> {
                            Log.i(TAG, "onViewCreated: state flow - none")
                        }
                        is Result.Success<View> -> {
                            Log.i(TAG, "onViewCreated: success")
                            binding.mdContainer.removeAllViews()
                            binding.mdContainer.addView(res.data)
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