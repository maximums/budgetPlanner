package com.endava.budgetplanner.dashboard.ui.vm

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.AmountFormatter
import com.endava.budgetplanner.common.base.RoundFormatter
import com.endava.budgetplanner.common.ext.filterByNoTransactions
import com.endava.budgetplanner.common.ext.filterByType
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.dashboard.ui.vm.states.DashboardEvent
import com.endava.budgetplanner.dashboard.ui.vm.states.DashboardState
import com.endava.budgetplanner.data.models.AmountBalance
import com.endava.budgetplanner.data.models.Category
import com.endava.budgetplanner.data.models.MappedCategory
import com.endava.budgetplanner.data.repo.contract.DashboardRepository
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val INITIAL_NUMBER = 0

class DashboardViewModel(
    private val tokenPreferences: TokenPreferences,
    private val dashboardRepository: DashboardRepository,
    private val formatter: AmountFormatter,
    private val roundFormatterWithPostFix: RoundFormatter,
    private val roundFormatter: RoundFormatter
) : ViewModel() {

    init {
        fetchData()
    }

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Empty)
    val state get() = _state.asStateFlow()

    private val _channel = Channel<DashboardEvent>()
    val channel get() = _channel.consumeAsFlow()

    private var mappedCategories: List<MappedCategory> = listOf()
    private var categoriesToShow: List<Category> = listOf()
    private var balance: AmountBalance? = null
    private var requestProceeding = false
    var viewpagerPos = INITIAL_NUMBER

    private suspend fun getToken(): String? {
        return tokenPreferences.getData { it[TokenPreferences.TOKEN_KEY] }.first()
    }

    fun fetchData() = viewModelScope.launch {
        val token = getToken()
        if (token != null) {
            requestProceeding = true
            _state.value = DashboardState.Loading
            val resource = dashboardRepository.getListOfCategories(token)
            when (resource) {
                Resource.ConnectionError -> {
                    _channel.send(DashboardEvent.ConnectionError)
                }
                is Resource.Error -> {
                    _channel.send(DashboardEvent.Error(resource.message))
                }
                is Resource.Success -> {
                    val allCategories = resource.data.categories
                    mappedCategories = mapCategories(allCategories)
                    categoriesToShow = allCategories.filterByNoTransactions()
                    balance = formatBalance(resource.data.currentAmount)
                    getListOfCategories()
                }
                Resource.SuccessEmpty -> _channel.send(DashboardEvent.ErrorId(R.string.unknown_error))
            }
            _state.value = DashboardState.Empty
            requestProceeding = false
        } else {
            _channel.send(DashboardEvent.ErrorId(R.string.invalid_token_error))
        }
    }

    fun onClickedTab(pos: Int = INITIAL_NUMBER) {
        if (!requestProceeding)
            getListOfCategories(pos)
    }

    private fun getListOfCategories(pos: Int = INITIAL_NUMBER) {
        if (balance != null) {
            _state.value = when (pos) {
                INITIAL_NUMBER -> {
                    val expenseList = categoriesToShow.filterByType(TransactionType.EXPENSES)
                    if (expenseList.isEmpty()) {
                        DashboardState.NoTransactions(
                            pos,
                            R.string.no_expense_transaction,
                            checkNotNull(balance),
                            R.string.initial_amount,
                            R.drawable.ic_initial_card_exp,
                            viewpagerPos,
                            mappedCategories
                        )
                    } else {
                        DashboardState.Success(
                            pos,
                            expenseList,
                            checkNotNull(balance),
                            checkNotNull(
                                formatCardAmount(
                                    expenseList,
                                    TransactionType.EXPENSES
                                )
                            ),
                            checkNotNull(
                                formatPieChartAmount(
                                    expenseList,
                                    TransactionType.EXPENSES
                                )
                            ),
                            R.string.expense_format_template,
                            R.drawable.ic_card_exp,
                            mapPieEntries(expenseList),
                            viewpagerPos,
                            mappedCategories
                        )
                    }
                }
                else -> {
                    val incomeList = categoriesToShow.filterByType(TransactionType.INCOME)
                    if (incomeList.isEmpty()) {
                        DashboardState.NoTransactions(
                            pos,
                            R.string.no_income_transaction,
                            checkNotNull(balance),
                            R.string.initial_amount,
                            R.drawable.ic_initial_card_inc,
                            viewpagerPos,
                            mappedCategories
                        )
                    } else {
                        DashboardState.Success(
                            pos,
                            incomeList,
                            checkNotNull(balance),
                            checkNotNull(
                                formatCardAmount(
                                    incomeList,
                                    TransactionType.INCOME
                                )
                            ),
                            checkNotNull(
                                formatPieChartAmount(
                                    incomeList,
                                    TransactionType.INCOME
                                )
                            ),
                            R.string.income_format_template,
                            R.drawable.ic_card_inc,
                            mapPieEntries(incomeList),
                            viewpagerPos,
                            mappedCategories
                        )
                    }
                }
            }
        } else {
            fetchData()
        }
    }

    private fun formatBalance(amount: Double) = formatter.format(amount)

    private fun formatCardAmount(
        categories: List<Category>,
        transactionType: TransactionType
    ): String? {
        val sum = sumAmounts(categories)
        return roundFormatter.format(
            sum,
            transactionType
        )
    }

    private fun formatPieChartAmount(
        categories: List<Category>,
        transactionType: TransactionType
    ): String? {
        val sum = sumAmounts(categories)
        return roundFormatterWithPostFix.format(
            sum,
            transactionType
        )
    }

    private fun sumAmounts(categories: List<Category>) = categories.sumOf { it.categoryAmount }

    private fun mapPieEntries(list: List<Category>) = list.map { category ->
        PieEntry(category.categoryAmount.toFloat(), Color.parseColor(category.color))
    }

    private fun mapCategories(list: List<Category>) = list.map { category ->
        MappedCategory(category.name, category.code, category.color, category.type)
    }
}