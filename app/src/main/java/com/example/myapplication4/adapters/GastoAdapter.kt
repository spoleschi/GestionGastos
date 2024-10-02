package com.example.myapplication4.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Gasto
import com.example.myapplication4.R
import com.example.myapplication4.databinding.ItemCategoryNewBinding
import com.example.myapplication4.databinding.ItemTransaccionBinding

class GastoAdapter(private val gastos: List<Gasto>) : RecyclerView.Adapter<GastoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        // Aquí se infla el layout para cada elemento de la lista
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaccion, parent, false)
        val binding = ItemTransaccionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GastoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        // Aquí se asignan los datos de cada gasto al ViewHolder
        val gasto = gastos[position]
        holder.bind(gasto)
    }

    override fun getItemCount() = gastos.size
}

class GastoViewHolder(private val binding: ItemTransaccionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(gasto: Gasto) {
        // Aquí se mapean los datos del gasto a los elementos de la vista

        binding.tvDescrip.text = gasto.descGasto
        binding.tvMonto.text = gasto.montoGasto.toString()
        binding.tvCat.text = gasto.categoriaGasto.nombre
        binding.tvCat.setBackgroundColor(Color.parseColor(gasto.categoriaGasto.color))
    }
}