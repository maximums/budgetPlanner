package com.endava.budgetplanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import com.endava.budgetplanner.databinding.DialogDeleteTransactionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TITLE_KEY = "two_button_title_key"
private const val MESSAGE_KEY = "two_button_message_key"
private const val POSITIVE_BUTTON_KEY = "two_positive_button_key"
private const val NEGATIVE_BUTTON_KEY = "two_negative_button_key"

class CustomDialog(
    private val positiveCallback: () -> Unit,
    private val negativeCallback: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogDeleteTransactionBinding.inflate(layoutInflater)
        arguments?.let {
            with(binding) {
                dialogDeleteTransactionTitle.text = it.getString(TITLE_KEY)
                dialogDeleteTransactionMessage.text = it.getString(MESSAGE_KEY)
                dialogDeleteTransactionPositiveBtn.text = it.getString(POSITIVE_BUTTON_KEY)
                dialogDeleteTransactionNegativeBtn.text = it.getString(NEGATIVE_BUTTON_KEY)
                dialogDeleteTransactionPositiveBtn.setOnClickListener {
                    positiveCallback()
                    dismiss()
                }
                dialogDeleteTransactionNegativeBtn.setOnClickListener {
                    negativeCallback()
                    dismiss()
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "two_buttons_dialog_tag"

        fun newInstance(
            title: String,
            message: String,
            positiveButtonTitle: String,
            negativeButtonTitle: String,
            positiveCallback: () -> Unit,
            negativeCallback: () -> Unit
        ) = CustomDialog(positiveCallback, negativeCallback).apply {
            isCancelable = false
            arguments = Bundle().apply {
                putString(TITLE_KEY, title)
                putString(MESSAGE_KEY, message)
                putString(POSITIVE_BUTTON_KEY, positiveButtonTitle)
                putString(NEGATIVE_BUTTON_KEY, negativeButtonTitle)
            }
        }
    }
}