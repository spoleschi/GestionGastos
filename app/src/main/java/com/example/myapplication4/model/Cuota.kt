package com.example.myapplication4.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "cuota",
    foreignKeys = [ForeignKey(
        entity = Transaccion::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("transaccionId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cuota(
    @PrimaryKey(autoGenerate = true)
    val nroCuota: Int,
    val fechaPago: Date,
    val transaccionId: Int
)

