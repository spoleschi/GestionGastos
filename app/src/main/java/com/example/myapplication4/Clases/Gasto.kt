package com.example.myapplication4.Clases
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
@RequiresApi(Build.VERSION_CODES.O)

data class Gasto(
    val cantCuotas: Int,
    val interes: Float,
    val cuotas: ArrayList<Cuota> = arrayListOf(),
    val id: Int,
    val desc: String,
    val monto: Float,
    val fecha: LocalDate,
    val categoria: Categoria
) {
    init {
        val fechaPagar = LocalDate.now()
        for (i in 0 until cantCuotas) {
            val cuota = Cuota(i + 1, fechaPagar.plusMonths(i.toLong()))
            cuotas.add(cuota)
        }
    }
}
