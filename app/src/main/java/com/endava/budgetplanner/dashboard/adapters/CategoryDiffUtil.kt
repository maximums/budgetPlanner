package com.endava.budgetplanner.dashboard.adapters

import androidx.recyclerview.widget.DiffUtil
import com.endava.budgetplanner.data.models.Category

object CategoryDiffUtil : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}