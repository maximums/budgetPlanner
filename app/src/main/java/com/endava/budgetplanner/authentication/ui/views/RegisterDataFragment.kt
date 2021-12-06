package com.endava.budgetplanner.authentication.ui.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.budgetplanner.authentication.ui.vm.RegisterDataViewModel
import com.endava.budgetplanner.authentication.ui.vm.factories.RegisterDataFactory
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterEvent
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterState
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.callbacks.DefaultTextWatcher
import com.endava.budgetplanner.common.ext.hideKeyboardFrom
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.databinding.FragmentRegisterDataBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisterDataFragment"

class RegisterDataFragment : BaseFragment<FragmentRegisterDataBinding>() {

    @Inject
    lateinit var factory: RegisterDataFactory
    private val viewModel by viewModels<RegisterDataViewModel> {
        factory
    }
    private val textWatcher by lazy {
        DefaultTextWatcher {
            viewModel.handleFields(getName(), getSurname())
        }
    }

    private val args: RegisterDataFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        context.provideAppComponent().registerComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserver()
    }

    override fun onStart() = with(binding) {
        super.onStart()
        edtName.addTextChangedListener(textWatcher)
        edtSurname.addTextChangedListener(textWatcher)
        btnNext.setOnClickListener {
            viewModel.checkFieldsValidation(getName(), getSurname())
        }
    }

    override fun onStop() = with(binding) {
        super.onStop()
        edtName.removeTextChangedListener(textWatcher)
        edtSurname.removeTextChangedListener(textWatcher)
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentRegisterDataBinding {
        return FragmentRegisterDataBinding.inflate(inflater, container, parent)
    }

    private fun subscribeToObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        when (state) {
                            is RegisterState.ButtonState -> binding.btnNext.isEnabled =
                                state.isEnabled
                            RegisterState.Empty -> {
                            }
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            is RegisterEvent.Error -> showSnackBar(event.message)
                            RegisterEvent.NavigateNext -> {
                                hideKeyboardFrom(binding.root)
                                findNavController().navigate(
                                    RegisterDataFragmentDirections
                                        .actionRegisterDataFragmentToRegisterDataIndustryFragment(
                                            User(
                                                firstName = getName(),
                                                lastName = getSurname(),
                                                password = args.user.password,
                                                email = args.user.email
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getName() = binding.edtName.text.toString()

    private fun getSurname() = binding.edtSurname.text.toString()
}