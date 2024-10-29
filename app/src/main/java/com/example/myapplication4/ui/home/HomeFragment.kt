package com.example.myapplication4.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication4.ui.adapters.CategorySummaryAdapter
import com.example.myapplication4.databinding.FragmentHomeBinding
import com.example.myapplication4.model.AppDatabase
import com.example.myapplication4.repository.CategoryRepositoryImpl
import com.example.myapplication4.repository.TransactionRepositoryImpl
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategorySummaryAdapter

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
        setupViewModel()
        setupRecyclerView()
        initUI()
        observeViewModel()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val categoryRepository = CategoryRepositoryImpl(database.categoriaDao())
        val transactionRepository = TransactionRepositoryImpl(
            database.transactionDao(),
            categoryRepository
        )

        viewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(transactionRepository)
        )[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategorySummaryAdapter()
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun initUI() {
        // Configurar fecha actual
        val dateText = binding.dateText
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        dateText.text = currentDate.format(formatter)

        // Configurar TabLayout
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setTipoTransaccion(TipoTransaccion.GASTO)
                    1 -> viewModel.setTipoTransaccion(TipoTransaccion.INGRESO)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Configurar botones de período
        setupPeriodButtons()
    }

    private fun setupPeriodButtons() {
        binding.apply {
            dayButton.setOnClickListener {
                val today = LocalDate.now()
                viewModel.setPeriod(today, today)
                updatePeriodButtonStates(dayButton)
            }

            weekButton.setOnClickListener {
                val today = LocalDate.now()
                viewModel.setPeriod(today.minusWeeks(1), today)
                updatePeriodButtonStates(weekButton)
            }

            monthButton.setOnClickListener {
                val today = LocalDate.now()
                viewModel.setPeriod(today.withDayOfMonth(1), today)
                updatePeriodButtonStates(monthButton)
            }

            yearButton.setOnClickListener {
                val today = LocalDate.now()
                viewModel.setPeriod(today.withDayOfYear(1), today)
                updatePeriodButtonStates(yearButton)
            }

            allButton.setOnClickListener {
                viewModel.setPeriod(null, null)  // Pasar null para indicar sin restricción de fecha
                updatePeriodButtonStates(allButton)
            }

            // Iniciar con el mes seleccionado
            monthButton.performClick()
        }
    }

    private fun updatePeriodButtonStates(selectedButton: Button) {
        binding.apply {
            listOf(dayButton, weekButton, monthButton, yearButton, allButton).forEach { button ->
                button.isSelected = button == selectedButton
                button.alpha = if (button == selectedButton) 1f else 0.6f
            }
        }
    }

    private fun observeViewModel() {
        viewModel.categorySummaries.observe(viewLifecycleOwner) { summaries ->
            categoryAdapter.submitList(summaries)
        }

        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.totalAmount.text = String.format("$ %.2f", total)
        }

        viewModel.tipoTransaccion.observe(viewLifecycleOwner) { tipo ->
            binding.totalLabel.text = when (tipo) {
                TipoTransaccion.GASTO -> "Total Gastos"
                TipoTransaccion.INGRESO -> "Total Ingresos"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}