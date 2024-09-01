package com.example.gestiongastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class MainScreenActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var dayButton: Button
    private lateinit var weekButton: Button
    private lateinit var monthButton: Button
    private lateinit var yearButton: Button
    private lateinit var periodButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.expensesRecyclerView)
        dayButton = findViewById(R.id.dayButton)
        weekButton = findViewById(R.id.weekButton)
        monthButton = findViewById(R.id.monthButton)
        yearButton = findViewById(R.id.yearButton)
        periodButton = findViewById(R.id.periodButton)

        setupTabLayout()
        setupRecyclerView()
        setupPeriodButtons()
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
        val buttons = listOf(dayButton, weekButton, monthButton, yearButton, periodButton)
        buttons.forEach { button ->
            button.setOnClickListener {
                // Actualizar la vista según el período seleccionado
            }
        }
    }
}