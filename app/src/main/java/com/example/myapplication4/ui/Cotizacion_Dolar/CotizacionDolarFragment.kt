package com.example.myapplication4.ui.Cotizacion_Dolar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication4.databinding.FragmentCotizacionDolarBinding
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

class CotizacionDolarFragment : Fragment() {

    private var _binding: FragmentCotizacionDolarBinding? = null
    private val binding get() = _binding!!

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dolarapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(DolarApiService::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCotizacionDolarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Fetch dollar prices
        fetchDolarPrices()

        return root
    }

    private fun fetchDolarPrices() {
        service.getDolarPrices().enqueue(object : Callback<List<DolarItem>> {
            override fun onResponse(call: Call<List<DolarItem>>, response: Response<List<DolarItem>>) {
                if (response.isSuccessful) {
                    val dolarPrices = response.body()

                    dolarPrices?.let { prices ->
                        for (item in prices) {
                            when (item.casa) {
                                "blue" -> {
                                    binding.precioCompraDolarBlue.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarBlue.text = "Venta: ${item.venta}"
                                }
                                "oficial" -> {
                                    binding.precioCompraDolarOficial.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarOficial.text = "Venta: ${item.venta}"
                                }
                                "bolsa" -> {
                                    binding.precioCompraDolarMep.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarMep.text = "Venta: ${item.venta}"
                                }
                                "tarjeta" -> {
                                    binding.precioCompraDolarTarjeta.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarTarjeta.text = "Venta: ${item.venta}"
                                }
                                /*
                                "mayorista" -> {
                                    binding.precioCompraDolarMayorista.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarMayorista.text = "Venta: ${item.venta}"
                                }
                                "cripto" -> {
                                    binding.precioCompraDolarCripto.text = "Compra: ${item.compra}"
                                    binding.precioVentaDolarCripto.text = "Venta: ${item.venta}"
                                }
                                 */
                            }
                        }
                    }
                } else {
                    binding.precioCompraDolarBlue.text = "Error al obtener datos"
                }
            }

            override fun onFailure(call: Call<List<DolarItem>>, t: Throwable) {
                binding.precioCompraDolarBlue.text = "Error al conectar con API"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
