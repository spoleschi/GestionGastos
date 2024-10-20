package com.example.myapplication4.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transaccion",
    foreignKeys = [ForeignKey(
        entity = Categoria::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoriaId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val desc: String,
    val monto: Float,
    val fecha: Date,
    val categoriaId: Int,

    val cantCuotas: Int,
    val interes: Float,
    val tipo: String
)

