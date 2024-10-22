package com.example.myapplication4.model

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias WHERE tipo = :tipo")
    fun getCategoriasByTipo(tipo: String): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getCategoriaById(id: Int): CategoriaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: CategoriaEntity)

    @Update
    suspend fun updateCategoria(categoria: CategoriaEntity)

    @Delete
    suspend fun deleteCategoria(categoria: CategoriaEntity)

    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>
}