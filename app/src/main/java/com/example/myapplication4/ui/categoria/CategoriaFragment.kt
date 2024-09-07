package com.example.myapplication4.ui.categoria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.databinding.FragmentCategoriaBinding
import com.google.android.material.tabs.TabLayout

class CategoriaFragment : Fragment() {

    private val categories = listOf(
        Categoria(
            "Alimentos",
            "",
            "Verde",
            "Gasto"
        ),
        Categoria(
            "Trasporte",
            "",
            "Amarillo",
            "Gasto"
        ),
        Categoria(
            "Recreación",
            "",
            "Azul",
            "Gasto"
        )
    )
    private lateinit var categoriesAdapter: CategoriesAdapter

    private var binding: FragmentCategoriaBinding? = null // Use nullable type
    private lateinit var viewModel: CategoriaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriaBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)

        // Setup TabLayout (if needed)
        val tabLayout = binding?.tabLayout
        if (tabLayout != null) {
            // Access tabLayout properties
            tabLayout.addTab(tabLayout.newTab().setText("Gastos"))
            tabLayout.addTab(tabLayout.newTab().setText("Ingresos"))
        }

        // Setup RecyclerView
        val recyclerView = binding!!.recyclerView // Access using !! after null check
        categoriesAdapter = CategoriesAdapter(categories)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = categoriesAdapter

        // Manejar el clic en el botón flotante

        val fabCreate = binding!!.fabCreate
        fabCreate.setOnClickListener {
            // Navegar a la pantalla de creación de categoría
            Toast.makeText(fabCreate.context, "Toque el boton de agragar categoría", Toast.LENGTH_SHORT).show()
        }

        return binding!!.root
    }

    private fun setupTabLayout(tabLayout: TabLayout) {
        // ... (same as before)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Clear the reference
    }
}