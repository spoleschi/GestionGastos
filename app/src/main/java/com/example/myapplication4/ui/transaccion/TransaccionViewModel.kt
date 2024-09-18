package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.Clases.Transaccion

//class TransaccionViewModel : ViewModel() {
//
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is Transaction Fragment"
//    }
//    val text: LiveData<String> = _text
//}


class TransaccionViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Categoria>>()
    val categories: LiveData<List<Categoria>> = _categories

    private val _selectedCategory = MutableLiveData<Categoria>()
    val selectedCategory: LiveData<Categoria> = _selectedCategory

    private val _transactionType = MutableLiveData<TransactionType>()
    val transactionType: LiveData<TransactionType> = _transactionType

    private val _amount = MutableLiveData<Double>()
    val amount: LiveData<Double> = _amount

    init {
        // Inicializar con categorías de ejemplo
        _categories.value = listOf(
            Categoria(1,"Salud", "","#FF0000","Gasto"),
            Categoria(2,"Ocio", "","#00FF00","Gasto"),
            Categoria(3,"Casa", "","#0000FF","Gasto"),
            Categoria(4,"Café", "","#FFFF00","Gasto"),
            Categoria(5,"Educación","", "#FF00FF","Gasto"),
            Categoria(6,"Regalos","", "#00FFFF","Gasto"),
            Categoria(7,"Alimentación","", "#800080","Gasto")
        )
        _transactionType.value = TransactionType.EXPENSE
    }

    fun setSelectedCategory(category: Categoria) {
        _selectedCategory.value = category
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
    }

    fun setAmount(amount: Double) {
        _amount.value = amount
    }

    fun addTransaction(amount: Double, comment: String) {
        // Aquí implementarías la lógica para añadir la transacción a tu base de datos o repositorio
//        val transaction = Transaccion(
//            monto = amount,
//            categoria = _selectedCategory.value!!,
//            tipo = _transactionType.value!!,
//            desc = comment
//        )
        // Guardar la transacción (esto dependerá de tu implementación específica)
    }
}

enum class TransactionType {
    EXPENSE, INCOME
}

