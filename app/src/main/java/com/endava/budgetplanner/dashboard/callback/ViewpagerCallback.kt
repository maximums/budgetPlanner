package com.endava.budgetplanner.dashboard.callback

import androidx.viewpager2.widget.ViewPager2

class ViewpagerCallback(val callback: (Int)->Unit) : ViewPager2.OnPageChangeCallback(){
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        callback(position)
    }
}