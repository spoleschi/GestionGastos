package com.example.myapplication4.Clases
import java.time.LocalDate

data class Transaccion(
    val id: Int,
    val desc: String,
    val monto: Float,
    val fecha: LocalDate,
    val categoria: Categoria?,
    val cantCuotas: Int = 1,
    val interes: Float
)
