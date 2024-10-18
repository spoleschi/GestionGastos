package com.example.myapplication4.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.clases.Gasto
import com.example.myapplication4.clases.Ingreso
import com.example.myapplication4.clases.Transaccion
import com.example.myapplication4.databinding.ItemTransaccionBinding

class TransaccionAdapterOld(
    private var transacciones: List<Transaccion>
) : RecyclerView.Adapter<TransaccionAdapterOld.TransaccionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val binding = ItemTransaccionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaccionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        holder.bind(transacciones[position])
    }

    override fun getItemCount() = transacciones.size

    fun updateTransacciones(newTransacciones: List<Transaccion>) {
        transacciones = newTransacciones
        notifyDataSetChanged()
    }

    inner class TransaccionViewHolder(private val binding: ItemTransaccionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaccion: Transaccion) {
            binding.apply {
                when (transaccion) {
                    is Gasto -> {
                        tvDescrip.text = transaccion.descGasto
                        tvMonto.text = "- $ %.2f".format(transaccion.monto)
//                        tvFecha.text = transaccion.fechaGasto.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        tvCat.text = transaccion.categoriaGasto.nombre
                        tvCat.setBackgroundColor(Color.parseColor(transaccion.categoriaGasto.color))
                    }
                    is Ingreso -> {
                        tvDescrip.text = transaccion.descIngreso
                        tvMonto.text = "+ $ %.2f".format(transaccion.monto)
//                        tvFecha.text = transaccion.fechaIngreso.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        tvCat.text = transaccion.categoriaIngreso.nombre
                        tvCat.setBackgroundColor(Color.parseColor(transaccion.categoriaIngreso.color))
                    }
                }
            }
        }
    }
}