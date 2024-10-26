package com.example.myapplication4.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transacciones WHERE tipo = :tipo")
    fun getTransactionsByTipo(tipo: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transacciones WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuota(cuota: CuotaEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TransactionEntity>>

    // Operaciones de Cuotas
    @Query("SELECT * FROM cuotas WHERE transaccionId = :transactionId")
    suspend fun getCuotasByTransactionId(transactionId: Int): List<CuotaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuotas(cuotas: List<CuotaEntity>)

    @Update
    suspend fun updateCuota(cuota: CuotaEntity)

    @Delete
    suspend fun deleteCuota(cuota: CuotaEntity)

    @Query("DELETE FROM cuotas WHERE transaccionId = :transactionId")
    suspend fun deleteCuotasByTransactionId(transactionId: Int)

    // Operación de transacción con sus cuotas (útil para borrar todo junto)
    @Transaction
    suspend fun deleteTransactionWithCuotas(transactionId: Int) {
        deleteCuotasByTransactionId(transactionId)
        // Asumiendo que tienes el TransactionEntity
        getTransactionById(transactionId)?.let { deleteTransaction(it) }
    }
}