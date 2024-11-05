package com.example.myapplication4.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication4.clases.CategorySummary
import com.example.myapplication4.clases.Transaccion
import com.example.myapplication4.repository.TransactionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _categorySummaries = MutableLiveData<List<CategorySummary>>()
    val categorySummaries: LiveData<List<CategorySummary>> = _categorySummaries

    private val _total = MutableLiveData<Float>()
    val total: LiveData<Float> = _total

    private val _tipoTransaccion = MutableLiveData<TipoTransaccion>()
    val tipoTransaccion: LiveData<TipoTransaccion> = _tipoTransaccion

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private var currentTransactionsJob: Job? = null

    init {
        _tipoTransaccion.value = TipoTransaccion.GASTO
        observeTransactions()
    }

    private fun observeTransactions() {
        currentTransactionsJob?.cancel()
        currentTransactionsJob = viewModelScope.launch {
            when (_tipoTransaccion.value) {
                TipoTransaccion.GASTO -> transactionRepository.expenses
                TipoTransaccion.INGRESO -> transactionRepository.incomes
                else -> flowOf(emptyList())
            }.collect { transactions ->
                processTransactions(transactions)
            }
        }
    }

    private fun processTransactions(transactions: List<Transaccion>) {
        viewModelScope.launch {
            val transaccionesFiltradas = if (startDate != null && endDate != null) {
                transactions.filter { transaction ->
                    transaction.fecha.isAfterOrEqual(startDate!!) &&
                            transaction.fecha.isBeforeOrEqual(endDate!!)
                }
            } else {
                transactions
            }

            val total = transaccionesFiltradas.sumOf { it.monto.toDouble() }.toFloat()
            _total.value = total

            val resumenPorCategoria = transaccionesFiltradas
                .groupBy { it.categoria }
                .map { (categoria, transacciones) ->
                    val montoCategoria = transacciones.sumOf { it.monto.toDouble() }.toFloat()
                    val porcentaje = if (total > 0) (montoCategoria / total) * 100 else 0f
                    CategorySummary(
                        category = categoria,
                        total = montoCategoria,
                        percentage = porcentaje
                    )
                }
                .sortedByDescending { it.total }

            _categorySummaries.value = resumenPorCategoria
        }
    }

    fun setTipoTransaccion(tipo: TipoTransaccion) {
        _tipoTransaccion.value = tipo
        observeTransactions()
    }

    fun setPeriod(start: LocalDate?, end: LocalDate?) {
        startDate = start
        endDate = end
        observeTransactions()
    }

    // Funciones de extensión para hacer las comparaciones de fechas más legibles
    private fun LocalDate.isAfterOrEqual(other: LocalDate): Boolean =
        this.isEqual(other) || this.isAfter(other)

    private fun LocalDate.isBeforeOrEqual(other: LocalDate): Boolean =
        this.isEqual(other) || this.isBefore(other)

    override fun onCleared() {
        super.onCleared()
        currentTransactionsJob?.cancel()
    }

    class Factory(
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(transactionRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
enum class TipoTransaccion {
    GASTO, INGRESO
}

