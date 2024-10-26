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
        viewModelScope.launch {
            repository.initializeDefaultCategories()
            showExpenseCategories()
        }
    }

    fun getCurrentType(): String = currentType

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