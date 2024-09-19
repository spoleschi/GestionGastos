package com.example.myapplication4.ui.categoria

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.CategoryEditActivity
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.R
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.adapters.ColorPickerAdapter
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
        //viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(CategoriaViewModel::class.java)

        setupRecyclerView()
        setupTabLayout()
        observeCategories()

        // Manejar el clic en el botón flotante

        val fabCreate = binding!!.fabCreate
        fabCreate.setOnClickListener {
            // Navegar a la pantalla de creación de categoría
            showDialog()
            Toast.makeText(fabCreate.context, "Toque el boton de agragar categoría", Toast.LENGTH_SHORT).show()
        }

        return binding!!.root
    }

    private fun showDialog() {
        val context = requireContext()
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.activity_category_edit)

        val colors = listOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#000000", "#FF9300", "#808080")
//        val colors = listOf("#FF0000", "#00FF00", "#0000FF" )
        val btnSaveCategory: Button = dialog.findViewById<Button>(R.id.btnSaveCategory)
        val etCategoryName: EditText = dialog.findViewById<EditText>(R.id.etCategoryName)
        val autoCompleteTextView = dialog.findViewById<AutoCompleteTextView>(R.id.spinnerCategoryType)
        val rvColorPicker = dialog.findViewById<RecyclerView>(R.id.rvColorPicker)
        val tvSelectedColor= dialog.findViewById<TextView>(R.id.tvSelectedColor)

        tvSelectedColor.setText("Selecciones un color")

        // Configurar el RecyclerView para la selección de colores

        rvColorPicker.layoutManager = LinearLayoutManager(rvColorPicker.context, LinearLayoutManager.HORIZONTAL, false)
        rvColorPicker.adapter = ColorPickerAdapter(colors) { color ->
            tvSelectedColor.setText("Color seleccionado: $color")
        }

        // Configurar el spinner para el tipo de categoría
        //val categoryTypes = arrayOf("Gasto", "Ingreso")
        val categoryTypes = resources.getStringArray(R.array.category_types)
        val adapter = ArrayAdapter(autoCompleteTextView.context, android.R.layout.simple_dropdown_item_1line, categoryTypes)

        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setText("Gasto", false)

//        btnSaveCategory.setOnClickListener {
//            val selectedValue = autoCompleteTextView.text.toString()
//            //val selectedValue = etCategoryName.text.toString()
//            Toast.makeText(btnSaveCategory.context, "Toque el boton de agragar categoría con el tipo : $selectedValue", Toast.LENGTH_SHORT).show()
//            dialog.hide()
//        }

        btnSaveCategory.setOnClickListener {
            val categoryName = etCategoryName.text.toString()
            val selectedColor = tvSelectedColor.text.toString().substringAfter(": ") // Extraer el código de color
            val selectedType = autoCompleteTextView.text.toString()

            // Crear una nueva categoría
            val newCategory = Categoria(
                // Asignar un ID único, por ejemplo, usando un contador o una librería de generación de IDs
                id = 10,
                nombre = categoryName,
                desc = "",
                color = selectedColor,
                tipo = selectedType
            )

            // Agregar la nueva categoría al ViewModel
            viewModel.addCategory(newCategory)

            // Cerrar el diálogo
            dialog.dismiss()
        }

        dialog.show()
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
            putExtra("CATEGORY_ID", categoria.id.toString()) // Asumiendo que Categoria tiene un id
            putExtra("CATEGORY_NAME", categoria.nombre)
            putExtra("CATEGORY_COLOR", categoria.color)
            putExtra("CATEGORY_TYPE", categoria.tipo)
        }
        startActivity(intent)
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