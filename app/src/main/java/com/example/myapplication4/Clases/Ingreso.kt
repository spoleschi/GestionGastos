package com.example.myapplication4.Clases
import java.time.LocalDate

// Clase hija: Ingreso
data class Ingreso(
    val idIngreso: Int,
    val descIngreso: String,
    val montoIngreso: Float,
    val fechaIngreso: LocalDate,
    val categoriaIngreso: Categoria
) : Transaccion(idIngreso, descIngreso, montoIngreso, fechaIngreso, categoriaIngreso)