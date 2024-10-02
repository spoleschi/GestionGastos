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


//Cambio para manejar en listas separadas las categorías de gastos e ingresos
class CategoriaViewModel : ViewModel() {
    private val _expenseCategories = MutableLiveData<List<Categoria>>()
    val expenseCategories: LiveData<List<Categoria>> = _expenseCategories

    private val _incomeCategories = MutableLiveData<List<Categoria>>()
    val incomeCategories: LiveData<List<Categoria>> = _incomeCategories

    private val _currentCategories = MutableLiveData<List<Categoria>>()
    val currentCategories: LiveData<List<Categoria>> = _currentCategories

    private var currentType: String = "Gasto"

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
        currentType = "Gasto"
        _currentCategories.value = _expenseCategories.value
    }

    fun showIncomeCategories() {
        currentType = "Ingreso"
        _currentCategories.value = _incomeCategories.value
    }

    fun addCategory(newCategory: Categoria) {
        when (newCategory.tipo) {
            "Gasto" -> {
                val updatedCategories = _expenseCategories.value?.toMutableList() ?: mutableListOf()
                updatedCategories.add(newCategory)
                _expenseCategories.value = updatedCategories
            }
            "Ingreso" -> {
                val updatedCategories = _incomeCategories.value?.toMutableList() ?: mutableListOf()
                updatedCategories.add(newCategory)
                _incomeCategories.value = updatedCategories
            }
        }
        updateCurrentCategories()
    }

    fun updateCategory(updatedCategory: Categoria) {
        val oldCategory = findCategoryById(updatedCategory.id)

        if (oldCategory != null && oldCategory.tipo != updatedCategory.tipo) {
            // La categoría cambió de tipo, hay que moverla
            removeCategoryFromList(oldCategory)
            addCategory(updatedCategory)
        } else {
            // La categoría no cambió de tipo, solo actualizamos sus datos
            val listToUpdate = if (updatedCategory.tipo == "Gasto") _expenseCategories else _incomeCategories
            val updatedList = listToUpdate.value?.map {
                if (it.id == updatedCategory.id) updatedCategory else it
            }
            listToUpdate.value = updatedList
        }

        updateCurrentCategories()
    }

    private fun findCategoryById(id: Int): Categoria? {
        return _expenseCategories.value?.find { it.id == id }
            ?: _incomeCategories.value?.find { it.id == id }
    }

    private fun removeCategoryFromList(category: Categoria) {
        when (category.tipo) {
            "Gasto" -> {
                val updatedList = _expenseCategories.value?.filter { it.id != category.id }
                _expenseCategories.value = updatedList
            }
            "Ingreso" -> {
                val updatedList = _incomeCategories.value?.filter { it.id != category.id }
                _incomeCategories.value = updatedList
            }
        }
    }

    private fun updateCurrentCategories() {
        _currentCategories.value = if (currentType == "Gasto") _expenseCategories.value else _incomeCategories.value
    }
}