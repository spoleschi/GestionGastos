package com.example.myapplication4.ui.transaccion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication4.MainActivity
import com.example.myapplication4.adapters.CategoriesAdapter
import com.example.myapplication4.databinding.FragmentTransaccionBinding

import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication4.adapters.CategoryAdapter
import com.google.android.material.tabs.TabLayout


class TransaccionFragment : Fragment() {

    private var _binding: FragmentTransaccionBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: TransaccionViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaccionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TransaccionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupCategoryRecyclerView()
        setupAmountInput()
        setupAddButton()
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Cambiar entre gastos e ingresos
                when (tab?.position) {
                    0 -> viewModel.setTransactionType(TransactionType.EXPENSE)
                    1 -> viewModel.setTransactionType(TransactionType.INCOME)
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
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
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

    private fun setupAddButton() {
        binding.btnAdd.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val comment = binding.tilComment.editText?.text.toString()

            if (amount != null && viewModel.selectedCategory.value != null) {
                viewModel.addTransaction(amount, comment)
                // Limpiar campos después de añadir
                binding.etAmount.text.clear()
                binding.tilComment.editText?.text?.clear()
                // Mostrar mensaje de éxito
                // Puedes usar Snackbar o Toast aquí
            } else {
                // Mostrar mensaje de error
                // Puedes usar Snackbar o Toast aquí
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}