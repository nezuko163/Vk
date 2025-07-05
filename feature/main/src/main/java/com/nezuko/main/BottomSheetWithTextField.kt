package com.nezuko.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nezuko.main.databinding.BottomSheetWithTextFieldBinding

class BottomSheetWithTextField(
    private val onBtnClick: (String) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetWithTextFieldBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetWithTextFieldBinding.inflate(inflater, container, false)
        return binding.root
    }
//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//
//        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//        imm?.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT)
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editText.requestFocus()
        binding.btn.setOnClickListener {
            dismiss()
            onBtnClick(binding.editText.text.toString())
        }

    }
}