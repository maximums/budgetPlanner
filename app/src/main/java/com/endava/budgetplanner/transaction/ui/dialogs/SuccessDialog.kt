package com.endava.budgetplanner.transaction.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import com.endava.budgetplanner.databinding.DialogSuccessBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val MESSAGE_KEY = "message_key"

class SuccessDialog(
    private val actionNewTransaction: () -> Unit,
    private val actionDone: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogSuccessBinding.inflate(layoutInflater)
        binding.apply {
            successMessage.text = arguments?.getString(MESSAGE_KEY)
            successBtnAddAnother.setOnClickListener {
                actionNewTransaction()
                dismiss()
            }
            successBtnDone.setOnClickListener {
                actionDone()
                dismiss()
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(binding.root)
            .create()
    }

    companion object {

        const val TAG = "success_dialog_tag"

        fun newInstance(message: String, actionDone: () -> Unit, actionNewTransaction: () -> Unit) =
            SuccessDialog(actionNewTransaction, actionDone).apply {
                isCancelable = false
                arguments = Bundle().apply {
                    putString(MESSAGE_KEY, message)
                }
            }
    }
}