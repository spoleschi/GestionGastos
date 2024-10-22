//package com.example.myapplication4.ui.categoria
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.myapplication4.Clases.Categoria
//import com.example.myapplication4.repository.CategoryRepository
//import com.example.myapplication4.repository.TransactionRepository
//import com.example.myapplication4.ui.transaccion.TransactionViewModel
//
////Cambio para manejar en listas separadas las categorías de gastos e ingresos
//class CategoriaViewModel(private val repository: CategoryRepository) : ViewModel() {
//    private val _expenseCategories = MutableLiveData<List<Categoria>>()
//    val expenseCategories: LiveData<List<Categoria>> = _expenseCategories
//
//    private val _incomeCategories = MutableLiveData<List<Categoria>>()
//    val incomeCategories: LiveData<List<Categoria>> = _incomeCategories
//
//    private val _currentCategories = MutableLiveData<List<Categoria>>()
//    val currentCategories: LiveData<List<Categoria>> = _currentCategories
//
//    private var currentType: String = "Gasto"
//
//    init {
//        loadInitialCategories()
//    }
//
//    private fun loadInitialCategories() {
//        val initialExpenseCategories = listOf(
//            Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
//            Categoria(2, "Trasporte", "", "#FFEB3C", "Gasto"),
//            Categoria(3, "Ocio", "", "#800080", "Gasto"),
//            Categoria(4, "Salud", "", "#FF0000", "Gasto"),
//            Categoria(5, "Casa", "", "#287FD2", "Gasto"),
//            Categoria(6, "Educación", "", "#FF00FF", "Gasto"),
//            Categoria(7, "Regalos", "", "#00FFFF", "Gasto")
//        )
//        _expenseCategories.value = initialExpenseCategories
//
//        val initialIncomeCategories = listOf(
//            Categoria(8, "Salario", "", "#808080", "Ingreso"),
//            Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
//        )
//        _incomeCategories.value = initialIncomeCategories
//
//        // Inicialmente mostramos las categorías de gasto
//        _currentCategories.value = initialExpenseCategories
//    }
//
//    fun showExpenseCategories() {
//        currentType = "Gasto"
//        _currentCategories.value = _expenseCategories.value
//    }
//
//    fun showIncomeCategories() {
//        currentType = "Ingreso"
//        _currentCategories.value = _incomeCategories.value
//    }
//
////    fun addCategory(newCategory: Categoria) {
////        when (newCategory.tipo) {
////            "Gasto" -> {
////                val updatedCategories = _expenseCategories.value?.toMutableList() ?: mutableListOf()
////                updatedCategories.add(newCategory)
////                _expenseCategories.value = updatedCategories
////            }
////            "Ingreso" -> {
////                val updatedCategories = _incomeCategories.value?.toMutableList() ?: mutableListOf()
////                updatedCategories.add(newCategory)
////                _incomeCategories.value = updatedCategories
////            }
////        }
////        updateCurrentCategories()
////    }
////
////    fun updateCategory(updatedCategory: Categoria) {
////        val oldCategory = findCategoryById(updatedCategory.id)
////
////        if (oldCategory != null && oldCategory.tipo != updatedCategory.tipo) {
////            // La categoría cambió de tipo, hay que moverla
////            removeCategoryFromList(oldCategory)
////            addCategory(updatedCategory)
////        } else {
////            // La categoría no cambió de tipo, solo actualizamos sus datos
////            val listToUpdate = if (updatedCategory.tipo == "Gasto") _expenseCategories else _incomeCategories
////            val updatedList = listToUpdate.value?.map {
////                if (it.id == updatedCategory.id) updatedCategory else it
////            }
////            listToUpdate.value = updatedList
////        }
////
////        updateCurrentCategories()
////    }
////
////    private fun findCategoryById(id: Int): Categoria? {
////        return _expenseCategories.value?.find { it.id == id }
////            ?: _incomeCategories.value?.find { it.id == id }
////    }
////
////    private fun removeCategoryFromList(category: Categoria) {
////        when (category.tipo) {
////            "Gasto" -> {
////                val updatedList = _expenseCategories.value?.filter { it.id != category.id }
////                _expenseCategories.value = updatedList
////            }
////            "Ingreso" -> {
////                val updatedList = _incomeCategories.value?.filter { it.id != category.id }
////                _incomeCategories.value = updatedList
////            }
////        }
////    }
////
////    private fun updateCurrentCategories() {
////        _currentCategories.value = if (currentType == "Gasto") _expenseCategories.value else _incomeCategories.value
////    }
//
//    fun addCategory(newCategory: Categoria) {
//        val updatedCategories = when (newCategory.tipo) {
//            "Gasto" -> (_expenseCategories.value ?: emptyList()) + newCategory
//            "Ingreso" -> (_incomeCategories.value ?: emptyList()) + newCategory
//            else -> return // Ignore invalid types
//        }
//
//        updateCategoryList(newCategory.tipo, updatedCategories)
//    }
//
//    fun updateCategory(updatedCategory: Categoria) {
//        val oldCategory = findCategoryById(updatedCategory.id)
//
//        if (oldCategory != null && oldCategory.tipo != updatedCategory.tipo) {
//            // Category type changed, remove from old list and add to new list
//            removeCategoryFromList(oldCategory)
//            addCategory(updatedCategory)
//        } else {
//            // Update category in its current list
//            val updatedList = when (updatedCategory.tipo) {
//                "Gasto" -> _expenseCategories.value?.map { if (it.id == updatedCategory.id) updatedCategory else it }
//                "Ingreso" -> _incomeCategories.value?.map { if (it.id == updatedCategory.id) updatedCategory else it }
//                else -> return // Ignore invalid types
//            }
//            updateCategoryList(updatedCategory.tipo, updatedList ?: emptyList())
//        }
//    }
//
//    private fun findCategoryById(id: Int): Categoria? {
//        return _expenseCategories.value?.find { it.id == id }
//            ?: _incomeCategories.value?.find { it.id == id }
//    }
//
//    private fun removeCategoryFromList(category: Categoria) {
//        val updatedList = when (category.tipo) {
//            "Gasto" -> _expenseCategories.value?.filter { it.id != category.id }
//            "Ingreso" -> _incomeCategories.value?.filter { it.id != category.id }
//            else -> return // Ignore invalid types
//        }
//        updateCategoryList(category.tipo, updatedList ?: emptyList())
//    }
//
//    private fun updateCategoryList(type: String, updatedList: List<Categoria>) {
//        when (type) {
//            "Gasto" -> _expenseCategories.value = updatedList
//            "Ingreso" -> _incomeCategories.value = updatedList
//        }
//        updateCurrentCategories()
//    }
//
//    private fun updateCurrentCategories() {
//        _currentCategories.value =
//            if (currentType == "Gasto") _expenseCategories.value else _incomeCategories.value
//    }
//
//    // New convenience function to get a category by ID
//    fun getCategoryById(id: Int): Categoria? = findCategoryById(id)
//
//    // New function to get all categories (both expense and income)
//    fun getAllCategories(): List<Categoria> {
//        return (_expenseCategories.value ?: emptyList()) + (_incomeCategories.value ?: emptyList())
//    }
//}

package com.example.myapplication4.ui.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: CategoryRepository) : ViewModel() {
    private val _currentCategories = MutableStateFlow<List<Categoria>>(emptyList())
    val currentCategories: StateFlow<List<Categoria>> = _currentCategories.asStateFlow()

    private var currentType: String = "Gasto"

    init {
//        showExpenseCategories()
        viewModelScope.launch {
            repository.initializeDefaultCategories() // Inicializo categorías por defecto
            showExpenseCategories() // Mostrar categorías de gastos después de la inicialización
        }
    }

    fun showExpenseCategories() {
        currentType = "Gasto"
        viewModelScope.launch {
            repository.expenseCategories.collect { categories ->
                _currentCategories.value = categories
            }
        }
    }

    fun showIncomeCategories() {
        currentType = "Ingreso"
        viewModelScope.launch {
            repository.incomeCategories.collect { categories ->
                _currentCategories.value = categories
            }
        }
    }

    fun addCategory(newCategory: Categoria) {
        viewModelScope.launch {
            repository.addCategory(newCategory)
        }
    }

    fun updateCategory(updatedCategory: Categoria) {
        viewModelScope.launch {
            repository.updateCategory(updatedCategory)
        }
    }

    fun deleteCategory(categoria: Categoria) {
        viewModelScope.launch {
            repository.deleteCategory(categoria)
        }
    }

    suspend fun getCategoryById(id: Int): Categoria? {
        return repository.findCategoryById(id)
    }

    fun getAllCategories(): Flow<List<Categoria>> {
        return repository.getAllCategories()
    }

    class Factory(private val repository: CategoryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoriaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}