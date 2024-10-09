package com.example.myapplication4.ui.transaccion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication4.databinding.FragmentTransactionBinding
import com.example.myapplication4.repository.TransactionRepository
import com.example.myapplication4.adapters.TransactionAdapter
import com.google.android.material.tabs.TabLayout

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(TransactionRepository())
    }
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabLayout()
        setupPeriodButtons()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter()
        binding.transactionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TransactionFragment.adapter
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.setTransactionType(TransactionType.EXPENSE)
                    1 -> viewModel.setTransactionType(TransactionType.INCOME)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupPeriodButtons() {
        binding.dayButton.setOnClickListener { viewModel.setPeriod(Period.DAY) }
        binding.weekButton.setOnClickListener { viewModel.setPeriod(Period.WEEK) }
        binding.monthButton.setOnClickListener { viewModel.setPeriod(Period.MONTH) }
        binding.yearButton.setOnClickListener { viewModel.setPeriod(Period.YEAR) }
        binding.allButton.setOnClickListener { viewModel.setPeriod(Period.ALL) }
    }

    private fun observeViewModel() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

