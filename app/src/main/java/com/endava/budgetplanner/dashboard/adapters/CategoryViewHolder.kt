package com.endava.budgetplanner.dashboard.adapters

import android.graphics.Color
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseViewHolder
import com.endava.budgetplanner.data.models.Category
import com.endava.budgetplanner.databinding.ItemCategoryBinding

class CategoryViewHolder(
    binding: ItemCategoryBinding,
    private val onItemClicked: (Int) -> Unit
) :
    BaseViewHolder<ItemCategoryBinding, Category>(binding) {

    override fun bind(item: Category) = with(binding) {
        itemView.setOnClickListener {
            onItemClicked(item.id)
        }
        categoryImage.setImageResource(item.code.imageId)
        categoryCard.setCardBackgroundColor(Color.parseColor(item.color))
        categoryTitle.text = item.name
        categoryTransaction.text = itemView.context.resources.getString(
            R.string.transaction_template,
            item.numberOfTransactions
        )
    }
}