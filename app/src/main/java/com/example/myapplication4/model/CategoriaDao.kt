package com.example.myapplication4.model
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias WHERE tipo = :tipo")
    fun getCategoriasByTipo(tipo: String): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): Flow<List<Categoria>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria)

    @Update
    suspend fun updateCategoria(categoria: Categoria)

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getCategoriaById(id: Int): Categoria?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categorias: List<Categoria>)
}