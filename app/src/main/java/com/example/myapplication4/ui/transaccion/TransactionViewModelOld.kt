//package com.example.myapplication4.ui.transaccion
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication4.clases.Transaccion
//import com.example.myapplication4.repository.TransactionRepository
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//
//class TransactionViewModelOld(private val repository: TransactionRepository) : ViewModel() {
//
//    private val _transactions = MutableLiveData<List<Transaccion>>()
//    val transactions: LiveData<List<Transaccion>> = _transactions
//
//    private var currentType = TransactionType.EXPENSE
//    private var currentPeriod = Period.ALL
//
//    init {
//        loadTransactions()
//    }
//
//    fun setTransactionType(type: TransactionType) {
//        currentType = type
//        loadTransactions()
//    }
//
//    fun setPeriod(period: Period) {
//        currentPeriod = period
//        loadTransactions()
//    }
//
//    private fun loadTransactions() {
//        viewModelScope.launch {
//            val allTransactions = when (currentType) {
//                TransactionType.EXPENSE -> repository.getExpenses()
//                TransactionType.INCOME -> repository.getIncomes()
//            }
//
//            val filteredTransactions = when (currentPeriod) {
//                Period.DAY -> allTransactions.filter { it.fecha == LocalDate.now() }
//                Period.WEEK -> allTransactions.filter { it.fecha.isAfter(LocalDate.now().minusWeeks(1)) }
//                Period.MONTH -> allTransactions.filter { it.fecha.isAfter(LocalDate.now().minusMonths(1)) }
//                Period.YEAR -> allTransactions.filter { it.fecha.isAfter(LocalDate.now().minusYears(1)) }
//                Period.ALL -> allTransactions
//            }
//
//            _transactions.postValue(filteredTransactions)
//        }
//    }
//}
//
////enum class TransactionType {
////    EXPENSE, INCOME
////}
//
////enum class Period {
////    DAY, WEEK, MONTH, YEAR, ALL
////}
//
//class TransactionViewModelFactoryOld(private val repository: TransactionRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return TransactionViewModelOld(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}