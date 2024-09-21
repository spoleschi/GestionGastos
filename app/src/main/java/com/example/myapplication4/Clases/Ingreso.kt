package com.example.myapplication4.Clases
import java.time.LocalDate

data class Ingreso(
    val id: Int,
    val desc: String,
    val monto: Float,
    val fecha: LocalDate,
    val categoria: Categoria
)
