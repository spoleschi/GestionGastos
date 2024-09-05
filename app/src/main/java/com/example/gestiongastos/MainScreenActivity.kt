package com.example.gestiongastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapplication.CategoriesAdapter
import com.example.myfirstapplication.Category
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar

class MainScreenActivity : AppCompatActivity() {

    private val categories = listOf(
        Category.Alimentos,
        Category.Transporte,
        Category.Recreacion
    )
    private lateinit var dateText: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var dayButton: Button
    private lateinit var weekButton: Button
    private lateinit var monthButton: Button
    private lateinit var yearButton: Button
    private lateinit var periodButton: Button

    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.expensesRecyclerView)
        dayButton = findViewById(R.id.dayButton)
        weekButton = findViewById(R.id.weekButton)
        monthButton = findViewById(R.id.monthButton)
        yearButton = findViewById(R.id.yearButton)

        // Obtener referencia al TextView
        dateText = findViewById(R.id.dateText)

        // Obtener la fecha actual y formatearla
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        val formattedDate = dateFormat.format(currentDate)

        // Asignar la fecha formateada al TextView
        dateText.text = formattedDate

        // Aquí deberíamos agregar lógica adicional para interactuar con los elementos de la vista
        // Por ejemplo, configurar el RecyclerView, manejar los clics en los botones, etc.

        // Ejemplo: Configurar el RecyclerView
        //val expensesRecyclerView = binding.expensesRecyclerView
        // ... configurar el adaptador y otros parámetros del RecyclerView

        setupTabLayout()
        setupRecyclerView()
        setupPeriodButtons()

        initUI()
    }

    private fun initUI() {
        categoriesAdapter = CategoriesAdapter(categories)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = categoriesAdapter
    }

    private fun setupTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Actualizar la vista según la pestaña seleccionada (Gastos o Ingresos)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Aquí se debería configurar nuestro adaptador personalizado
        //El adaptador actúa como un puente entre los datos y las vistas que se muestran en el RecyclerView.
        // recyclerView.adapter = YourCustomAdapter()
    }

    private fun setupPeriodButtons() {
        val buttons = listOf(dayButton, weekButton, monthButton, yearButton)
        buttons.forEach { button ->
            button.setOnClickListener {
                // Actualizar la vista según el período seleccionado
            }
        }
    }
}