package com.example.myapplication4.ui.categoria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoriaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Category Fragment"
    }
    val text: LiveData<String> = _text
}