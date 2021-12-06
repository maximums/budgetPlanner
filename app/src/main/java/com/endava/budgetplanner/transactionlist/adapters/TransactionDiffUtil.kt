package com.endava.budgetplanner.transactionlist.adapters

import androidx.recyclerview.widget.DiffUtil
import com.endava.budgetplanner.data.models.TransactionResponse

object TransactionDiffUtil : DiffUtil.ItemCallback<TransactionResponse>() {
    override fun areItemsTheSame(oldItem: TransactionResponse, newItem: TransactionResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionResponse, newItem: TransactionResponse): Boolean {
        return oldItem == newItem
    }
}