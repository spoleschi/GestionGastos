package com.example.myapplication4.ui.transaccion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication4.databinding.FragmentTransactionBinding
import com.example.myapplication4.databinding.FragmentTransactionEditBinding
import com.example.myapplication4.repository.TransactionRepository
import com.example.myapplication4.ui.adapters.TransactionAdapter
import com.example.myapplication4.ui.adapters.CategoryAdapter
import com.example.myapplication4.repository.CategoryRepository
import com.google.android.material.tabs.TabLayout
import android.app.DatePickerDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication4.model.AppDatabase
import com.example.myapplication4.model.Transaccion
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment() {

    private var _listBinding: FragmentTransactionBinding? = null
    private var _editBinding: FragmentTransactionEditBinding? = null
    private val listBinding get() = _listBinding!!
    private val editBinding get() = _editBinding!!

    // Inicializar la base de datos y el repositorio
    val database = AppDatabase.getDatabase(requireContext())
    val repositoryCategoria = CategoryRepository(database.categoriaDao())
    val repositoryTransaccion = TransactionRepository(database.transaccionDao())

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(repositoryCategoria, repositoryTransaccion)
    }

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private var isEditMode = false

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _listBinding = FragmentTransactionBinding.inflate(inflater, container, false)
//        _editBinding = FragmentTransactionEditBinding.inflate(inflater, container, false)
//        return listBinding.root
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Creamos un ConstraintLayout que contendrá ambas vistas
        val rootView = ConstraintLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Inflamos ambos layouts
        _listBinding = FragmentTransactionBinding.inflate(inflater, container, false)
        _editBinding = FragmentTransactionEditBinding.inflate(inflater, container, false)

        // Añadimos ambas vistas al root layout
        rootView.addView(listBinding.root)
        rootView.addView(editBinding.root)

        // Inicialmente ocultamos la vista de edición
        editBinding.root.visibility = View.GONE

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
        setupEditView()
        observeViewModel()
    }

    private fun setupListView() {
        setupRecyclerView()
        setupTabLayout()
        setupPeriodButtons()
        setupFab()
    }

    private fun setupEditView() {
        setupCategoryRecyclerView()
        setupEditViews()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(::selectTransaction,repositoryCategoria)  // Usar referencia al método

        listBinding.transactionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }

    private fun setupTabLayout() {
        listBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewModel.setTransactionType(TransactionType.EXPENSE)
                        editBinding.tilCuotas.visibility = View.VISIBLE
                        editBinding.tilInteres.visibility = View.VISIBLE
                    }
                    1 -> {
                        viewModel.setTransactionType(TransactionType.INCOME)
                        editBinding.tilCuotas.visibility = View.GONE
                        editBinding.tilInteres.visibility = View.GONE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupPeriodButtons() {
        listBinding.dayButton.setOnClickListener { viewModel.setPeriod(Period.DAY) }
        listBinding.weekButton.setOnClickListener { viewModel.setPeriod(Period.WEEK) }
        listBinding.monthButton.setOnClickListener { viewModel.setPeriod(Period.MONTH) }
        listBinding.yearButton.setOnClickListener { viewModel.setPeriod(Period.YEAR) }
        listBinding.allButton.setOnClickListener { viewModel.setPeriod(Period.ALL) }
    }

    private fun setupFab() {
        listBinding.fabAddTransaction.setOnClickListener {
            viewModel.prepareForNewTransaction()
            showEditMode()
            editBinding.tabLayout.getTabAt(listBinding.tabLayout.selectedTabPosition)?.select()
            editBinding.tabLayout.visibility = View.VISIBLE
        }
    }

    private suspend fun selectTransaction(transaction: Transaccion) {
        // Seleccionar el tab correcto según el tipo de transacción
        val tabPosition: Int
        // Establecer el tipo de transacción
        if (transaction.tipo == "Gasto") {
            viewModel.setTransactionType(TransactionType.EXPENSE)
            tabPosition = 0
        } else {
            viewModel.setTransactionType(TransactionType.INCOME)
            tabPosition = 1
        }

//        editBinding.tabLayout.getTabAt(tabPosition)?.select()
        editBinding.tabLayout.visibility = View.GONE

        // Llenar los campos de edición directamente
        editBinding.apply {
            etAmount.setText(transaction.monto.toString())
            tilComment.editText?.setText(transaction.desc)

            // Establecer la fecha
            val calendar = Calendar.getInstance().apply {
                val calendar = Calendar.getInstance()
                calendar.time = transaction.fecha // Asignar la fecha del objeto Date
                set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }
            etDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time))
            viewModel.setSelectedDate(transaction.fecha.year, transaction.fecha.month - 1, transaction.fecha.day)

            // Si es un gasto, llenar campos adicionales
            if (transaction.tipo == "Gasto") {
                etCuotas.setText(transaction.cantCuotas.toString())
                etInteres.setText(transaction.interes.toString())
            }
        }
        // Establecer la categoría seleccionada
        repositoryCategoria.findCategoryById(transaction.categoriaId)
            ?.let { viewModel.setSelectedCategory(it) }

        // Guardar la transacción que se está editando
        viewModel.setEditingTransaction(transaction)

        // Mostrar la vista de edición
        showEditMode()
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            viewModel.setSelectedCategory(category)
        }
        editBinding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = categoryAdapter
        }
    }

    private fun setupEditViews() {

        setupEditTabLayout()

        editBinding.etAmount.addTextChangedListener { editable ->
            editable?.toString()?.toDoubleOrNull()?.let { viewModel.setAmount(it) }
        }

        editBinding.etDate.setOnClickListener { showDatePickerDialog() }

        editBinding.etCuotas.addTextChangedListener { editable ->
            editable?.toString()?.toIntOrNull()?.let { viewModel.setInstallments(it) }
        }

        editBinding.etInteres.addTextChangedListener { editable ->
            editable?.toString()?.toDoubleOrNull()?.let { viewModel.setInterestRate(it) }
        }

        editBinding.btnSaveTransaction.setOnClickListener {
            val amount = editBinding.etAmount.text.toString().toDoubleOrNull()
            val comment = editBinding.tilComment.editText?.text.toString()
            val installments = editBinding.etCuotas.text.toString().toIntOrNull() ?: 1
            val interestRate = editBinding.etInteres.text.toString().toDoubleOrNull() ?: 0.0

            if (amount != null && comment.isNotBlank() && viewModel.selectedCategory.value != null) {
                // Lanzar una corrutina para llamar a la función suspendida
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.saveTransaction(amount, comment, installments, interestRate)
                    showListMode()
                }
            } else {
                showErrorMessage()
            }
        }

        editBinding.btnCancel.setOnClickListener {
            showListMode()
        }
    }
    private fun setupEditTabLayout() {
        editBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewModel.setTransactionType(TransactionType.EXPENSE)
                        editBinding.tilCuotas.visibility = View.VISIBLE
                        editBinding.tilInteres.visibility = View.VISIBLE
                    }
                    1 -> {
                        viewModel.setTransactionType(TransactionType.INCOME)
                        editBinding.tilCuotas.visibility = View.GONE
                        editBinding.tilInteres.visibility = View.GONE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }

        viewModel.currentCategories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { calendar ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            editBinding.etDate.setText(dateFormat.format(calendar.time))
        }

        viewModel.transactionType.observe(viewLifecycleOwner) { type ->
            when (type) {
                TransactionType.EXPENSE -> {
                    editBinding.tilCuotas.visibility = View.VISIBLE
                    editBinding.tilInteres.visibility = View.VISIBLE
                }
                TransactionType.INCOME -> {
                    editBinding.tilCuotas.visibility = View.GONE
                    editBinding.tilInteres.visibility = View.GONE
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = viewModel.selectedDate.value ?: Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                viewModel.setSelectedDate(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

//    private fun showEditMode() {
//        isEditMode = true
//        (view as ViewGroup).removeAllViews()
//        (view as ViewGroup).addView(editBinding.root)
//    }
//
//    private fun showListMode() {
//        isEditMode = false
//        (view as ViewGroup).removeAllViews()
//        (view as ViewGroup).addView(listBinding.root)
//        clearEditFields()
//    }

//    private fun showListMode() {
//        isEditMode = false
//        val parent = (listBinding.root.parent as? ViewGroup)
//        parent?.removeView(listBinding.root)
//        (view as ViewGroup).removeAllViews()
//        (view as ViewGroup).addView(listBinding.root)
//        clearEditFields()
//    }
//
//    private fun showEditMode() {
//        isEditMode = true
//        val parent = (editBinding.root.parent as? ViewGroup)
//        parent?.removeView(editBinding.root)
//        (view as ViewGroup).removeAllViews()
//        (view as ViewGroup).addView(editBinding.root)
//    }

    private fun showEditMode() {
        isEditMode = true
        listBinding.root.visibility = View.GONE
        editBinding.root.visibility = View.VISIBLE
    }

    private fun showListMode() {
        isEditMode = false
        listBinding.tabLayout.getTabAt(editBinding.tabLayout.selectedTabPosition)?.select()
        editBinding.root.visibility = View.GONE
        listBinding.root.visibility = View.VISIBLE
        clearEditFields()
    }

    private fun clearEditFields() {
        editBinding.etAmount.text.clear()
        editBinding.tilComment.editText?.text?.clear()
        editBinding.etCuotas.text?.clear()
        editBinding.etInteres.text?.clear()
        viewModel.resetEditFields()
    }

    private fun showErrorMessage() {
        Snackbar.make(editBinding.root, "Por favor, completa todos los campos", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _listBinding = null
        _editBinding = null
    }
}