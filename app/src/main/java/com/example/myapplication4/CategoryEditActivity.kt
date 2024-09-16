package com.example.myapplication4

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication4.databinding.ActivityCategoryEditBinding

class CategoryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar los datos de la categoría
//        val categoryId = intent.getStringExtra("CATEGORY_ID") ?: ""
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        val categoryColor = intent.getStringExtra("CATEGORY_COLOR") ?: ""
        val categoryType = intent.getStringExtra("CATEGORY_TYPE") ?: ""

        // Configurar la UI con los datos de la categoría
        binding.etCategoryName.setText(categoryName)
        binding.etCategoryColor.setText(categoryColor)
        binding.spinnerCategoryType.setSelection(if (categoryType == "Gasto") 0 else 1)

        // Configurar el botón de guardar
        binding.btnSaveCategory.setOnClickListener {
//            saveCategory(categoryId)
        }
    }

    private fun saveCategory(categoryId: String) {
        val name = binding.etCategoryName.text.toString()
        val color = binding.etCategoryColor.text.toString()
        val type = if (binding.spinnerCategoryType.selectedItemPosition == 0) "Gasto" else "Ingreso"

        // Aquí implementarías la lógica para guardar los cambios
        // Por ejemplo, actualizar en una base de datos o enviar a un servidor

        // Después de guardar, cierra la actividad
        finish()
    }
}