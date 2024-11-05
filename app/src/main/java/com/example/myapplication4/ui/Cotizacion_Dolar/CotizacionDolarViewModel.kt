package com.example.myapplication4.ui.Cotizacion_Dolar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class DolarItem(
    val moneda: String,
    val casa: String,
    val nombre: String,
    val compra: Double,
    val venta: Double,
    val fechaActualizacion: String
)

interface DolarApiService {
    @GET("v1/dolares")
    fun getDolarPrices(): Call<List<DolarItem>>
}

class CotizacionDolarViewModel : ViewModel() {

    private val _dolarPrices = MutableLiveData<List<DolarItem>>()
    val dolarPrices: LiveData<List<DolarItem>> = _dolarPrices

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dolarapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(DolarApiService::class.java)

    fun fetchDolarPrices() {
        service.getDolarPrices().enqueue(object : Callback<List<DolarItem>> {
            override fun onResponse(call: Call<List<DolarItem>>, response: Response<List<DolarItem>>) {
                if (response.isSuccessful) {
                    _dolarPrices.value = response.body()
                } else {
                    _error.value = "Error al obtener datos"
                }
            }

            override fun onFailure(call: Call<List<DolarItem>>, t: Throwable) {
                _error.value = "Error al conectar con API"
            }
        })
    }
}