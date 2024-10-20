package com.example.myapplication4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication4.model.Transaccion
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.model.TransaccionDao
import java.time.LocalDate
import java.util.Date

class TransactionRepository(private val transaccionDao: TransaccionDao) {
    private val _expenses = MutableLiveData<List<Transaccion>>()
    val expenses: LiveData<List<Transaccion>> = _expenses

    private val _incomes = MutableLiveData<List<Transaccion>>()
    val incomes: LiveData<List<Transaccion>> = _incomes

    init {
        loadInitialTransactions()
    }

    private fun loadInitialTransactions() {
        val initialExpenses = listOf(
            Transaccion(1, "Compra de televisor", 1500.0f, Date(2023, 5, 15),1,1,0f,"Pago"),
            Transaccion(2, "Factura de luz", 150.0f, Date(2023, 6, 1),3,2,24f,"Pago"),
            Transaccion(3, "Compra de ropa", 300.0f, Date(2024, 8, 20),1,6,40f,"Pago"),
            Transaccion(4, "Cena en restaurante", 80.0f, Date(2024, 9, 12),1,1,20f,"Pago"),
            Transaccion(5, "Visita al dentista", 150.0f, Date(2024, 10, 8),2,2,10f,"Pago")
        )
        _expenses.value = initialExpenses

        val initialIncomes = listOf(
            Transaccion(6, "Salario", 3000.0f, Date(2024, 9, 15),4,1,0f,"Ingreso"),
            Transaccion(7, "Venta de artículos", 500.0f, Date(2024, 10, 8),4,1,0f,"Ingreso")
        )
        _incomes.value = initialIncomes
    }

    suspend fun addTransaction(transaction: Transaccion) {
            //No estoy seguro si la logica es la misma
            if(transaction.tipo === "Gasto"){
                val updatedExpenses = (_expenses.value ?: emptyList()) + transaction
                _expenses.value = updatedExpenses
            }
            else if(transaction.tipo === "Ingreso") {
                val updatedIncomes = (_incomes.value ?: emptyList()) + transaction
                _incomes.value = updatedIncomes
            }
        transaccionDao.insertTransaccion(transaction)
    }

    fun updateTransaction(updatedTransaction: Transaccion) {
        if(updatedTransaction.tipo === "Gasto"){
            val currentList = _expenses.value ?: emptyList()
            val updatedList = currentList.map {
                if (it.id == updatedTransaction.id) updatedTransaction else it
            }
            _expenses.value = updatedList
        }
       else if(updatedTransaction.tipo === "Ingreso"){
            val currentList = _incomes.value ?: emptyList()
            val updatedList = currentList.map {
                if (it.id == updatedTransaction.id) updatedTransaction else it
            }
            _incomes.value = updatedList
       }
    }

    fun deleteTransaction(transaction: Transaccion) {
        if(transaction.tipo === "Gasto"){
                val currentList = _expenses.value ?: emptyList()
                val updatedList = currentList.filter { it.id != transaction.id }
                _expenses.value = updatedList
        }
        else if(transaction.tipo === "Ingreso"){
                val currentList = _incomes.value ?: emptyList()
                val updatedList = currentList.filter { it.id != transaction.id }
                _incomes.value = updatedList
        }
    }

    fun findTransactionById(id: Int): Transaccion? {
        return _expenses.value?.find { it.id == id }
            ?: _incomes.value?.find { it.id == id }
    }

    fun getAllTransactions(): List<Transaccion> {
        return (_expenses.value ?: emptyList()) + (_incomes.value ?: emptyList())
    }

    fun getExpenses(): List<Transaccion> {
        return _expenses.value ?: emptyList()
    }

    fun getIncomes(): List<Transaccion> {
        return _incomes.value ?: emptyList()
    }
}