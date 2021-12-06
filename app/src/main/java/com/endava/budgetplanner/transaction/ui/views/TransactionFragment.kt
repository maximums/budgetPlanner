package com.endava.budgetplanner.transaction.ui.views

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.dialogs.OneButtonDialog
import com.endava.budgetplanner.common.ext.*
import com.endava.budgetplanner.common.utils.DateHelper
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.data.models.DateFormatter
import com.endava.budgetplanner.data.models.Transaction
import com.endava.budgetplanner.databinding.FragmentTransactionBinding
import com.endava.budgetplanner.transaction.ui.dialogs.CustomDatePickerDialog
import com.endava.budgetplanner.transaction.ui.dialogs.SuccessDialog
import com.endava.budgetplanner.transaction.ui.vm.TransactionFactory
import com.endava.budgetplanner.transaction.ui.vm.TransactionViewModel
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionEvent
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionState
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionValidateEvent
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val EMPTY_FIELD = ""
private const val MAX_LENGTH_NOTES = 250
private const val AMOUNT_DECIMALS = 2

class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val args: TransactionFragmentArgs by navArgs()

    @Inject
    lateinit var factory: TransactionFactory
    private val viewModel by viewModels<TransactionViewModel> {
        factory
    }

    private lateinit var selectedDate: DateFormatter
    private var categoryCode: String? = null
    private lateinit var currentType: String
    private var maxAmount: Double? = null

    override fun onAttach(context: Context) {
        context.provideAppComponent().transactionComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.transaction_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        subscribeToObservers()
        binding.edtAmount.decimalLimit(AMOUNT_DECIMALS)
        binding.transNotes.lengthLimit(MAX_LENGTH_NOTES)
    }

    override fun onStart() = with(binding) {
        super.onStart()
        btnDate.setOnClickListener {
            showDatePickerDialog()
        }
        transactionToggle.setOnCheckedChangeListener { _, id ->
            currentType = if (id == transactionRadioBtnExpense.id) {
                setupChips()
                maxAmount = args.balance.balance
                TransactionType.EXPENSES.typeName
            } else {
                maxAmount = null
                setupChips(TransactionType.INCOME)
                TransactionType.INCOME.typeName
            }
        }
        transactionChipGroup.setOnCheckedChangeListener { group, _ ->
            val chipId = group.checkedChipId
            categoryCode = if (chipId != View.NO_ID) {
                group.findViewById<Chip>(chipId).tag.toString()
            } else {
                null
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_trans_save -> {
            if (categoryCode != null) {
                viewModel.saveMenuItemClick(
                    getTransactionBody(),
                    maxAmount,
                    selectedDate.date
                )
            } else {
                showSnackBar(R.string.no_category_selected)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentTransactionBinding {
        return FragmentTransactionBinding.inflate(inflater, container, parent)
    }

    private fun setupUI() = with(binding) {
        edtAmount.setText(EMPTY_FIELD)
        edtTitle.setText(EMPTY_FIELD)
        transNotes.setText(EMPTY_FIELD)
        transactionRadioBtnExpense.isChecked = true
        categoryCode = null
        currentType = TransactionType.EXPENSES.typeName
        val currentDate = DateHelper.getCurrentDate()
        binding.btnDate.text = currentDate.formatted
        selectedDate = currentDate
        setupChips()
    }

    private fun setupChips(type: TransactionType = TransactionType.EXPENSES) = with(binding) {
        val categories = args.categories.filter { it.transactionType == type }
        transactionChipGroup.removeAllViews()
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                transactionCategoryChip(
                    category.name,
                    category.code.code,
                    category.color
                )
            }
            transactionChipGroup.addView(chip)
        }
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        when (state) {
                            TransactionState.Empty -> setProgressBarVisibility(false)
                            TransactionState.Loading -> setProgressBarVisibility(true)
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            TransactionEvent.ClearFields -> setupUI()
                            TransactionEvent.ConnectionError -> showConnectionErrorDialog()
                            is TransactionEvent.Error -> showSnackBar(event.message)
                            is TransactionEvent.ErrorId -> showSnackBar(event.messageId)
                            TransactionEvent.NavigateBack -> findNavController().popBackStack()
                            is TransactionEvent.Success -> showSuccessDialog(
                                event.amount,
                                event.template
                            )
                            is TransactionEvent.DateSelected -> {
                                binding.btnDate.text = event.date.formatted
                                selectedDate = event.date
                            }
                        }
                    }
                }
                launch {
                    viewModel.channelValidation.collectLatest { event ->
                        if (event is TransactionValidateEvent.Error) {
                            showSnackBar(event.message)
                        }
                    }
                }
            }
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        binding.transactionProgressBar.isVisible = isVisible
    }

    private fun showConnectionErrorDialog() {
        OneButtonDialog.newInstance(
            getString(R.string.lost_internet_connection),
            getString(R.string.lost_internet_connection_mes),
            getString(R.string.retry)
        ) {
            viewModel.addNewTransaction(getTransactionBody())
        }.show(childFragmentManager, OneButtonDialog.TAG)
    }

    private fun showSuccessDialog(amount: Double, @StringRes template: Int) {
        SuccessDialog.newInstance(
            getString(template, amount.toString()),
            actionDone = {
                viewModel.onDoneButtonClicked()
            },
            actionNewTransaction = {
                viewModel.onNewTransactionButtonClicked()
            }
        ).show(childFragmentManager, SuccessDialog.TAG)
    }

    private fun showDatePickerDialog() {
        CustomDatePickerDialog.newInstance { year, month, dayOfMonth ->
            viewModel.onDateSelected(year, month, dayOfMonth)
        }.show(childFragmentManager, CustomDatePickerDialog.TAG)
    }

    private fun getTransactionBody(): Transaction {
        return Transaction(
            name = binding.edtTitle.text.toString(),
            amount = binding.edtAmount.text.toDoubleOrEmpty(),
            type = currentType,
            category = checkNotNull(categoryCode),
            notes = binding.transNotes.text.toString(),
            date = selectedDate.formatted,
        )
    }
}