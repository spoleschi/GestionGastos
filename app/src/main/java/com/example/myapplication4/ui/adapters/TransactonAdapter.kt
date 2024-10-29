package com.example.myapplication4.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.databinding.ItemTransactionBinding
import java.time.format.DateTimeFormatter
import android.graphics.Color
import com.example.myapplication4.clases.Transaccion

class TransactionAdapter(private val onTransactionClick: (Transaccion) -> Unit) :
    ListAdapter<Transaccion, TransactionAdapter.ViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaccion) {
            binding.apply {
                descriptionTextView.text = transaction.desc
                amountTextView.text = "$ ${transaction.monto}"
                dateTextView.text = transaction.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                categoryTextView.text = transaction.categoria.nombre
                categoryTextView.setTextColor(Color.parseColor(transaction.categoria.color))

                // Set click listener for the entire item
                root.setOnClickListener { onTransactionClick(transaction) }
            }
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaccion>() {
        override fun areItemsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaccion, newItem: Transaccion): Boolean {
            return oldItem == newItem
        }
    }
}