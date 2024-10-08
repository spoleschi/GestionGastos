package com.example.myapplication4.repository

import com.example.myapplication4.Clases.*
import java.time.LocalDate

class TransactionRepository {
    private val listaGastos = listOf(
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

    private val listaIngresos = listOf(
        Ingreso(1, "Salario", 3000.0f, LocalDate.of(2023, 5, 1),
            Categoria(6, "Salario", "Ingreso por trabajo", "#4CAF50", "Fijo")),
        Ingreso(2, "Venta de artículos", 500.0f, LocalDate.of(2023, 6, 15),
            Categoria(7, "Ventas", "Ingresos por ventas", "#2196F3", "Variable"))
    )

    fun getExpenses(): List<Gasto> = listaGastos

    fun getIncomes(): List<Ingreso> = listaIngresos
}