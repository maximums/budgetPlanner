package com.endava.budgetplanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import com.endava.budgetplanner.databinding.DialogLoadingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TITLE_KEY = "title_key"
private const val MESSAGE_KEY = "message_key"

class LoadingDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogLoadingBinding.inflate(layoutInflater)
        arguments?.let {
            with(binding) {
                loadingDialogTitle.text = it.getString(TITLE_KEY)
                loadingDialogMessage.text = it.getString(MESSAGE_KEY)
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "loading_dialog"

        fun newInstance(title: String, message: String) = LoadingDialog().apply {
            arguments = Bundle().apply {
                putString(TITLE_KEY, title)
                putString(MESSAGE_KEY, message)
            }
            isCancelable = false
        }
    }

}