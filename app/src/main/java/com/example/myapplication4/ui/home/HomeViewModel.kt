package com.example.myapplication4.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.Clases.Gasto
import java.time.LocalDate

//class HomeViewModel : ViewModel() {
//
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is Home Fragment"
//    }
//    val text: LiveData<String> = _text
//}


class HomeViewModel : ViewModel() {
    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>> = _gastos

    private val _total = MutableLiveData<Float>()
    val total: LiveData<Float> = _total

    init {
        cargarGastos()
    }

    private fun cargarGastos() {
        // Aquí es donde normalmente cargarías los gastos de una base de datos o API
        // Por ahora, usaremos datos de ejemplo
        val listaGastos = listOf(
            Gasto(1, "Compra de televisor", 1500.0f, LocalDate.of(2023, 5, 15),
                Categoria(1, "Electrónica", "Gastos en aparatos electrónicos", "#FF9800", "Variable"), 12, 0.15f),
            Gasto(2, "Factura de luz", 150.0f, LocalDate.of(2023, 6, 1),
                Categoria(2, "Servicios", "Gastos en servicios básicos", "#4CAF50", "Fijo"), 1, 0.0f),
            Gasto(3, "Compra de ropa", 300.0f, LocalDate.of(2023, 7, 20),
                Categoria(3, "Ropa", "Gastos en vestimenta", "#E91E63", "Variable"), 3, 0.10f),
            Gasto(4, "Cena en restaurante", 80.0f, LocalDate.of(2023, 8, 12),
                Categoria(4, "Alimentación", "Gastos en comida", "#FFC107", "Variable"), 1, 0.0f),
            Gasto(5, "Visita al dentista", 150.0f, LocalDate.of(2023, 9, 3),
                Categoria(5, "Salud", "Gastos médicos", "#2196F3", "Variable"), 2, 0.05f)
        )

        _gastos.value = listaGastos
        calcularTotal()
    }

    private fun calcularTotal() {
        val totalGastos = _gastos.value?.sumOf { it.monto.toDouble() }?.toFloat() ?: 0f
        _total.value = totalGastos
    }
}