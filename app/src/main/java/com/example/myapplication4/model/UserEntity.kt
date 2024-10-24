package com.example.myapplication4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val username: String,
    val pwd: String,
    val saldo: Float
)
