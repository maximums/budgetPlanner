package com.endava.budgetplanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import com.endava.budgetplanner.databinding.DialogErrorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TITLE_KEY = "title_key"
private const val MESSAGE_KEY = "message_key"
private const val BUTTON_KEY = "button_key"

class ErrorDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogErrorBinding.inflate(layoutInflater)
        arguments?.let {
            with(binding) {
                errorDialogTitle.text = it.getString(TITLE_KEY)
                errorDialogMessage.text = it.getString(MESSAGE_KEY)
                errorDismissButton.text = it.getString(BUTTON_KEY)
                errorDismissButton.setOnClickListener {
                    dismiss()
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "error_dialog"

        fun newInstance(title: String, message: String, button_text: String) = ErrorDialog().apply {
            arguments = Bundle().apply {
                putString(TITLE_KEY, title)
                putString(MESSAGE_KEY, message)
                putString(BUTTON_KEY, button_text)
            }
            isCancelable = false
        }
    }
}