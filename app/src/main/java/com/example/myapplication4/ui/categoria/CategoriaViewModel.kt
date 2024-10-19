package com.example.myapplication4.ui.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _currentCategories = MutableStateFlow<List<Categoria>>(emptyList())
    val currentCategories = _currentCategories.asStateFlow()

    private var currentType: String = "Gasto"

    init {
        viewModelScope.launch {
            repository.loadInitialCategories()
        }
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            when (currentType) {
                "Gasto" -> repository.expenseCategories.collect {
                    _currentCategories.value = it
                }
                "Ingreso" -> repository.incomeCategories.collect {
                    _currentCategories.value = it
                }
            }
        }
    }

    fun showExpenseCategories() {
        currentType = "Gasto"
        observeCategories()
    }

    fun showIncomeCategories() {
        currentType = "Ingreso"
        observeCategories()
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

    suspend fun getCategoryById(id: Int): Categoria? = repository.findCategoryById(id)

    suspend fun getAllCategories(): List<Categoria> = repository.getAllCategories()

    class Factory(
        private val repository: CategoryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoriaViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}