package com.endava.budgetplanner.dashboard.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.ext.toMixedSize
import com.endava.budgetplanner.databinding.FragmentPieChartBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.io.Serializable

private const val PIE_CHART_FRAGMENT_TAG = "budget_data"
private const val TRANSACTIONS = "transactions_data"
private const val TOTAL_AMOUNT = "total_amount"

class PieChartFragment : BaseFragment<FragmentPieChartBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { element ->
            val amount = element.getString(TOTAL_AMOUNT)
            val transaction = element.getSerializable(TRANSACTIONS) as List<PieEntry>
            if (transaction.isNotEmpty() && amount != null) {
                setupPieChart()
                populatePieChart(transaction, amount)
            } else {
                binding.fragmentPiechart.setNoDataText(resources.getString(R.string.no_transaction))
            }
        }
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentPieChartBinding {
        return FragmentPieChartBinding.inflate(inflater, container, false)
    }

    private fun setupPieChart() {
        with(binding) {
            fragmentPiechart.apply {
                invalidate()
                legend.isEnabled = false
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.TRANSPARENT)
                setCenterTextColor(Color.WHITE)
                isRotationEnabled = false
            }
        }
    }

    private fun populatePieChart(pieEntries: List<PieEntry>, totalAmount: String) {
        with(binding) {
            fragmentPiechart.apply {
                data = PieData(
                    PieDataSet(pieEntries, PIE_CHART_FRAGMENT_TAG).apply {
                        colors = pieEntries.map { it.data } as List<Int>
                    }
                ).apply { setDrawValues(false) }
                centerText = totalAmount.toMixedSize(
                    resources.getDimensionPixelSize(R.dimen.font_20),
                    resources.getDimensionPixelSize(R.dimen.font_16),
                    '.'
                )
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(data: List<PieEntry>, totalAmount: String?) = PieChartFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TRANSACTIONS, data as Serializable)
                putString(TOTAL_AMOUNT, totalAmount)
            }
        }
    }
}