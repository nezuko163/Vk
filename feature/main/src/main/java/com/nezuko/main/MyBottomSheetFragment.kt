package com.nezuko.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nezuko.main.databinding.BottomSheetLayoutBinding

class MyBottomSheetFragment(
    private val onOpenExitedFile: () -> Unit,
    private val onSearchFileOnInternet: () -> Unit,
    private val onCreateNewFile: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openFileTV.setOnClickListener {
            dismiss()
            onOpenExitedFile()
        }
        binding.openInternetFileTV.setOnClickListener {
            dismiss()
            onSearchFileOnInternet()
        }
        binding.createNewFileTV.setOnClickListener {
            dismiss()
            onCreateNewFile()
        }
    }

}