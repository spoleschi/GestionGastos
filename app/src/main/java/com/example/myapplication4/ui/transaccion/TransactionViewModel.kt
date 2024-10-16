package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.*
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.clases.Gasto
import com.example.myapplication4.clases.Ingreso
import com.example.myapplication4.clases.Transaccion
import com.example.myapplication4.repository.CategoryRepository
import com.example.myapplication4.repository.TransactionRepository
import java.time.LocalDate
import java.util.*

class TransactionViewModel(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaccion>>()
    val transactions: LiveData<List<Transaccion>> = _transactions

    private val _currentCategories = MutableLiveData<List<Categoria>>()
    val currentCategories: LiveData<List<Categoria>> = _currentCategories

    private val _selectedCategory = MutableLiveData<Categoria>()
    val selectedCategory: LiveData<Categoria> = _selectedCategory

    private val _transactionType = MutableLiveData<TransactionType>()
    val transactionType: LiveData<TransactionType> = _transactionType

    private val _amount = MutableLiveData<Double>()
    val amount: LiveData<Double> = _amount

    private val _selectedDate = MutableLiveData<Calendar>()
    val selectedDate: LiveData<Calendar> = _selectedDate

    private val _installments = MutableLiveData<Int>()
    val installments: LiveData<Int> = _installments

    private val _interestRate = MutableLiveData<Double>()
    val interestRate: LiveData<Double> = _interestRate

    private var currentPeriod = Period.ALL
    private var currentType = TransactionType.EXPENSE
    private var editingTransaction: Transaccion? = null

    init {
        _transactionType.value = TransactionType.EXPENSE
        _selectedDate.value = Calendar.getInstance()
        loadTransactions()
        showExpenseCategories()
    }

    private fun loadTransactions() {
//        val allTransactions = transactionRepository.getAllTransactions()
        val allTransactions = when (_transactionType.value) {
            TransactionType.EXPENSE -> transactionRepository.getExpenses()
            TransactionType.INCOME -> transactionRepository.getIncomes()
            else ->  transactionRepository.getExpenses()
        }


        _transactions.value = filterTransactionsByPeriod(allTransactions)
    }

    private fun filterTransactionsByPeriod(transactions: List<Transaccion>): List<Transaccion> {
        val currentDate = Calendar.getInstance()
        return when (currentPeriod) {
            Period.DAY -> transactions.filter { it.fecha == LocalDate.now() }
            Period.WEEK -> transactions.filter { it.fecha.isAfter(LocalDate.now().minusWeeks(1)) }
            Period.MONTH -> transactions.filter { it.fecha.isAfter(LocalDate.now().minusMonths(1)) }
            Period.YEAR -> transactions.filter { it.fecha.isAfter(LocalDate.now().minusYears(1)) }
            Period.ALL -> transactions
        }
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
        when (type) {
            TransactionType.EXPENSE -> {
                showExpenseCategories()
                showExpenses()
            }
            TransactionType.INCOME -> {
                showIncomeCategories()
                showIncomes()
            }
        }
    }

    fun showExpenseCategories() {
        _currentCategories.value = categoryRepository.getExpenseCategories()
    }

    fun showIncomeCategories() {
        _currentCategories.value = categoryRepository.getIncomeCategories()
    }
    fun showExpenses() {
        _transactions.value = transactionRepository.getExpenses()
    }

    fun showIncomes() {
        _transactions.value = transactionRepository.getIncomes()
    }

    fun setPeriod(period: Period) {
        currentPeriod = period
        loadTransactions()
    }

    fun setSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        _selectedDate.value = calendar
    }

    fun setSelectedCategory(category: Categoria) {
        _selectedCategory.value = category
    }

    fun setAmount(amount: Double) {
        _amount.value = amount
    }

    fun setInstallments(installments: Int) {
        _installments.value = installments
    }

    fun setInterestRate(rate: Double) {
        _interestRate.value = rate
    }

    fun selectTransaction(transaction: Transaccion) {
        editingTransaction = transaction
        _transactionType.value = when (transaction) {
            is Gasto -> TransactionType.EXPENSE
            is Ingreso -> TransactionType.INCOME
            else -> null
        }
        _amount.value = transaction.monto.toDouble()
        _selectedDate.value = Calendar.getInstance().apply {
            set(transaction.fecha.year, transaction.fecha.monthValue - 1, transaction.fecha.dayOfMonth)
        }
        _selectedCategory.value = transaction.categoria
        if (transaction is Gasto) {
            _installments.value = transaction.cantCuotas
            _interestRate.value = transaction.interes.toDouble()
        }
    }

    fun prepareForNewTransaction() {
        editingTransaction = null
        resetEditFields()
    }

    fun resetEditFields() {
        _amount.value = 0.0
        _selectedDate.value = Calendar.getInstance()
        _selectedCategory.value = null
        _installments.value = 1
        _interestRate.value = 0.0
    }

    fun saveTransaction(amount: Double, comment: String, installments: Int, interestRate: Double) {
        val category = _selectedCategory.value ?: return
        val date = _selectedDate.value ?: return
        val localDate = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH))

        val transaction = when (_transactionType.value) {
            TransactionType.EXPENSE -> Gasto(
                idGasto = editingTransaction?.id ?: generateId(),
                descGasto = comment,
                montoGasto = amount.toFloat(),
                fechaGasto = localDate,
                categoriaGasto = category,
                cantCuotas = installments,
                interes = interestRate.toFloat()
            )
            TransactionType.INCOME -> Ingreso(
                idIngreso = editingTransaction?.id ?: generateId(),
                descIngreso = comment,
                montoIngreso = amount.toFloat(),
                fechaIngreso = localDate,
                categoriaIngreso = category
            )
            null -> return
        }

        if (editingTransaction != null) {
            transactionRepository.updateTransaction(transaction)
        } else {
            transactionRepository.addTransaction(transaction)
        }

        loadTransactions()
        resetEditFields()
    }

    private fun generateId(): Int {
        return (_transactions.value?.size ?: 0) + 1
    }
}

enum class TransactionType {
    EXPENSE, INCOME
}

enum class Period {
    DAY, WEEK, MONTH, YEAR, ALL
}

class TransactionViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(categoryRepository, transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}