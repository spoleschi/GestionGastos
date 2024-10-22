package com.example.myapplication4.repository

import com.example.myapplication4.model.CategoriaDao
import com.example.myapplication4.model.CategoriaEntity
import com.example.myapplication4.model.toDomain
import com.example.myapplication4.model.toEntity
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.repository.CategoryRepository
import com.example.myapplication4.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class CategoryRepositoryImpl(private val categoriaDao: CategoriaDao) : CategoryRepository {
    override val expenseCategories: Flow<List<Categoria>> =
        categoriaDao.getCategoriasByTipo("Gasto")
            .map { entities -> entities.map { it.toDomain() } }

    override val incomeCategories: Flow<List<Categoria>> =
        categoriaDao.getCategoriasByTipo("Ingreso")
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun addCategory(categoria: Categoria) {
        categoriaDao.insertCategoria(categoria.toEntity())
    }

    override suspend fun updateCategory(categoria: Categoria) {
        categoriaDao.updateCategoria(categoria.toEntity())
    }

    override suspend fun deleteCategory(categoria: Categoria) {
        categoriaDao.deleteCategoria(categoria.toEntity())
    }

    override suspend fun findCategoryById(id: Int): Categoria? {
        return categoriaDao.getCategoriaById(id)?.toDomain()
    }

    override fun getAllCategories(): Flow<List<Categoria>> =
        categoriaDao.getAllCategorias()
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun initializeDefaultCategories() {
        // Verificar si ya existen categorías
        val currentCategories = categoriaDao.getAllCategorias().first()

        if (currentCategories.isEmpty()) {
            // Lista de categorías de gastos iniciales
            val initialExpenseCategories = listOf(
                Categoria(1, "Alimentos", "", "#3EC54A", "Gasto"),
                Categoria(2, "Trasporte", "", "#FFEB3C", "Gasto"),
                Categoria(3, "Ocio", "", "#800080", "Gasto"),
                Categoria(4, "Salud", "", "#FF0000", "Gasto"),
                Categoria(5, "Casa", "", "#287FD2", "Gasto"),
                Categoria(6, "Educación", "", "#FF00FF", "Gasto"),
                Categoria(7, "Regalos", "", "#00FFFF", "Gasto")
            )

            // Lista de categorías de ingresos iniciales
            val initialIncomeCategories = listOf(
                Categoria(8, "Salario", "", "#808080", "Ingreso"),
                Categoria(9, "Inversiones", "", "#FF9300", "Ingreso")
            )

            // Insertar todas las categorías iniciales
            (initialExpenseCategories + initialIncomeCategories).forEach { categoria ->
                addCategory(categoria)
            }
        }
    }
}