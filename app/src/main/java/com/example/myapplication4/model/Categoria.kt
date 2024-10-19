package com.example.myapplication4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nombre: String,
    var desc: String,
    var color: String,
    var tipo: String
)