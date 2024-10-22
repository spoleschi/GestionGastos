package com.example.myapplication4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication4.clases.*
import java.time.LocalDate

class TransactionRepositoryOld {
    private val _expenses = MutableLiveData<List<Gasto>>()
    val expenses: LiveData<List<Gasto>> = _expenses

    private val _incomes = MutableLiveData<List<Ingreso>>()
    val incomes: LiveData<List<Ingreso>> = _incomes

    init {
        loadInitialTransactions()
    }

    private fun loadInitialTransactions() {
        val initialExpenses = listOf(
            Gasto(1, "Compra de televisor", 1500.0f, LocalDate.of(2023, 5, 15),
                Categoria(1, "Electrónica", "Gastos en aparatos electrónicos", "#FF9800", "Gasto"), 12, 0.15f),
            Gasto(2, "Factura de luz", 150.0f, LocalDate.of(2023, 6, 1),
                Categoria(2, "Servicios", "Gastos en servicios básicos", "#4CAF50", "Gasto"), 1, 0.0f),
            Gasto(3, "Compra de ropa", 300.0f, LocalDate.of(2024, 8, 20),
                Categoria(3, "Ropa", "Gastos en vestimenta", "#E91E63", "Gasto"), 3, 0.10f),
            Gasto(4, "Cena en restaurante", 80.0f, LocalDate.of(2024, 9, 12),
                Categoria(4, "Alimentación", "Gastos en comida", "#FFC107", "Gasto"), 1, 0.0f),
            Gasto(5, "Visita al dentista", 150.0f, LocalDate.of(2024, 10, 8),
                Categoria(5, "Salud", "Gastos médicos", "#2196F3", "Gasto"), 2, 0.05f)
        )
        _expenses.value = initialExpenses

        val initialIncomes = listOf(
            Ingreso(1, "Salario", 3000.0f, LocalDate.of(2024, 9, 15),
                Categoria(6, "Salario", "Ingreso por trabajo", "#4CAF50", "Ingreso")),
            Ingreso(2, "Venta de artículos", 500.0f, LocalDate.of(2024, 10, 8),
                Categoria(7, "Ventas", "Ingresos por ventas", "#2196F3", "Ingreso"))
        )
        _incomes.value = initialIncomes
    }

    fun addTransaction(transaction: Transaccion) {
        when (transaction) {
            is Gasto -> {
                val updatedExpenses = (_expenses.value ?: emptyList()) + transaction
                _expenses.value = updatedExpenses
            }
            is Ingreso -> {
                val updatedIncomes = (_incomes.value ?: emptyList()) + transaction
                _incomes.value = updatedIncomes
            }
        }
    }

    fun updateTransaction(updatedTransaction: Transaccion) {
        when (updatedTransaction) {
            is Gasto -> {
                val currentList = _expenses.value ?: emptyList()
                val updatedList = currentList.map {
                    if (it.idGasto == updatedTransaction.idGasto) updatedTransaction else it
                }
                _expenses.value = updatedList
            }
            is Ingreso -> {
                val currentList = _incomes.value ?: emptyList()
                val updatedList = currentList.map {
                    if (it.idIngreso == updatedTransaction.idIngreso) updatedTransaction else it
                }
                _incomes.value = updatedList
            }
        }
    }

    fun deleteTransaction(transaction: Transaccion) {
        when (transaction) {
            is Gasto -> {
                val currentList = _expenses.value ?: emptyList()
                val updatedList = currentList.filter { it.idGasto != transaction.idGasto }
                _expenses.value = updatedList
            }
            is Ingreso -> {
                val currentList = _incomes.value ?: emptyList()
                val updatedList = currentList.filter { it.idIngreso != transaction.idIngreso }
                _incomes.value = updatedList
            }
        }
    }

    fun findTransactionById(id: Int): Transaccion? {
        return _expenses.value?.find { it.idGasto == id }
            ?: _incomes.value?.find { it.idIngreso == id }
    }

    fun getAllTransactions(): List<Transaccion> {
        return (_expenses.value ?: emptyList()) + (_incomes.value ?: emptyList())
    }

    fun getExpenses(): List<Gasto> {
        return _expenses.value ?: emptyList()
    }

    fun getIncomes(): List<Ingreso> {
        return _incomes.value ?: emptyList()
    }
}