package com.example.myapplication4.ui.transaccion

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.databinding.FragmentTransaccionBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

class TransaccionFragment : Fragment() {

    private var _binding: FragmentTransaccionBinding? = null
    private val binding get() = _binding!!

    private lateinit var descGasto: EditText
    private lateinit var montoGasto: EditText
    private lateinit var fechaGasto: EditText
    private lateinit var numeroCuotas: EditText
    private lateinit var tasaInteres: EditText
    private lateinit var botonGuardarGasto: Button
    private lateinit var categoria: Categoria

    private val vistaGasto: TransaccionViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(TransaccionViewModel::class.java)
        _binding = FragmentTransaccionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        descGasto = binding.inputDesc
        montoGasto = binding.inputMonto
        fechaGasto = binding.inputDate
        numeroCuotas = binding.inputCuotas
        tasaInteres = binding.inputInteres
        botonGuardarGasto = binding.buttonGuardarGasto

        categoria = Categoria("category1","firstone","red","type1")

        botonGuardarGasto.setOnClickListener {
            val descripcion = descGasto.text.toString()
            val montoString = montoGasto.text.toString()
            val fechaString = fechaGasto.text.toString()
            val cuotasString = numeroCuotas.text.toString()
            val interesString = tasaInteres.text.toString()

            if (descripcion.isNotBlank() && montoString.isNotBlank() && fechaString.isNotBlank() && cuotasString.isNotBlank() && interesString.isNotBlank()) {
                val monto = montoString.toFloat()
                val cuotas = cuotasString.toInt()
                val interes = interesString.toFloat()
                val fecha = parseDate(fechaString)
                if (monto != null && cuotas != null && interes != null && fecha != null) {
                    try {
                        vistaGasto.agregarGasto(
                            cantCuotas = cuotas,
                            interes = interes,
                            desc = descripcion,
                            monto = monto,
                            fecha = fecha,
                            categoria = categoria
                        )
                        requireActivity().supportFragmentManager.popBackStack() // Go back to previous fragment
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (monto == null) montoGasto.error = "Monto inválido"
                    if (cuotas == null) numeroCuotas.error = "Número de cuotas inválido"
                    if (interes == null) tasaInteres.error = "Tasa de interés inválida"
                }
            } else {
                if (descripcion.isBlank()) descGasto.error = "Descripción requerida"
                if (montoString.isBlank()) montoGasto.error = "Monto requerido"
                if (fechaString.isBlank()) fechaGasto.error = "Fecha requerida"
                if (cuotasString.isBlank()) numeroCuotas.error = "Número de cuotas requerido"
                if (interesString.isBlank()) tasaInteres.error = "Tasa de interés requerida"
            }
        }

        return root
    }

    fun parseDate(dateString: String, format: String = "yyyy-MM-dd"): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern(format)
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null // Return null if parsing fails
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
