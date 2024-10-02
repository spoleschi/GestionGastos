package com.example.myapplication4.Clases
import java.time.LocalDate

// Clase Padre
open class Transaccion(
    val id: Int,
    val desc: String,
    val monto: Float,
    val fecha: LocalDate,
    val categoria: Categoria,
//    val usuario: Usuario

//    val cantCuotas: Int = 1,
//    val interes: Float
)

