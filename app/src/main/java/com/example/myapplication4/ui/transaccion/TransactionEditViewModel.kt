package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.*
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.model.Transaccion
import com.example.myapplication4.repository.CategoryRepository
import com.example.myapplication4.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.time.LocalDate
import java.util.Date

class TransactionEditViewModel(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

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

    private val _transactions = MutableLiveData<List<Transaccion>>()
    val transactions: LiveData<List<Transaccion>> = _transactions

    init {
        _transactionType.value = TransactionType.EXPENSE
        _selectedDate.value = Calendar.getInstance()
        loadTransactions()
        showExpenseCategories()
    }

    private fun loadTransactions() {
        val allTransactions = transactionRepository.getExpenses() + transactionRepository.getIncomes()
        _transactions.value = allTransactions
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
        when (type) {
            TransactionType.EXPENSE -> showExpenseCategories()
            TransactionType.INCOME -> showIncomeCategories()
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
            categoryRepository.expenseCategories.collect{
                _currentCategories.value = it
            }
        }
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

    suspend fun addTransaction(amount: Double, comment: String, date: Calendar, installments: Int, interestRate: Double) {
        val category = _selectedCategory.value ?: return
        val transactionType = _transactionType.value ?: return

        val transaction = when (transactionType) {
            TransactionType.EXPENSE -> Transaccion(
                id = generateId(),
                desc= comment,
                monto = amount.toFloat(),
                fecha = Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)),
                categoriaId = category.id,
                cantCuotas = installments,
                interes = interestRate.toFloat(),
                tipo = "Gasto"
            )
            TransactionType.INCOME -> Transaccion(
                id = generateId(),
                desc= comment,
                monto = amount.toFloat(),
                fecha = Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)),
                categoriaId = category.id,
                cantCuotas = installments,
                interes = interestRate.toFloat(),
                tipo = "Ingreso"
            )
        }

        // Add the new transaction to the repository
        when (transactionType) {
//            TransactionType.EXPENSE -> transactionRepository.addExpense(transaction as Gasto)
//            TransactionType.INCOME -> transactionRepository.addIncome(transaction as Ingreso)
            TransactionType.EXPENSE -> transactionRepository.addTransaction(transaction)
            TransactionType.INCOME -> transactionRepository.addTransaction(transaction)
        }

        // Reload transactions to reflect the changes
        loadTransactions()
    }

    private fun generateId(): Int {
        return (_transactions.value?.size ?: 0) + 1
    }
}

//enum class TransactionType {
//    EXPENSE, INCOME
//}

class TransactionEditViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionEditViewModel(categoryRepository, transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}