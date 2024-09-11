package com.example.myapplication4.ui.categoria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoriaViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<Categoria>>()
    val categories: LiveData<List<Categoria>> = _categories

    data class Categoria(val name: String, val color: String)

    fun updateCategories(newCategories: List<Categoria>) {
        _categories.value = newCategories
    }
}