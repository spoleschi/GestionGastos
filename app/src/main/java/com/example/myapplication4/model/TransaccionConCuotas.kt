package com.example.myapplication4.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransaccionConCuotas(
    @Embedded val transaccion: Transaccion,
    @Relation(
        parentColumn = "id",
        entityColumn = "transaccionId"
    )
    val cuotas: List<Cuota> // Lista de cuotas asociadas a la transacción
)

