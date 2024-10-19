package com.example.myapplication4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.model.CategoriaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class CategoryRepository(private val categoriaDao: CategoriaDao) {
    val expenseCategories: Flow<List<Categoria>> = categoriaDao.getCategoriasByTipo("Gasto")
    val incomeCategories: Flow<List<Categoria>> = categoriaDao.getCategoriasByTipo("Ingreso")

    suspend fun loadInitialCategories() {
        val count = categoriaDao.getAllCategorias().first().size
        if (count == 0) {
            val initialExpenseCategories = listOf(
                Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
                Categoria(2, "Trasporte", "", "#FFEB3C", "Gasto"),
                Categoria(3, "Ocio", "", "#800080", "Gasto"),
                Categoria(4, "Salud", "", "#FF0000", "Gasto"),
                Categoria(5, "Casa", "", "#287FD2", "Gasto"),
                Categoria(6, "Educación", "", "#FF00FF", "Gasto"),
                Categoria(7, "Regalos", "", "#00FFFF", "Gasto")
            )

            val initialIncomeCategories = listOf(
                Categoria(8, "Salario", "", "#808080", "Ingreso"),
                Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
            )

            categoriaDao.insertAll(initialExpenseCategories + initialIncomeCategories)
        }
    }

    suspend fun addCategory(newCategory: Categoria) {
        categoriaDao.insertCategoria(newCategory)
    }

    suspend fun updateCategory(updatedCategory: Categoria) {
        categoriaDao.updateCategoria(updatedCategory)
    }

    suspend fun deleteCategory(categoria: Categoria) {
        categoriaDao.deleteCategoria(categoria)
    }


    suspend fun findCategoryById(id: Int): Categoria? {
        return categoriaDao.getCategoriaById(id)
    }

    suspend fun getAllCategories(): List<Categoria> {
        return categoriaDao.getAllCategorias().first()
    }
}