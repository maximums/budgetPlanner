package com.endava.budgetplanner.transactionlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.endava.budgetplanner.data.models.TransactionResponse
import com.endava.budgetplanner.databinding.ItemTransactionBinding

class TransactionDetailsAdapter :
    ListAdapter<TransactionResponse, TransactionDetailsViewHolder>(TransactionDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailsViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionDetailsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}