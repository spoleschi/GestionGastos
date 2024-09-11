package com.example.myapplication4.ui.transaccion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.Clases.Gasto
import java.time.LocalDate

class TransaccionViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Transaction Fragment"
    }
    val text: LiveData<String> = _text

    // Lista de gastos
    private val _gastos = MutableLiveData<List<Gasto>>(emptyList())
    private var id = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun agregarGasto(cantCuotas: Int, interes: Float, desc: String, monto: Float, fecha: LocalDate, categoria: Categoria) {
        if (cantCuotas < 1 || cantCuotas > 24) {
            throw IllegalArgumentException("La cantidad de cuotas permitidas es: minimo 1, maximo 24")
        }
        val newGasto = Gasto(
            cantCuotas = cantCuotas,
            interes = interes,
            id = id++,
            desc = desc,
            monto = monto,
            fecha = fecha,
            categoria = categoria
        )
        val updatedExpenses = _gastos.value.orEmpty() + newGasto
        _gastos.value = updatedExpenses
    }
}