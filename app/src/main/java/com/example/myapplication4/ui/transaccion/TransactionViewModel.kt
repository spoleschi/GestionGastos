package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.*
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.model.Transaccion
import com.example.myapplication4.repository.CategoryRepository
import com.example.myapplication4.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TransactionViewModel(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaccion>>()
    val transactions: LiveData<List<Transaccion>> = _transactions

    private val _currentCategories = MutableLiveData<List<Categoria>>()
    val currentCategories: LiveData<List<Categoria>> = _currentCategories

    private val _selectedCategory = MutableLiveData<Categoria?>()
    val selectedCategory: MutableLiveData<Categoria?> = _selectedCategory

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
        val currentDate = LocalDate.now()

        return when (currentPeriod) {
            Period.DAY -> transactions.filter {
                it.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == currentDate
            }
            Period.WEEK -> transactions.filter {
                it.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(currentDate.minusWeeks(1))
            }
            Period.MONTH -> transactions.filter {
                it.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(currentDate.minusMonths(1))
            }
            Period.YEAR -> transactions.filter {
                it.fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(currentDate.minusYears(1))
            }
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
        viewModelScope.launch {
            categoryRepository.expenseCategories.collect{
                _currentCategories.value = it
            }
        }
    }

    fun showIncomeCategories() {
        viewModelScope.launch {
            categoryRepository.incomeCategories.collect{
                _currentCategories.value = it
            }
        }
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

    fun setSelectedCategory(category: com.example.myapplication4.model.Categoria) {
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

    suspend fun selectTransaction(transaction: Transaccion) {
        editingTransaction = transaction

        if(transaction.tipo === "Gasto"){
            _transactionType.value = TransactionType.EXPENSE
        }
        else if(transaction.tipo === "Ingreso"){
            _transactionType.value = TransactionType.INCOME
        }

        _amount.value = transaction.monto.toDouble()
        _selectedDate.value = Calendar.getInstance().apply {
            time = transaction.fecha
            set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH))
        }

        _selectedCategory.value = categoryRepository.findCategoryById(transaction.categoriaId)
        if (transaction.tipo === "Gasto") {
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

    suspend fun saveTransaction(amount: Double, comment: String, installments: Int, interestRate: Double) {
        val category = _selectedCategory.value ?: return
        val date = _selectedDate.value ?: return
        val localDate = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH))

        val transaction = when (_transactionType.value) {
            TransactionType.EXPENSE -> Transaccion(
                id = editingTransaction?.id ?: generateId(),
                desc= comment,
                monto = amount.toFloat(),
                fecha = Calendar.getInstance().time,
                categoriaId = category.id,
                cantCuotas = installments,
                interes = interestRate.toFloat(),
                tipo = "Gasto"
            )
            TransactionType.INCOME -> Transaccion(
                id = editingTransaction?.id ?: generateId(),
                desc= comment,
                monto = amount.toFloat(),
                fecha = Calendar.getInstance().time,
                categoriaId = category.id,
                cantCuotas = installments,
                interes = interestRate.toFloat(),
                tipo = "Ingreso"
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

    fun setEditingTransaction(transaction: com.example.myapplication4.model.Transaccion) {
        editingTransaction = transaction
    }

    fun getEditingTransaction(): Transaccion? = editingTransaction
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