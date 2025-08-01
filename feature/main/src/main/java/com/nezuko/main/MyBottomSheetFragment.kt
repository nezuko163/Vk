package com.nezuko.main

import android.app.Dialog
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
        }

        return dialog
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