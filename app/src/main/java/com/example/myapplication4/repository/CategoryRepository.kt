package com.example.myapplication4.repository

import com.example.myapplication4.clases.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val expenseCategories: Flow<List<Categoria>>
    val incomeCategories: Flow<List<Categoria>>

    suspend fun addCategory(categoria: Categoria)
    suspend fun updateCategory(categoria: Categoria)
    suspend fun deleteCategory(categoria: Categoria)
    suspend fun findCategoryById(id: Int): Categoria?
    fun getAllCategories(): Flow<List<Categoria>>
    suspend fun initializeDefaultCategories()
}