package com.endava.budgetplanner.transactionlist.ui.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.callbacks.SwipeToDeleteCallback
import com.endava.budgetplanner.common.dialogs.CustomDialog
import com.endava.budgetplanner.common.dialogs.LoadingDialog
import com.endava.budgetplanner.common.dialogs.OneButtonDialog
import com.endava.budgetplanner.common.ext.lazyUnsafe
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.data.models.TransactionResponse
import com.endava.budgetplanner.databinding.FragmentTransactionListBinding
import com.endava.budgetplanner.transactionlist.adapters.TransactionDetailsAdapter
import com.endava.budgetplanner.transactionlist.ui.vm.TransactionListFactory
import com.endava.budgetplanner.transactionlist.ui.vm.TransactionListViewModel
import com.endava.budgetplanner.transactionlist.ui.vm.state.TransactionListEvent
import com.endava.budgetplanner.transactionlist.ui.vm.state.TransactionListState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class TransactionListFragment : BaseFragment<FragmentTransactionListBinding>() {

    private val args by navArgs<TransactionListFragmentArgs>()

    @Inject
    lateinit var factory: TransactionListFactory.Factory
    private val viewModel by viewModels<TransactionListViewModel> {
        factory.create(args.categoryId)
    }

    private var loadingDialog: LoadingDialog? = null

    override fun onAttach(context: Context) {
        context.provideAppComponent().transactionListComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        subscribeToObservers()
    }

    private val transactionAdapter: TransactionDetailsAdapter by lazyUnsafe {
        TransactionDetailsAdapter()
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentTransactionListBinding {
        return FragmentTransactionListBinding.inflate(inflater, container, parent)
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        when (state) {
                            TransactionListState.Empty -> loadingDialog?.dismiss()
                            TransactionListState.Loading -> showLoadingDialog()
                            is TransactionListState.Success -> {
                                loadingDialog?.dismiss()
                                setupUI(
                                    state.transactions,
                                    state.categoryName,
                                    state.amount
                                )
                            }
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            TransactionListEvent.ConnectionError -> showConnectionErrorDialog()
                            is TransactionListEvent.Error -> showSnackBar(event.message)
                            is TransactionListEvent.ErrorId -> showSnackBar(event.messageId)
                        }
                    }
                }
            }
        }
    }

    private fun showConnectionErrorDialog() {
        OneButtonDialog.newInstance(
            getString(R.string.lost_internet_connection),
            getString(R.string.lost_internet_connection_mes),
            getString(R.string.retry)
        ) {
            viewModel.getListOfTransactionsByCategoryId()
        }.show(parentFragmentManager, OneButtonDialog.TAG)
    }

    private fun setupUI(transactions: List<TransactionResponse>, name: String, amount: String) {
        transactionAdapter.submitList(transactions)
        with(binding) {
            transactionListTitle.text = name
            transactionListAmount.text = amount
        }
    }

    private fun setupRecycler() {
        binding.transactionRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
            ItemTouchHelper(SwipeToDeleteCallback(
                requireContext()
            ) { position: Int ->
                showConfirmDeletingDialog(position)
            }).attachToRecyclerView(
                this
            )
        }
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog.newInstance(
            getString(R.string.please_wait),
            getString(R.string.your_request_is_being_processed)
        )
        loadingDialog?.show(parentFragmentManager, LoadingDialog.TAG)
    }

    private fun showConfirmDeletingDialog(position: Int) {
        CustomDialog.newInstance(
            getString(R.string.transaction_delete_dialog_title),
            getString(R.string.transaction_delete_dialog_message),
            getString(R.string.yes),
            getString(R.string.no),
            {
                viewModel.deleteTransaction(transactionAdapter.currentList, position)
            },
            { transactionAdapter.notifyItemChanged(position) }
        ).show(childFragmentManager, CustomDialog.TAG)
    }

}