package com.example.myapplication4.model

import androidx.room.*
import java.time.LocalDate

@Entity(
    tableName = "transacciones",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: Int,
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Float,
    val fecha: LocalDate,
    val categoriaId: Int,
    val tipo: String, // "INGRESO" o "GASTO"
    // Campos específicos para gastos
    val cantCuotas: Int? = null,
    val interes: Float? = null
)
