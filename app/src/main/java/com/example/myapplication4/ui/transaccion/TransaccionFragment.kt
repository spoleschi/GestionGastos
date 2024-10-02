package com.example.myapplication4.ui.transaccion

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication4.MainActivity
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.databinding.FragmentTransaccionBinding

import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.adapters.CategoryAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class TransaccionFragment : Fragment() {

    private var _binding: FragmentTransaccionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransaccionViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var descGasto: EditText
    private lateinit var montoGasto: EditText
    private lateinit var fechaGasto: EditText
    private lateinit var numeroCuotas: EditText
    private lateinit var tasaInteres: EditText
    private lateinit var botonGuardarGasto: Button
    private lateinit var categoria: Categoria
    private val vistaGasto: TransaccionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaccionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TransaccionViewModel::class.java)



        descGasto = binding.inputDesc
        montoGasto = binding.etAmount
        fechaGasto = binding.etDate
        numeroCuotas = binding.etCuotas
        tasaInteres = binding.etInteres
        botonGuardarGasto = binding.btnAdd

        //categoria = Categoria("category1","firstone","red","type1")

//        botonGuardarGasto.setOnClickListener {
//            val descripcion = descGasto.text.toString()
//            val montoString = montoGasto.text.toString()
//            val fechaString = fechaGasto.text.toString()
//            val cuotasString = numeroCuotas.text.toString()
//            val interesString = tasaInteres.text.toString()
//
//            if (descripcion.isNotBlank() && montoString.isNotBlank() && fechaString.isNotBlank() && cuotasString.isNotBlank() && interesString.isNotBlank()) {
//                val monto = montoString.toFloat()
//                val cuotas = cuotasString.toInt()
//                val interes = interesString.toFloat()
//                val fecha = fechaString
//                if (monto != null && cuotas != null && interes != null && fecha != null) {
//                    try {
//                        vistaGasto.agregarGasto(
//                            cantCuotas = cuotas,
//                            interes = interes,
//                            desc = descripcion,
//                            monto = monto,
//                            fecha = fecha,
//                            categoria = categoria
//                        )
//                        requireActivity().supportFragmentManager.popBackStack() // Go back to previous fragment
//                    } catch (e: IllegalArgumentException) {
//                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    if (monto == null) montoGasto.error = "Monto inválido"
//                    if (cuotas == null) numeroCuotas.error = "Número de cuotas inválido"
//                    if (interes == null) tasaInteres.error = "Tasa de interés inválida"
//                }
//            } else {
//                if (descripcion.isBlank()) descGasto.error = "Descripción requerida"
//                if (montoString.isBlank()) montoGasto.error = "Monto requerido"
//                if (fechaString.isBlank()) fechaGasto.error = "Fecha requerida"
//                if (cuotasString.isBlank()) numeroCuotas.error = "Número de cuotas requerido"
//                if (interesString.isBlank()) tasaInteres.error = "Tasa de interés requerida"
//            }
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupCategoryRecyclerView()
        setupAmountInput()
        setupDateInput()
        setupInstallmentsInput()
        setupInterestRateInput()
        setupAddButton()
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Cambiar entre gastos e ingresos
                when (tab?.position) {
                    0 -> {
                        viewModel.setTransactionType(TransactionType.EXPENSE)
                        viewModel.showExpenseCategories()
                        binding.tilCuotas.visibility = View.VISIBLE
                        binding.tilInteres.visibility = View.VISIBLE
                    }
                    1 -> {
                        viewModel.setTransactionType(TransactionType.INCOME)
                        viewModel.showIncomeCategories()
                        binding.tilCuotas.visibility = View.GONE
                        binding.tilInteres.visibility = View.GONE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            viewModel.setSelectedCategory(category)
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = categoryAdapter
        }

        // Observar cambios en las categorías
        viewModel.currentCategories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
        }
    }

    private fun setupAmountInput() {
        binding.etAmount.addTextChangedListener { editable ->
            editable?.toString()?.let { amount ->
                if (amount.isNotEmpty()) {
                    viewModel.setAmount(amount.toDouble())
                }
            }
        }
    }

    private fun setupDateInput() {
        binding.etDate.setOnClickListener { showDatePickerDialog() }

        viewModel.selectedDate.observe(viewLifecycleOwner) { calendar ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.etDate.setText(dateFormat.format(calendar.time))
        }
    }

    private fun setupInstallmentsInput() {
        binding.etCuotas.addTextChangedListener { editable ->
            editable?.toString()?.toIntOrNull()?.let { installments ->
                viewModel.setInstallments(installments)
            }
        }
    }

    private fun setupInterestRateInput() {
        binding.etInteres.addTextChangedListener { editable ->
            editable?.toString()?.toDoubleOrNull()?.let { rate ->
                viewModel.setInterestRate(rate)
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

    private fun setupAddButton() {
        binding.btnAdd.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val comment = binding.tilComment.editText?.text.toString()
            val date = viewModel.selectedDate.value
            val installments = binding.etCuotas.text.toString().toIntOrNull() ?: 1
            val interestRate = binding.etInteres.text.toString().toDoubleOrNull() ?: 0.0

            if (amount != null && viewModel.selectedCategory.value != null && date != null) {
                viewModel.addTransaction(amount, comment, date, installments, interestRate)
                // Limpiar campos después de añadir
                binding.etAmount.text.clear()
                binding.tilComment.editText?.text?.clear()
                binding.etCuotas.text?.clear()
                binding.etInteres.text?.clear()
                // Reiniciar la fecha a la actual
                viewModel.setSelectedDate(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
                // Mostrar mensaje de éxito
                // Uso Snackbar en vez de Toast para probar
                Snackbar.make(binding.root, "Transacción añadida con éxito", Snackbar.LENGTH_SHORT).show()
            } else {
                // Mostrar mensaje de error
                Snackbar.make(binding.root, "Por favor, completa todos los campos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}