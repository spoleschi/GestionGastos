package com.example.myapplication4.model

import androidx.room.*

@Dao
interface CuotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuota(cuota: Cuota)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuotas(cuotas: List<Cuota>)

    @Update
    suspend fun updateCuota(cuota: Cuota)

    @Delete
    suspend fun deleteCuota(cuota: Cuota)

    @Query("SELECT * FROM cuota WHERE transaccionId = :transaccionId")
    suspend fun getCuotasByTransaccionId(transaccionId: Int): List<Cuota>

    @Query("SELECT * FROM cuota")
    suspend fun getAllCuotas(): List<Cuota>
}
