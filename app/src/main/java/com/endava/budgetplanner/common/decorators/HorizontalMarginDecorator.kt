package com.endava.budgetplanner.common.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val DIVIDER = 2

class HorizontalMarginDecorator(
    private val horizontalMargin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val oneSideMargin = horizontalMargin / 2

        outRect.apply {
            left = oneSideMargin
            right = oneSideMargin
        }
    }
}