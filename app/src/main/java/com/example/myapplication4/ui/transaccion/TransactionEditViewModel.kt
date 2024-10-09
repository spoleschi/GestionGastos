package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.*
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.Clases.Gasto
import com.example.myapplication4.Clases.Ingreso
import com.example.myapplication4.Clases.Transaccion
import com.example.myapplication4.repository.CategoryRepository
import java.util.Calendar
import java.time.LocalDate

class TransaccionViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

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

    private val _transactions = MutableLiveData<MutableList<Transaccion>>()
    val transactions: LiveData<MutableList<Transaccion>> = _transactions

    init {
        _transactionType.value = TransactionType.EXPENSE
        _selectedDate.value = Calendar.getInstance()
        _transactions.value = mutableListOf()
        showExpenseCategories()
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
        when (type) {
            TransactionType.EXPENSE -> showExpenseCategories()
            TransactionType.INCOME -> showIncomeCategories()
        }
    }

    fun showExpenseCategories() {
        _currentCategories.value = categoryRepository.getExpenseCategories()
    }

    fun showIncomeCategories() {
        _currentCategories.value = categoryRepository.getIncomeCategories()
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

    fun addTransaction(amount: Double, comment: String, date: Calendar, installments: Int, interestRate: Double) {
        val category = _selectedCategory.value ?: return
        val transactionType = _transactionType.value ?: return

        val transaction = when (transactionType) {
            TransactionType.EXPENSE -> Gasto(
                idGasto = generateId(),
                descGasto = comment,
                montoGasto = amount.toFloat(),
                fechaGasto = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)),
                categoriaGasto = category,
                cantCuotas = installments,
                interes = interestRate.toFloat()
            )
            TransactionType.INCOME -> Ingreso(
                idIngreso = generateId(),
                descIngreso = comment,
                montoIngreso = amount.toFloat(),
                fechaIngreso = LocalDate.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH)),
                categoriaIngreso = category
            )
        }

        val currentTransactions = _transactions.value ?: mutableListOf()
        currentTransactions.add(transaction)
        _transactions.value = currentTransactions
    }

    private fun generateId(): Int {
        return (_transactions.value?.size ?: 0) + 1
    }
}

enum class TransactionType {
    EXPENSE, INCOME
}

class TransaccionViewModelFactory(private val categoryRepository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransaccionViewModel(categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}