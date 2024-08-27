package com.example.myapplication4.ui.transaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransaccionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Transaction Fragment"
    }
    val text: LiveData<String> = _text
}