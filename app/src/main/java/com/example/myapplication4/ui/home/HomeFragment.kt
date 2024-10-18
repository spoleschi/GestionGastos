package com.example.myapplication4.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication4.clases.Gasto
import com.example.myapplication4.clases.Ingreso
import com.example.myapplication4.clases.Transaccion
import com.example.myapplication4.adapters.TransaccionAdapterOld
import com.example.myapplication4.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

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
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initUI()
        observeViewModel()
    }

//    private fun initUI() {
//        val dateText = binding.dateText
//        val currentDate = Calendar.getInstance().time
//        val dateFormat = SimpleDateFormat("dd/MM/yy")
//        val formattedDate = dateFormat.format(currentDate)
//        dateText.text = formattedDate
//
//        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)
//    }

    private fun initUI() {
        val dateText = binding.dateText
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        val formattedDate = dateFormat.format(currentDate)
        dateText.text = formattedDate

        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)

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
    }

//    private fun observeViewModel() {
//        viewModel.gastos.observe(viewLifecycleOwner) { gastos ->
//            binding.expensesRecyclerView.adapter = GastoAdapter(gastos)
//        }
//
//        viewModel.total.observe(viewLifecycleOwner) { total ->
//            binding.totalAmount.text = "$ %.2f".format(total)
//        }
//    }

    private fun observeViewModel() {
        viewModel.transacciones.observe(viewLifecycleOwner) { transacciones ->
            updateRecyclerView(transacciones)
        }

        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.totalAmount.text = "$ %.2f".format(total)
        }

        viewModel.tipoTransaccion.observe(viewLifecycleOwner) { tipo ->
            binding.totalLabel.text = when (tipo) {
                TipoTransaccion.GASTO -> "Total Gastos"
                TipoTransaccion.INGRESO -> "Total Ingresos"
            }
            updateRecyclerView(viewModel.transacciones.value ?: emptyList())
        }
    }

    private fun updateRecyclerView(transacciones: List<Transaccion>) {
        val filteredTransacciones = when (viewModel.tipoTransaccion.value) {
            TipoTransaccion.GASTO -> transacciones.filterIsInstance<Gasto>()
            TipoTransaccion.INGRESO -> transacciones.filterIsInstance<Ingreso>()
            else -> emptyList()
        }
        binding.expensesRecyclerView.adapter = TransaccionAdapterOld(filteredTransacciones)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}