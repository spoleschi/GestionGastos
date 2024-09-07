package com.example.myapplication4.ui.categoria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoriaViewModel : ViewModel() {

    // Replace this with your actual data source (e.g., Room database or remote API)
    private val _categories = MutableLiveData<List<Categoria>>()
    val categories: LiveData<List<Categoria>> = _categories

    // Sample Categoria data class (modify according to your needs)
    data class Categoria(val name: String, val color: String)

    // Function to update categories list (replace with your logic)
    fun updateCategories(newCategories: List<Categoria>) {
        _categories.value = newCategories
    }
}