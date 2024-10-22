
package com.example.myapplication4.ui.categoria
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.R
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.adapters.ColorPickerAdapter
import com.example.myapplication4.databinding.FragmentCategoriaBinding
import com.example.myapplication4.databinding.FragmentCategoryEditBinding
import com.example.myapplication4.repository.CategoryRepository
import com.example.myapplication4.repository.CategoryRepositoryImpl
import com.google.android.material.tabs.TabLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication4.model.AppDatabase
import kotlinx.coroutines.launch

class CategoriaFragment : Fragment() {
    private var _binding: FragmentCategoriaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CategoriaViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriaBinding.inflate(inflater, container, false)
        val database = AppDatabase.getDatabase(requireContext())
        val repository: CategoryRepository = CategoryRepositoryImpl(database.categoriaDao())

        viewModel = ViewModelProvider(
            requireActivity(),
            CategoriaViewModel.Factory(repository)
        )[CategoriaViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar los componentes en el orden correcto
        setupRecyclerView()
        setupTabLayout()
        observeCategories()

        binding.fabCreate.setOnClickListener {
            showEditView(null)
        }
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesAdapter(emptyList()) { categoria ->
            showEditView(categoria)
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = categoriesAdapter
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentCategories.collect { categories ->
                categoriesAdapter.updateCategories(categories)
            }
        }
    }

    private fun showEditView(categoria: Categoria?) {
        // Ocultar la vista principal
        binding.mainContent.visibility = View.GONE

        // Inflar la vista de edición
        val editView = FragmentCategoryEditBinding.inflate(layoutInflater, binding.root, true)

        // Configurar la vista de edición
        setupEditView(editView, categoria)
    }

    private fun setupEditView(editBinding: FragmentCategoryEditBinding, categoria: Categoria?) {
        val colors = listOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#000000", "#FF9300", "#808080")

        if (categoria != null) {
            editBinding.etCategoryName.setText(categoria.nombre)
            editBinding.tvSelectedColor.text = "Color seleccionado: ${categoria.color}"
            editBinding.spinnerCategoryType.setText(categoria.tipo, false)
        } else {
            editBinding.tvSelectedColor.text = "Seleccione un color"
            editBinding.spinnerCategoryType.setText("Gasto", false)
        }

        editBinding.rvColorPicker.layoutManager = GridLayoutManager(requireContext(), 6)
        editBinding.rvColorPicker.adapter = ColorPickerAdapter(colors) { color ->
            editBinding.tvSelectedColor.text = "Color seleccionado: $color"
        }

        val categoryTypes = resources.getStringArray(R.array.category_types)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryTypes)
        editBinding.spinnerCategoryType.setAdapter(adapter)

        editBinding.btnSaveCategory.setOnClickListener {
            saveCategory(editBinding, categoria)
        }
        editBinding.btnCancel.setOnClickListener {
            returnToMainView()
        }
        // Si la categoría existe, mostrar el botón eliminar y manejar el clic
        if (categoria != null) {
            editBinding.btnDeleteCategory.visibility = View.VISIBLE
            editBinding.btnDeleteCategory.setOnClickListener {
                eliminarCategoria(categoria)
            }
        } else {
            editBinding.btnDeleteCategory.visibility = View.GONE
        }
    }

    private fun saveCategory(editBinding: FragmentCategoryEditBinding, oldCategoria: Categoria?) {
        val categoryName = editBinding.etCategoryName.text.toString()
        val selectedColor = editBinding.tvSelectedColor.text.toString().substringAfter(": ")
        val selectedType = editBinding.spinnerCategoryType.text.toString()

        if (oldCategoria == null) {
            val newCategory = Categoria(
                id = System.currentTimeMillis().toInt() ,
                nombre = categoryName,
                desc = "",
                color = selectedColor,
                tipo = selectedType
            )
            viewModel.addCategory(newCategory)
        } else {
            val updatedCategory = oldCategoria.copy(
                nombre = categoryName,
                color = selectedColor,
                tipo = selectedType
            )
            viewModel.updateCategory(updatedCategory)
        }

        returnToMainView()
    }

    private fun eliminarCategoria(categoria: Categoria) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Categoría")
            .setMessage("¿Estás seguro de que deseas eliminar esta categoría?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteCategory(categoria)
                returnToMainView()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun returnToMainView() {
        // Remover la vista de edición y mostrar la vista principal
        (binding.root as ViewGroup).removeViewAt((binding.root as ViewGroup).childCount - 1)
        binding.mainContent.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        observeCategories()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the reference
    }
}