package com.example.myapplication4.model

import com.example.myapplication4.clases.*

fun CategoriaEntity.toDomain(): Categoria {
    return Categoria(
        id = id,
        nombre = nombre,
        desc = desc,
        color = color,
        tipo = tipo
    )
}

fun Categoria.toEntity(): CategoriaEntity {
    return CategoriaEntity(
        id = id,
        nombre = nombre,
        desc = desc,
        color = color,
        tipo = tipo
    )
}

fun TransactionEntity.toDomain(categoria: Categoria, cuotas: List<Cuota> = emptyList()): Transaccion {
    return when (tipo) {
        "INGRESO" -> Ingreso(
            idIngreso = id,
            descIngreso = descripcion,
            montoIngreso = monto,
            fechaIngreso = fecha,
            categoriaIngreso = categoria
        )
        "GASTO" -> Gasto(
            idGasto = id,
            descGasto = descripcion,
            montoGasto = monto,
            fechaGasto = fecha,
            categoriaGasto = categoria,
            cantCuotas = cantCuotas ?: 1,
            interes = interes ?: 0f,
            cuotas = ArrayList(cuotas)
        )
        else -> throw IllegalArgumentException("Tipo de transacción desconocido: $tipo")
    }
}

fun Transaccion.toEntity(): TransactionEntity {
    return when (this) {
        is Ingreso -> TransactionEntity(
            id = idIngreso,
            descripcion = descIngreso,
            monto = montoIngreso,
            fecha = fechaIngreso,
            categoriaId = categoriaIngreso.id,
            tipo = "INGRESO"
        )
        is Gasto -> TransactionEntity(
            id = idGasto,
            descripcion = descGasto,
            monto = montoGasto,
            fecha = fechaGasto,
            categoriaId = categoriaGasto.id,
            tipo = "GASTO",
            cantCuotas = cantCuotas,
            interes = interes
        )
        else -> throw IllegalArgumentException("Tipo de transacción no soportado")
    }
}

fun Cuota.toEntity(transactionId: Int) = CuotaEntity(
    transaccionId = transactionId,
    nroCuota = nroCuota,
    fechaPago = fechaPago
)

fun CuotaEntity.toDomain() = Cuota(
    nroCuota = nroCuota,
    fechaPago = fechaPago
)