package com.endava.budgetplanner.dashboard.ui.views

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.BaseFragment
import com.endava.budgetplanner.common.dialogs.ErrorDialog
import com.endava.budgetplanner.common.dialogs.OneButtonDialog
import com.endava.budgetplanner.common.ext.lazyUnsafe
import com.endava.budgetplanner.common.ext.provideAppComponent
import com.endava.budgetplanner.common.utils.CurrencyType
import com.endava.budgetplanner.dashboard.adapters.CategoryAdapter
import com.endava.budgetplanner.dashboard.adapters.ViewPagerAdapter
import com.endava.budgetplanner.dashboard.callback.TabCallback
import com.endava.budgetplanner.dashboard.callback.ViewpagerCallback
import com.endava.budgetplanner.dashboard.ui.vm.DashboardFactory
import com.endava.budgetplanner.dashboard.ui.vm.DashboardViewModel
import com.endava.budgetplanner.dashboard.ui.vm.states.DashboardEvent
import com.endava.budgetplanner.dashboard.ui.vm.states.DashboardState
import com.endava.budgetplanner.data.models.AmountBalance
import com.endava.budgetplanner.data.models.Category
import com.endava.budgetplanner.data.models.MappedCategory
import com.endava.budgetplanner.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DashboardFragment"

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var factory: DashboardFactory
    private val viewModel by viewModels<DashboardViewModel> {
        factory
    }

    private var mappedCategories: Array<MappedCategory> = arrayOf()
    private var userBalance: AmountBalance? = null
    private val categoryAdapter: CategoryAdapter by lazyUnsafe {
        CategoryAdapter(::navigateToTransactionList)
    }
    private val tabCallback by lazyUnsafe {
        TabCallback {
            viewModel.onClickedTab(it)
        }
    }

    private val viewpagerCallback by lazyUnsafe {
        ViewpagerCallback {
            viewModel.viewpagerPos = it
        }
    }

    private var flagSpinner = false

    override fun onAttach(context: Context) {
        context.provideAppComponent().dashboardComponent().create().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dashNewTransactionButton.isEnabled = false
        registerForContextMenu(binding.containerDashboard)
        setupUI()
        subscribeToObservers()
        refreshData()
    }

    override fun onStart() = with(binding) {
        super.onStart()
        dashNewTransactionButton.setOnClickListener {
            navigateToNewTransaction()
        }
        dashLayoutCategories.dashAddNewTransactionButton.setOnClickListener {
            navigateToNewTransaction()
        }
        dashSignOutButton.setOnClickListener {
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
            )
        }
        dashLayoutCategories.tabLayout.addOnTabSelectedListener(tabCallback)
        binding.dashViewpager.registerOnPageChangeCallback(viewpagerCallback)
    }

    override fun onStop() {
        super.onStop()
        binding.dashLayoutCategories.tabLayout.removeOnTabSelectedListener(tabCallback)
        binding.dashViewpager.unregisterOnPageChangeCallback(viewpagerCallback)
    }

    override fun onDestroyView() {
        userBalance = null
        unregisterForContextMenu(binding.containerDashboard)
        super.onDestroyView()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        userBalance?.let {
            menu.add(CurrencyType.DOLLAR.sign.plus(it.balance.toString()))
                .setOnMenuItemClickListener { return@setOnMenuItemClickListener true }
        }
    }

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        parent: Boolean
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    private fun setupUI() {
        binding.dashLayoutCategories.dashRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun refreshData() {
        binding.dashSwipeRefresh.apply {
            setOnRefreshListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val job = viewModel.fetchData()
                    flagSpinner = true
                    job.join()
                    isRefreshing = false
                    flagSpinner = false
                }
            }
        }
    }

    private fun setupViewPager(
        pieEntries: List<PieEntry>,
        cardAmount: String,
        pieChartAmount: String?,
        @DrawableRes imgRes: Int,
        viewpagerAdapterPos: Int
    ) {
        binding.dashViewpager.apply {
            isVisible = true
            val viewList = listOf<Fragment>(
                CardFragment.newInstance(
                    cardAmount,
                    imgRes
                ),
                PieChartFragment.newInstance(
                    pieEntries,
                    pieChartAmount
                )
            )
            val pagerAdapter = ViewPagerAdapter(
                viewList,
                childFragmentManager,
                lifecycle
            )
            adapter = pagerAdapter
            currentItem = viewpagerAdapterPos
        }
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest { state ->
                        when (state) {
                            DashboardState.Empty -> setProgressBarVisibility(false)
                            DashboardState.Loading -> setProgressBarVisibility(true)
                            is DashboardState.Success -> {
                                binding.dashNewTransactionButton.isEnabled = true
                                setMappedCategories(state.mappedCategories.toTypedArray())
                                setupViewPager(
                                    state.pieEntries,
                                    getString(state.templateAmount, state.cardAmount),
                                    getString(state.templateAmount, state.pieChartAmount),
                                    state.cardImg,
                                    state.viewpagerAdapterPos
                                )
                                setBalance(state.amount)
                                selectTabIfNotSelected(state.pos)
                                submitList(state.list)
                            }
                            is DashboardState.NoTransactions -> {
                                binding.dashNewTransactionButton.isEnabled = true
                                setMappedCategories(state.mappedCategories.toTypedArray())
                                setupViewPager(
                                    listOf(),
                                    getString(state.initialAmount),
                                    null,
                                    state.cardImg,
                                    state.viewpagerAdapterPos
                                )
                                setBalance(state.amount)
                                selectTabIfNotSelected(state.pos)
                                setTextToNoTransaction(state.textId)
                            }
                        }
                    }
                }
                launch {
                    viewModel.channel.collectLatest { event ->
                        when (event) {
                            DashboardEvent.ConnectionError -> showConnectionErrorDialog()
                            is DashboardEvent.Error -> showErrorAlertDialog(event.message)
                            is DashboardEvent.ErrorId -> showSnackBar(event.textId)
                        }
                    }
                }
            }
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        if(!flagSpinner){
            binding.dashLayoutCategories.dashProgressBar.isVisible = isVisible
        }
    }

    private fun selectTabIfNotSelected(pos: Int) {
        val tab = binding.dashLayoutCategories.tabLayout.getTabAt(pos)
        tab?.let {
            if (!it.isSelected)
                binding.dashLayoutCategories.tabLayout.selectTab(it)
        }
    }

    private fun navigateToNewTransaction() {
        if (mappedCategories.isEmpty())
            showSnackBar(R.string.empty_list_of_categories_error)
        else
            userBalance?.let {
                findNavController().navigate(
                    DashboardFragmentDirections.actionDashboardFragmentToTransactionFragment(
                        mappedCategories, it
                    )
                )
            }
    }

    private fun setMappedCategories(categories: Array<MappedCategory>) {
        if (!categories.contentEquals(mappedCategories)) {
            mappedCategories = categories
        }
    }

    private fun showConnectionErrorDialog() {
        OneButtonDialog.newInstance(
            getString(R.string.lost_internet_connection),
            getString(R.string.lost_internet_connection_mes),
            getString(R.string.retry)
        ) {
            viewModel.fetchData()
        }.show(childFragmentManager, OneButtonDialog.TAG)
    }

    private fun setBalance(amountBalance: AmountBalance) {
        if (userBalance != amountBalance) {
            binding.dashAmount.text = amountBalance.category
            userBalance = amountBalance
        }
    }

    private fun setTextToNoTransaction(@StringRes textId: Int) {
        binding.dashLayoutCategories.dashRecView.isVisible = false
        binding.dashLayoutCategories.dashNoTransactionLayout.isVisible = true
        binding.dashLayoutCategories.dashNoTransactionText.setText(textId)
    }

    private fun submitList(list: List<Category>) {
        binding.dashLayoutCategories.dashRecView.isVisible = true
        binding.dashLayoutCategories.dashNoTransactionLayout.isVisible = false
        categoryAdapter.submitList(list)
    }

    private fun showErrorAlertDialog(message: String) {
        ErrorDialog.newInstance(
            getString(R.string.error),
            message,
            getString(R.string.got_it)
        ).show(childFragmentManager, ErrorDialog.TAG)
    }

    private fun navigateToTransactionList(id: Int) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToTransactionListFragment(id)
        )
    }
}