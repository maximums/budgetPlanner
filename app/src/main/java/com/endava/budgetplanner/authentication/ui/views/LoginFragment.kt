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
import com.endava.budgetplanner.R
import com.endava.budgetplanner.authentication.ui.vm.LoginViewModel
import com.endava.budgetplanner.authentication.ui.vm.factories.LoginFactory
import com.endava.budgetplanner.authentication.ui.vm.states.LoginEvent
import com.endava.budgetplanner.authentication.ui.vm.states.LoginState
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.callbacks.DefaultTextWatcher
import com.endava.budgetplanner.common.dialogs.ErrorDialog
import com.endava.budgetplanner.common.dialogs.LoadingDialog
import com.endava.budgetplanner.common.dialogs.OneButtonDialog
import com.endava.budgetplanner.common.ext.hideKeyboardFrom
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginFragment"

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var factory: LoginFactory
    private val viewModel by viewModels<LoginViewModel> {
        factory
    }
    private var loadingDialog: LoadingDialog? = null
    private val textWatcher by lazy {
        DefaultTextWatcher {
            viewModel.handleFields(getEmail(), getPassword())
        }
    }

    override fun onAttach(context: Context) {
        context.provideAppComponent().loginComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserver()
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            loginEmail.addTextChangedListener(textWatcher)
            loginPassword.addTextChangedListener(textWatcher)
            buttonSignIn.setOnClickListener {
                viewModel.checkFieldsValidation(getEmail(), getPassword())
            }
            buttonSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.loginEmail.removeTextChangedListener(textWatcher)
        binding.loginPassword.removeTextChangedListener(textWatcher)
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, parent)
    }

    private fun subscribeToObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginState.collectLatest { state ->
                        when (state) {
                            is LoginState.ButtonState -> binding.buttonSignIn.isEnabled =
                                state.isEnabled
                            LoginState.Empty -> loadingDialog?.dismiss()
                            LoginState.Loading -> showLoadingDialog()
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            LoginEvent.ConnectionError -> showConnectionErrorDialog()
                            is LoginEvent.NavigateNext -> {
                                hideKeyboardFrom(binding.root)
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
                                )
                            }
                            is LoginEvent.NetworkError -> showErrorDialog(event.message)
                            is LoginEvent.ValidationError -> showSnackBar(event.message)
                        }
                    }
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        ErrorDialog.newInstance(
            getString(R.string.authentication_failed),
            message,
            getString(R.string.got_it)
        ).show(parentFragmentManager, ErrorDialog.TAG)
    }

    private fun showConnectionErrorDialog() {
        OneButtonDialog.newInstance(
            getString(R.string.lost_internet_connection),
            getString(R.string.lost_internet_connection_mes),
            getString(R.string.retry)
        ) {
            viewModel.checkFieldsValidation(getEmail(), getPassword())
        }.show(parentFragmentManager, OneButtonDialog.TAG)
    }

    private fun getEmail() = binding.loginEmail.text.toString()

    private fun getPassword() = binding.loginPassword.text.toString()

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog.newInstance(
            getString(R.string.please_wait),
            getString(R.string.your_request_is_being_processed)
        )
        loadingDialog?.show(parentFragmentManager, LoadingDialog.TAG)
    }
}