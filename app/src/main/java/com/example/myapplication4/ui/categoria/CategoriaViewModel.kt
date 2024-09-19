package com.example.myapplication4.ui.categoria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication4.Clases.Categoria
import kotlinx.coroutines.launch

//class CategoriaViewModel : ViewModel() {
//
//    // Replace this with your actual data source (e.g., Room database or remote API)
//    private val _categories = MutableLiveData<List<Categoria>>()
//    val categories: LiveData<List<Categoria>> = _categories
//
//    // Sample Categoria data class (modify according to your needs)
//    data class Categoria(val name: String, val color: String)
//
//    // Function to update categories list (replace with your logic)
//    fun updateCategories(newCategories: List<Categoria>) {
//        _categories.value = newCategories
//    }
//}


//class CategoriaViewModel : ViewModel() {
//    private val _categories = MutableLiveData<List<Categoria>>()
//    val categories: LiveData<List<Categoria>> = _categories
//
//    init {
//        loadInitialCategories()
//    }
//
//    private fun loadInitialCategories() {
//        val initialCategories = listOf(
//            Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
//            Categoria(2, "Trasporte", "", "#FFEB3c", "Gasto"),
//            Categoria(3, "Ocio", "", "#800080", "Gasto"),
//            Categoria(4,"Salud", "", "#FF0000","Gasto"),
//            Categoria(5,"Casa", "", "#287FD2","Gasto"),
//            Categoria(6,"Educación","", "#FF00FF","Gasto"),
//            Categoria(7,"Regalos","", "#00FFFF","Gasto"),
//            Categoria(8, "Salario", "", "#808080", "Ingreso"),
//            Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
//        )
//        _categories.value = initialCategories
//    }
//
//    fun updateCategories(newCategories: List<Categoria>) {
//        _categories.value = newCategories
//    }
//
//    // Agregar funciones para manejar la lógica de negocio,
//    // como agregar, editar o eliminar categorías
//}


//Cambio para manejar en listas separadas las categorías de gastos e ingresos
class CategoriaViewModel : ViewModel() {
    private val _expenseCategories = MutableLiveData<List<Categoria>>()
    val expenseCategories: LiveData<List<Categoria>> = _expenseCategories

    private val _incomeCategories = MutableLiveData<List<Categoria>>()
    val incomeCategories: LiveData<List<Categoria>> = _incomeCategories

    private val _currentCategories = MutableLiveData<List<Categoria>>()
    val currentCategories: LiveData<List<Categoria>> = _currentCategories

    init {
        loadInitialCategories()
    }

    private fun loadInitialCategories() {
        val initialExpenseCategories = listOf(
            Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
            Categoria(2, "Trasporte", "", "#FFEB3C", "Gasto"),
            Categoria(3, "Ocio", "", "#800080", "Gasto"),
            Categoria(4,"Salud", "", "#FF0000","Gasto"),
            Categoria(5,"Casa", "", "#287FD2","Gasto"),
            Categoria(6,"Educación","", "#FF00FF","Gasto"),
            Categoria(7,"Regalos","", "#00FFFF","Gasto")
        )
        _expenseCategories.value = initialExpenseCategories

        val initialIncomeCategories = listOf(
            Categoria(8, "Salario", "", "#808080", "Ingreso"),
            Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
        )
        _incomeCategories.value = initialIncomeCategories

        // Inicialmente mostramos las categorías de gasto
        _currentCategories.value = initialExpenseCategories
    }

    fun showExpenseCategories() {
        _currentCategories.value = _expenseCategories.value
    }

    fun showIncomeCategories() {
        _currentCategories.value = _incomeCategories.value
    }

    fun addCategory(newCategory: Categoria) {
        val updatedCategories = currentCategories.value?.toMutableList() ?: mutableListOf()
        updatedCategories.add(newCategory)
        _currentCategories.value = updatedCategories
        // Agregar la lógica para persistir la categoría en una base de datos
    }

    fun updateCategory(updatedCategory: Categoria) {
        viewModelScope.launch {
            val currentCategories = _currentCategories.value?.toMutableList() ?: mutableListOf()
            val index = currentCategories.indexOfFirst { it.id == updatedCategory.id }
            if (index != -1) {
                currentCategories[index] = updatedCategory
                _currentCategories.value = currentCategories

                // Actualizar también la lista específica
                if (updatedCategory.tipo == "Gasto") {
                    _expenseCategories.value = _expenseCategories.value?.map {
                        if (it.id == updatedCategory.id) updatedCategory else it
                    }
                } else {
                    _incomeCategories.value = _incomeCategories.value?.map {
                        if (it.id == updatedCategory.id) updatedCategory else it
                    }
                }
            }
        }
    }
}