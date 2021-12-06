package com.endava.budgetplanner.transactionlist.adapters

import com.endava.budgetplanner.common.base.BaseViewHolder
import com.endava.budgetplanner.data.models.TransactionResponse
import com.endava.budgetplanner.databinding.ItemTransactionBinding

class TransactionDetailsViewHolder(binding: ItemTransactionBinding) :
    BaseViewHolder<ItemTransactionBinding, TransactionResponse>(binding) {

    override fun bind(item: TransactionResponse) = with(binding) {
        transactionItemTitle.text = item.name
        transactionItemDate.text = item.date
        transactionItemAmount.text = item.amount
    }
}