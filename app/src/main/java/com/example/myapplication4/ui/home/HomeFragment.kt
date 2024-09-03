package com.example.myapplication4.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication4.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencia al TextView
        val dateText = binding.dateText

        // Obtener la fecha actual y formatearla
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        val formattedDate = dateFormat.format(currentDate)

        // Asignar la fecha formateada al TextView
        dateText.text = formattedDate

        // Aquí deberíamos agregar lógica adicional para interactuar con los elementos de la vista
        // Por ejemplo, configurar el RecyclerView, manejar los clics en los botones, etc.

        // Ejemplo: Configurar el RecyclerView
        val expensesRecyclerView = binding.expensesRecyclerView
        // ... configurar el adaptador y otros parámetros del RecyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}