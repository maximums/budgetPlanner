package com.endava.budgetplanner.dashboard.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.ext.toMixedSize
import com.endava.budgetplanner.databinding.FragmentCardBinding

private const val TOTAL_AMOUNT = "amount"
private const val CARD_TYPE = "card_type"

class CardFragment : BaseFragment<FragmentCardBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val amount = it.getString(TOTAL_AMOUNT)
            if (amount != null) {
                setupCard(amount, it.getInt(CARD_TYPE))
            }
        }
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentCardBinding {
        return FragmentCardBinding.inflate(inflater, container, false)
    }

    private fun setupCard(amount: String, type: Int) {
        with(binding) {
            dashCard.setImageResource(type)
            dashCardAmount.text = amount.toMixedSize(
                resources.getDimensionPixelSize(R.dimen.font_27),
                resources.getDimensionPixelSize(R.dimen.font_20),
                '.'
            )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(amount: String, @DrawableRes cardType: Int) = CardFragment().apply {
            arguments = Bundle().apply {
                putString(TOTAL_AMOUNT, amount)
                putInt(CARD_TYPE, cardType)
            }
        }

    }
}