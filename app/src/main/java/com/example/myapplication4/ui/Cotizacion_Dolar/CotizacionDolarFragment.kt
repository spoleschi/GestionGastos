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

class CotizacionDolarFragment : Fragment() {

    private var _binding: FragmentCotizacionDolarBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CotizacionDolarViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCotizacionDolarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this)[CotizacionDolarViewModel::class.java]

        setupObservers()
        viewModel.fetchDolarPrices()

        return root
    }

    private fun setupObservers() {
        viewModel.dolarPrices.observe(viewLifecycleOwner) { prices ->
            prices?.forEach { item ->
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
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                binding.precioCompraDolarBlue.text = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}