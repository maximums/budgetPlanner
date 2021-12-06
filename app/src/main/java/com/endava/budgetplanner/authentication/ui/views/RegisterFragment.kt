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
import com.endava.budgetplanner.authentication.ui.vm.RegisterViewModel
import com.endava.budgetplanner.authentication.ui.vm.factories.RegisterFactory
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterEvent
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterState
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.callbacks.DefaultTextWatcher
import com.endava.budgetplanner.common.ext.hideKeyboardFrom
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    @Inject
    lateinit var factory: RegisterFactory
    private val viewModel by viewModels<RegisterViewModel> {
        factory
    }
    private val textWatcher by lazy {
        DefaultTextWatcher {
            viewModel.handleFields(getEmail(), getPassword(), getPasswordConf())
        }
    }

    override fun onAttach(context: Context) {
        context.provideAppComponent().registerComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().actionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserver()
    }

    override fun onStart() = with(binding) {
        super.onStart()
        edtMail.addTextChangedListener(textWatcher)
        edtPass.addTextChangedListener(textWatcher)
        edtPassConfirmation.addTextChangedListener(textWatcher)
        btnNext.setOnClickListener {
            viewModel.checkFieldsValidation(getEmail(), getPassword(), getPasswordConf())
        }
        txtSignIn.setOnClickListener {
            findNavController()
                .navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    override fun onStop() = with(binding) {
        super.onStop()
        edtMail.removeTextChangedListener(textWatcher)
        edtPass.removeTextChangedListener(textWatcher)
        edtPassConfirmation.removeTextChangedListener(textWatcher)
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, parent)
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
                                findNavController()
                                    .navigate(
                                        RegisterFragmentDirections
                                            .actionRegisterFragmentToRegisterDataFragment(
                                                User(password = getPassword(), email = getEmail())
                                            )
                                    )
                            }
                        }
                    }

                }
            }
        }
    }

    private fun getEmail() = binding.edtMail.text.toString()

    private fun getPassword() = binding.edtPass.text.toString()

    private fun getPasswordConf() = binding.edtPassConfirmation.text.toString()

    companion object {

        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}