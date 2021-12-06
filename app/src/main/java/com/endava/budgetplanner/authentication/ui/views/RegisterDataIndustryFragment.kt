package com.endava.budgetplanner.authentication.ui.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.budgetplanner.R
import com.endava.budgetplanner.authentication.ui.vm.RegisterIndustryViewModel
import com.endava.budgetplanner.authentication.ui.vm.factories.RegisterIndustryFactory
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterIndustryEvent
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterIndustryState
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.callbacks.DefaultTextWatcher
import com.endava.budgetplanner.common.dialogs.ErrorDialog
import com.endava.budgetplanner.common.dialogs.LoadingDialog
import com.endava.budgetplanner.common.dialogs.OneButtonDialog
import com.endava.budgetplanner.common.ext.decimalLimit
import com.endava.budgetplanner.common.ext.hideKeyboardFrom
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.common.ext.toMixedSize
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.databinding.FragmentRegisterDataIndustryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterDataIndustryFragment : BaseFragment<FragmentRegisterDataIndustryBinding>() {

    @Inject
    lateinit var factory: RegisterIndustryFactory
    private val viewModel by viewModels<RegisterIndustryViewModel> {
        factory
    }

    private val args: RegisterDataIndustryFragmentArgs by navArgs()
    private var industryItem: String? = null

    private var loadingDialog: LoadingDialog? = null
    private val textWatcher by lazy {
        DefaultTextWatcher {
            viewModel.handleFields(getInitialBalance(), industryItem)
        }
    }

    override fun onAttach(context: Context) {
        context.provideAppComponent().registerComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setItemsToDropDownMenu()
        subscribeToObserver()
    }

    override fun onStart() {
        super.onStart()
        binding.edtInitialBalance.apply {
            addTextChangedListener(textWatcher)
            doAfterTextChanged { text ->
                text?.toString()?.let {
                    viewModel.handleBalanceField(it)
                }
            }
        }
        binding.btnSignUp.setOnClickListener {
            viewModel.handleValidationResult(getInitialBalance(), getUser())
        }
    }

    override fun onStop() {
        super.onStop()
        binding.edtInitialBalance.removeTextChangedListener(textWatcher)
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentRegisterDataIndustryBinding {
        return FragmentRegisterDataIndustryBinding.inflate(inflater, container, parent)
    }

    private fun subscribeToObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        when (state) {
                            RegisterIndustryState.Empty -> loadingDialog?.dismiss()
                            RegisterIndustryState.Loading -> showLoadingDialog()
                            is RegisterIndustryState.ButtonState -> binding.btnSignUp.isEnabled =
                                state.isEnabled
                            is RegisterIndustryState.BalanceFieldState -> {
                                if (state.addPrefix) {
                                    binding.edtInitialBalance.apply {
                                        setText(state.text)
                                        setSelection(length())
                                    }
                                }
                                if (state.mixSize) {
                                    setSpannableStringToBalance(state.text)
                                }
                            }
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            RegisterIndustryEvent.ConnectionError -> showConnectionErrorDialog()
                            is RegisterIndustryEvent.Error -> showErrorAlertDialog(event.message)
                            RegisterIndustryEvent.NavigateNext -> {
                                hideKeyboardFrom(binding.root)
                                findNavController()
                                    .navigate(
                                        RegisterDataIndustryFragmentDirections
                                            .actionRegisterDataIndustryFragmentToWelcomeFragment()
                                    )
                            }
                            is RegisterIndustryEvent.ValidationError -> showSnackBar(event.message)
                        }
                    }
                }
            }
        }
    }

    private fun setSpannableStringToBalance(text: String) {
        val span = text.toMixedSize(
            resources.getDimensionPixelSize(R.dimen.font_27),
            resources.getDimensionPixelSize(R.dimen.font_20),
            '.'
        )
        binding.edtInitialBalance.apply {
            setText(span)
            setSelection(text.length)
        }
    }

    private fun setItemsToDropDownMenu() {
        val items = resources.getStringArray(R.array.industry)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_role, items)
        (binding.industry as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            setOnItemClickListener { parent, _, pos, _ ->
                binding.menu.isHintEnabled = false
                industryItem = parent.getItemAtPosition(pos).toString()
                viewModel.handleFields(getInitialBalance(), industryItem)
            }
            setDropDownBackgroundDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_menu_roles)
            )
        }
    }

    private fun showConnectionErrorDialog() {
        OneButtonDialog.newInstance(
            getString(R.string.lost_internet_connection),
            getString(R.string.lost_internet_connection_mes),
            getString(R.string.retry)
        ) {
            viewModel.handleValidationResult(getInitialBalance(), getUser())
        }.show(parentFragmentManager, OneButtonDialog.TAG)
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog.newInstance(
            getString(R.string.please_wait),
            getString(R.string.your_request_is_being_processed)
        )
        loadingDialog?.show(parentFragmentManager, LoadingDialog.TAG)
    }

    private fun getUser() = User(
        args.user.firstName,
        args.user.lastName,
        args.user.password,
        industryItem,
        args.user.email,
        getInitialBalance().toDouble()
    )

    private fun showErrorAlertDialog( message: String) {
        ErrorDialog.newInstance(
            getString(R.string.error),
            message,
            getString(R.string.got_it)
        ).show(parentFragmentManager, ErrorDialog.TAG)
    }

    private fun getInitialBalance() = binding.edtInitialBalance.text.toString().removePrefix("$")
}