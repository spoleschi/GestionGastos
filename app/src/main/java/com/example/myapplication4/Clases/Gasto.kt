package com.example.myapplication4.Clases
import java.time.LocalDate

// Clase hija: Gasto
data class Gasto(
    val cantCuotas: Int,
    val interes: Float,
    val cuotas: ArrayList<Cuota> = arrayListOf(),
// Propiedades heredadas de Transaccion
    val idGasto: Int,
    val descGasto: String,
    val montoGasto: Float,
    val fechaGasto: LocalDate,
    val categoriaGasto: Categoria
) : Transaccion(idGasto, descGasto, montoGasto, fechaGasto, categoriaGasto) {
    init {
        require(cantCuotas > 0) { "El número de cuotas debe ser mayor que 0" }
        val fechaPagar = LocalDate.now()
        for (i in 0 until cantCuotas) {
            val cuota = Cuota(i + 1, fechaPagar.plusMonths(i.toLong()))
            cuotas.add(cuota)
        }
    }
}
