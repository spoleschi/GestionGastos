package com.example.myapplication4.repository

import com.example.myapplication4.clases.*
import com.example.myapplication4.model.TransactionDao
import com.example.myapplication4.model.toDomain
import com.example.myapplication4.model.toEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.time.LocalDate
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

interface TransactionRepository {
    val expenses: Flow<List<Transaccion>>
    val incomes: Flow<List<Transaccion>>

    suspend fun addTransaction(transaction: Transaccion)
    suspend fun updateTransaction(transaction: Transaccion)
    suspend fun deleteTransaction(transaction: Transaccion)
    suspend fun getTransactionById(id: Int): Transaccion?
    fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaccion>>
}

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryRepository: CategoryRepository
) : TransactionRepository {

    override val expenses: Flow<List<Transaccion>> =
        transactionDao.getTransactionsByTipo("GASTO")
            .transform { entities ->
                coroutineScope {
                    val transactions = entities.map { entity ->
                        async {
                            val categoria = categoryRepository.findCategoryById(entity.categoriaId)
                                ?: throw IllegalStateException("Categoría no encontrada para id: ${entity.categoriaId}")
                            val cuotas = transactionDao.getCuotasByTransactionId(entity.id).map { it.toDomain() }
                            entity.toDomain(categoria, cuotas)
                        }
                    }
                    emit(transactions.awaitAll())
                }
            }

    override val incomes: Flow<List<Transaccion>> =
        transactionDao.getTransactionsByTipo("INGRESO")
            .transform { entities ->
                coroutineScope {
                    val transactions = entities.map { entity ->
                        async {
                            val categoria = categoryRepository.findCategoryById(entity.categoriaId)
                                ?: throw IllegalStateException("Categoría no encontrada para id: ${entity.categoriaId}")
                            entity.toDomain(categoria)
                        }
                    }
                    emit(transactions.awaitAll())
                }
            }

    override suspend fun addTransaction(transaction: Transaccion) {
        val entity = transaction.toEntity()
        transactionDao.insertTransaction(entity)

        if (transaction is Gasto) {
            transaction.cuotas.forEach { cuota ->
                transactionDao.insertCuota(cuota.toEntity(transaction.id))
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaccion) {
        val entity = transaction.toEntity()
        transactionDao.updateTransaction(entity)

        if (transaction is Gasto) {
            // Primero eliminamos las cuotas existentes y luego insertamos las nuevas
            transactionDao.deleteCuotasByTransactionId(transaction.id)
            transaction.cuotas.forEach { cuota ->
                transactionDao.insertCuota(cuota.toEntity(transaction.id))
            }
        }
    }

    override suspend fun deleteTransaction(transaction: Transaccion) {
        if (transaction is Gasto) {
            // Primero eliminamos las cuotas asociadas
            transactionDao.deleteCuotasByTransactionId(transaction.id)
        }
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    override suspend fun getTransactionById(id: Int): Transaccion? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        val categoria = categoryRepository.findCategoryById(entity.categoriaId)
            ?: throw IllegalStateException("Categoría no encontrada para id: ${entity.categoriaId}")
        val cuotas = if (entity.tipo == "GASTO") {
            transactionDao.getCuotasByTransactionId(entity.id).map { it.toDomain() }
        } else {
            emptyList()
        }
        return entity.toDomain(categoria, cuotas)
    }

    override fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaccion>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)
            .transform { entities ->
                coroutineScope {
                    val transactions = entities.map { entity ->
                        async {
                            val categoria = categoryRepository.findCategoryById(entity.categoriaId)
                                ?: throw IllegalStateException("Categoría no encontrada para id: ${entity.categoriaId}")
                            val cuotas = if (entity.tipo == "GASTO") {
                                transactionDao.getCuotasByTransactionId(entity.id).map { it.toDomain() }
                            } else {
                                emptyList()
                            }
                            entity.toDomain(categoria, cuotas)
                        }
                    }
                    emit(transactions.awaitAll())
                }
            }
}