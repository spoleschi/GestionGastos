package com.example.myapplication4.ui.categoria

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.R
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.adapters.ColorPickerAdapter
import com.example.myapplication4.databinding.FragmentCategoriaBinding
import com.google.android.material.tabs.TabLayout

class CategoriaFragmentConDialog : Fragment() {
    private var binding: FragmentCategoriaBinding? = null
    private lateinit var viewModel: CategoriaViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriaBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(CategoriaViewModel::class.java)


        setupRecyclerView()
        setupTabLayout()
        observeCategories()

        // Manejar el clic en el botón flotante

        val fabCreate = binding!!.fabCreate
        fabCreate.setOnClickListener {
            showDialog(null) // Pasar null para indicar que es una nueva categoría
        }

        return binding!!.root
    }

    private fun showDialog(categoria: Categoria?) {
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.fragment_category_edit)

        val colors = listOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#000000", "#FF9300", "#808080")
        val btnSaveCategory: Button = dialog.findViewById(R.id.btnSaveCategory)
        val etCategoryName: EditText = dialog.findViewById(R.id.etCategoryName)
        val autoCompleteTextView = dialog.findViewById<AutoCompleteTextView>(R.id.spinnerCategoryType)
        val rvColorPicker = dialog.findViewById<RecyclerView>(R.id.rvColorPicker)
        val tvSelectedColor= dialog.findViewById<TextView>(R.id.tvSelectedColor)

        // Configurar los campos si estamos editando una categoría existente
        if (categoria != null) {
            etCategoryName.setText(categoria.nombre)
            tvSelectedColor.text = "Color seleccionado: ${categoria.color}"
            autoCompleteTextView.setText(categoria.tipo, false)
        } else {
            tvSelectedColor.text = "Seleccione un color"
            autoCompleteTextView.setText("Gasto", false)
        }

        // Configurar el RecyclerView para la selección de colores

        //rvColorPicker.layoutManager = LinearLayoutManager(rvColorPicker.context, LinearLayoutManager.HORIZONTAL, false)
        rvColorPicker.layoutManager = GridLayoutManager(rvColorPicker.context, 6)
        rvColorPicker.adapter = ColorPickerAdapter(colors) { color ->
            tvSelectedColor.text = "Color seleccionado: $color"
        }

        // Configurar el spinner para el tipo de categoría
        //val categoryTypes = arrayOf("Gasto", "Ingreso")
        val categoryTypes = resources.getStringArray(R.array.category_types)
        val adapter = ArrayAdapter(autoCompleteTextView.context, android.R.layout.simple_dropdown_item_1line, categoryTypes)
        autoCompleteTextView.setAdapter(adapter)

        btnSaveCategory.setOnClickListener {
            val categoryName = etCategoryName.text.toString()
            val selectedColor = tvSelectedColor.text.toString().substringAfter(": ")
            val selectedType = autoCompleteTextView.text.toString()

            if (categoria == null) {
                // Crear una nueva categoría
                val newCategory = Categoria(
                    id = System.currentTimeMillis().toInt(), // Generar un ID único
                    nombre = categoryName,
                    desc = "",
                    color = selectedColor,
                    tipo = selectedType
                )
                viewModel.addCategory(newCategory)
            } else {
                // Actualizar la categoría existente
                val updatedCategory = categoria.copy(
                    nombre = categoryName,
                    color = selectedColor,
                    tipo = selectedType
                )
                viewModel.updateCategory(updatedCategory)
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding!!.recyclerView
//      categoriesAdapter = CategoriesAdapter(emptyList()) { categoria ->
//          penCategoryEditActivity(categoria)
//        }
        categoriesAdapter = CategoriesAdapter(emptyList()) { categoria ->
            showDialog(categoria) // Pasar la categoría seleccionada para edición
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

    override fun onResume() {
        super.onResume()
        observeCategories()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Clear the reference
    }

}