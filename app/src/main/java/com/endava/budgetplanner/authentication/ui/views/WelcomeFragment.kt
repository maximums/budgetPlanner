package com.endava.budgetplanner.authentication.ui.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.databinding.FragmentWelcomeBinding

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    override fun onStart() {
        super.onStart()
        binding.welcomeBSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, container, parent)
    }
}