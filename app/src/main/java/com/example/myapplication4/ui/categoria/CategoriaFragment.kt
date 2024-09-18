package com.example.myapplication4.ui.categoria

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.CategoryEditActivity
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.WelcomeActivity
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.databinding.FragmentCategoriaBinding
import com.google.android.material.tabs.TabLayout

class CategoriaFragment : Fragment() {
    private var binding: FragmentCategoriaBinding? = null
    private lateinit var viewModel: CategoriaViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriaBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)

        setupRecyclerView()
        setupTabLayout()
        observeCategories()

        // Manejar el clic en el botón flotante

        val fabCreate = binding!!.fabCreate
        fabCreate.setOnClickListener {
            // Navegar a la pantalla de creación de categoría
            Toast.makeText(fabCreate.context, "Toque el boton de agragar categoría", Toast.LENGTH_SHORT).show()
        }

        return binding!!.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding!!.recyclerView
        categoriesAdapter = CategoriesAdapter(emptyList()) { categoria ->
            openCategoryEditActivity(categoria)
        }
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 4)
        recyclerView.adapter = categoriesAdapter
    }

    private fun setupTabLayout() {
        val tabLayout = binding!!.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Gastos"))
        tabLayout.addTab(tabLayout.newTab().setText("Ingresos"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.showExpenseCategories()
                    1 -> viewModel.showIncomeCategories()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeCategories() {
        viewModel.currentCategories.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.updateCategories(categories)
        }
    }


    private fun openCategoryEditActivity(categoria: Categoria) {
        val intent = Intent(activity, CategoryEditActivity::class.java).apply {
            putExtra("CATEGORY_ID", categoria.id) // Asumiendo que Categoria tiene un id
            putExtra("CATEGORY_NAME", categoria.nombre)
            putExtra("CATEGORY_COLOR", categoria.color)
            putExtra("CATEGORY_TYPE", categoria.tipo)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Clear the reference
    }
}