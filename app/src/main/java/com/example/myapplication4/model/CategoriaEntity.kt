package com.example.myapplication4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey
    val id: Int,
    //@PrimaryKey(autoGenerate = true) var id: Int = 0,
    val nombre: String,
    val desc: String,
    val color: String,
    val tipo: String
)
