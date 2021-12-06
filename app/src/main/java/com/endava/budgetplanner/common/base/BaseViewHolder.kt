package com.endava.budgetplanner.common.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<out V : ViewBinding, in I : Item>(protected val binding: V) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: I)
}