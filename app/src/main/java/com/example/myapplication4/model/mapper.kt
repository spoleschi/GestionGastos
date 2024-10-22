package com.example.myapplication4.model

import com.example.myapplication4.clases.Categoria

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