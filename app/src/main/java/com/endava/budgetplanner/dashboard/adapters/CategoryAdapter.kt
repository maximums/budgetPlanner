package com.endava.budgetplanner.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.endava.budgetplanner.data.models.Category
import com.endava.budgetplanner.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onItemClicked: (Int) -> Unit
) : ListAdapter<Category, CategoryViewHolder>(CategoryDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategoryViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}