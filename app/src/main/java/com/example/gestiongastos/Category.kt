package com.example.myfirstapplication

sealed class Category {
    object Alimentos: Category()
    object Transporte: Category()
    object Recreacion: Category()
}