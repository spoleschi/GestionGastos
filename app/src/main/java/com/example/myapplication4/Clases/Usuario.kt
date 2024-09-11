package com.example.myapplication4.Clases

data class Usuario(
    val id: Int,
    var nombre: String,
    var username: String,
    var pwd: String,
    var saldo: Float
) {
    fun actualizarSaldo(s: Float) {
        this.saldo = s
    }
}
