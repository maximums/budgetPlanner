package com.endava.budgetplanner.dashboard.callback

import com.google.android.material.tabs.TabLayout

class TabCallback(
    private val callback: (Int) -> Unit
) : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            callback(it.position)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}