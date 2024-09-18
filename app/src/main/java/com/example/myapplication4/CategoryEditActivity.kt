package com.example.myapplication4

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication4.databinding.ActivityCategoryEditBinding

import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication4.R
import com.example.myapplication4.adapters.ColorPickerAdapter


//class CategoryEditActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityCategoryEditBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCategoryEditBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Recuperar los datos de la categoría
//        val categoryId = intent.getStringExtra("CATEGORY_ID") ?: ""
//        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
//        val categoryColor = intent.getStringExtra("CATEGORY_COLOR") ?: ""
//        val categoryType = intent.getStringExtra("CATEGORY_TYPE") ?: ""
//
//        // Configurar la UI con los datos de la categoría
//        binding.etCategoryName.setText(categoryName)
//        binding.etCategoryColor.setText(categoryColor)
//        binding.spinnerCategoryType.setSelection(if (categoryType == "Gasto") 0 else 1)
//
//        // Configurar el botón de guardar
//        binding.btnSaveCategory.setOnClickListener {
////            saveCategory(categoryId)
//        }
//    }
//
//    private fun saveCategory(categoryId: String) {
//        val name = binding.etCategoryName.text.toString()
//        val color = binding.etCategoryColor.text.toString()
//        val type = if (binding.spinnerCategoryType.selectedItemPosition == 0) "Gasto" else "Ingreso"
//
//        // Aquí implementarías la lógica para guardar los cambios
//        // Por ejemplo, actualizar en una base de datos o enviar a un servidor
//
//        // Después de guardar, cierra la actividad
//        finish()
//    }
//}

class CategoryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryEditBinding
    private val colors = listOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#000000", "#FF9300", "#808080")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar los datos de la categoría
        val categoryId = intent.getStringExtra("CATEGORY_ID") ?: ""
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        val categoryColor = intent.getStringExtra("CATEGORY_COLOR") ?: ""
        val categoryType = intent.getStringExtra("CATEGORY_TYPE") ?: ""

        // Configurar la UI con los datos de la categoría
        binding.etCategoryName.setText(categoryName)
        binding.tvSelectedColor.text = "Color seleccionado: $categoryColor"

        // Configurar el RecyclerView para la selección de colores
        binding.rvColorPicker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvColorPicker.adapter = ColorPickerAdapter(colors) { color ->
            binding.tvSelectedColor.text = "Color seleccionado: $color"
        }

        // Configurar el spinner para el tipo de categoría
        val categoryTypes = arrayOf("Gasto", "Ingreso")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryTypes)
        binding.spinnerCategoryType.setAdapter(adapter)
        binding.spinnerCategoryType.setText(categoryType, false)

        // Configurar el botón de guardar
        binding.btnSaveCategory.setOnClickListener {
            saveCategory(categoryId)
        }
    }

    private fun saveCategory(categoryId: String) {
        val name = binding.etCategoryName.text.toString()
        val color = binding.tvSelectedColor.text.toString().substringAfter(": ")
        val type = binding.spinnerCategoryType.text.toString()

        // Acá implementar la lógica para guardar los cambios
        // Por ejemplo, actualizar en una base de datos o enviar a un servidor

        // Después de guardar, cierra la actividad
        finish()
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView((R.layout.dialog_category_edit))
        dialog.show()
    }
}