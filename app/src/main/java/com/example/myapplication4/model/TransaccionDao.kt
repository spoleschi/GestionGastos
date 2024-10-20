package com.example.myapplication4.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaccion(transaccion: Transaccion)

    // Actualizar una transacción
    @Update
    suspend fun updateTransaccion(transaccion: Transaccion)

    @Delete
    suspend fun deleteTransaccion(transaccion: Transaccion)


    @Query("SELECT * FROM transaccion")
    suspend fun getAllTransacciones(): Flow<List<Categoria>>

    @Query("SELECT * FROM transaccion WHERE id = :transaccionId")
    suspend fun getTransaccionById(transaccionId: Int): Transaccion
}
