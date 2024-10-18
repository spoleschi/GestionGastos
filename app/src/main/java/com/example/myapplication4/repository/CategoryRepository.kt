package com.example.myapplication4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication4.clases.Categoria

class CategoryRepository {
    private val _expenseCategories = MutableLiveData<List<Categoria>>()
    val expenseCategories: LiveData<List<Categoria>> = _expenseCategories

    private val _incomeCategories = MutableLiveData<List<Categoria>>()
    val incomeCategories: LiveData<List<Categoria>> = _incomeCategories

    init {
        loadInitialCategories()
    }

    private fun loadInitialCategories() {
        val initialExpenseCategories = listOf(
            Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
            Categoria(2, "Trasporte", "", "#FFEB3C", "Gasto"),
            Categoria(3, "Ocio", "", "#800080", "Gasto"),
            Categoria(4, "Salud", "", "#FF0000", "Gasto"),
            Categoria(5, "Casa", "", "#287FD2", "Gasto"),
            Categoria(6, "Educación", "", "#FF00FF", "Gasto"),
            Categoria(7, "Regalos", "", "#00FFFF", "Gasto")
        )
        _expenseCategories.value = initialExpenseCategories

        val initialIncomeCategories = listOf(
            Categoria(8, "Salario", "", "#808080", "Ingreso"),
            Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
        )
        _incomeCategories.value = initialIncomeCategories
    }

    fun addCategory(newCategory: Categoria) {
        val updatedCategories = when (newCategory.tipo) {
            "Gasto" -> (_expenseCategories.value ?: emptyList()) + newCategory
            "Ingreso" -> (_incomeCategories.value ?: emptyList()) + newCategory
            else -> return // Ignore invalid types
        }

        updateCategoryList(newCategory.tipo, updatedCategories)
    }

    fun updateCategory(updatedCategory: Categoria) {
        val oldCategory = findCategoryById(updatedCategory.id)

        if (oldCategory != null && oldCategory.tipo != updatedCategory.tipo) {
            // Category type changed, remove from old list and add to new list
            removeCategoryFromList(oldCategory)
            addCategory(updatedCategory)
        } else {
            // Update category in its current list
            val updatedList = when (updatedCategory.tipo) {
                "Gasto" -> _expenseCategories.value?.map { if (it.id == updatedCategory.id) updatedCategory else it }
                "Ingreso" -> _incomeCategories.value?.map { if (it.id == updatedCategory.id) updatedCategory else it }
                else -> return // Ignore invalid types
            }
            updateCategoryList(updatedCategory.tipo, updatedList ?: emptyList())
        }
    }

    fun findCategoryById(id: Int): Categoria? {
        return _expenseCategories.value?.find { it.id == id }
            ?: _incomeCategories.value?.find { it.id == id }
    }

    private fun removeCategoryFromList(category: Categoria) {
        val updatedList = when (category.tipo) {
            "Gasto" -> _expenseCategories.value?.filter { it.id != category.id }
            "Ingreso" -> _incomeCategories.value?.filter { it.id != category.id }
            else -> return // Ignore invalid types
        }
        updateCategoryList(category.tipo, updatedList ?: emptyList())
    }

    private fun updateCategoryList(type: String, updatedList: List<Categoria>) {
        when (type) {
            "Gasto" -> _expenseCategories.value = updatedList
            "Ingreso" -> _incomeCategories.value = updatedList
        }
    }

    fun getAllCategories(): List<Categoria> {
        return (_expenseCategories.value ?: emptyList()) + (_incomeCategories.value ?: emptyList())
    }
    fun getExpenseCategories(): List<Categoria> {
        return _expenseCategories.value ?: emptyList()
    }

    fun getIncomeCategories(): List<Categoria> {
        return _incomeCategories.value ?: emptyList()
    }

}