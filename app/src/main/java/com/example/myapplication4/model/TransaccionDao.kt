package com.example.myapplication4.model

import androidx.room.*

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
    suspend fun getAllTransacciones(): List<Transaccion>

    @Query("SELECT * FROM transaccion WHERE id = :transaccionId")
    suspend fun getTransaccionById(transaccionId: Int): Transaccion?

    @Transaction
    @Query("SELECT * FROM transaccion WHERE id = :transaccionId")
    suspend fun getTransaccionConCuotas(transaccionId: Int): TransaccionConCuotas
}
