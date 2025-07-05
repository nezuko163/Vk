package com.nezuko.mdwriter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nezuko.domain.repository.Navigation
import com.nezuko.mdwriter.databinding.MdWriterLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WriterFragment : Fragment() {
    private lateinit var binding: MdWriterLayoutBinding
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
        binding = MdWriterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            navigation.navigateBack()
        }
        binding.edit.setOnClickListener {
            navigation.navigateFromWriterToMdReader(fileId)
        }
    }
}