package com.endava.budgetplanner.common.callbacks

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.endava.budgetplanner.R

private const val CORNER_RADIUS = 20f
private const val DIVIDER = 2
private const val HORIZONTAL_SWIPE_DIVIDER = 4
private const val INITIAL_NUMBER = 0

class SwipeToDeleteCallback(private val context: Context, private val callback: (id: Int) -> Unit) :
    ItemTouchHelper.SimpleCallback(INITIAL_NUMBER, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX / HORIZONTAL_SWIPE_DIVIDER,
            dY,
            actionState,
            isCurrentlyActive
        )
        val itemView = viewHolder.itemView
        if (dX < INITIAL_NUMBER) {
            getItemBackground(itemView, dX).draw(c)
            getIcon(itemView)?.draw(c)
        }
    }

    private fun getIcon(itemView: View): Drawable? {
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_bin)
        if (icon != null) {
            val iconMargin = (itemView.height - icon.intrinsicHeight) / DIVIDER
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / DIVIDER
            val iconBot = iconTop + icon.intrinsicHeight
            val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBot)
        }
        return icon
    }

    private fun getItemBackground(itemView: View, dX: Float) = with(itemView) {
        GradientDrawable().apply {
            setColor(context.getColor(R.color.delete_transaction_item_background))
            cornerRadius = CORNER_RADIUS
            setBounds(
                right + dX.toInt() - CORNER_RADIUS.toInt(),
                top,
                right,
                bottom
            )
        }
    }
}