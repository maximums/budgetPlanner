package com.endava.budgetplanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TITLE_KEY = "one_button_title_key"
private const val MESSAGE_KEY = "one_button_message_key"
private const val BUTTON_KEY = "one_button_mes_key"

class OneButtonDialog(
    private val callback: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(arguments?.getString(TITLE_KEY))
            .setMessage(arguments?.getString(MESSAGE_KEY))
            .setNeutralButton(arguments?.getString(BUTTON_KEY)) { _, _ ->
                callback()
            }
            .create()
    }

    companion object {

        const val TAG = "one_button_dialog_tag"

        fun newInstance(
            title: String,
            message: String,
            buttonMes: String,
            callback: () -> Unit
        ) = OneButtonDialog(callback).apply {
            isCancelable = false
            arguments = Bundle().apply {
                putString(TITLE_KEY, title)
                putString(MESSAGE_KEY, message)
                putString(BUTTON_KEY, buttonMes)
            }
        }
    }
}