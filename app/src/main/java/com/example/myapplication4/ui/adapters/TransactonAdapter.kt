package com.example.myapplication4.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.databinding.ItemTransactionBinding
import android.graphics.Color
import com.example.myapplication4.model.Categoria
import com.example.myapplication4.model.Transaccion
import com.example.myapplication4.repository.CategoryRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.KSuspendFunction1

class TransactionAdapter(private val onTransactionClick: KSuspendFunction1<Transaccion, Unit>, private val repository: CategoryRepository) :
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
            GlobalScope.launch {
                val categoria: Categoria? = repository.findCategoryById(transaction.categoriaId)

                // Now update the UI on the main thread
                binding.apply {
                    descriptionTextView.text = transaction.desc
                    amountTextView.text = "$ ${transaction.monto}"

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.dateTextView.text = dateFormat.format(transaction.fecha)

                    // Make sure categoria is not null before accessing its properties
                    categoryTextView.text = categoria?.nombre ?: "Unknown"
                    categoria?.color?.let { color ->
                        categoryTextView.setTextColor(Color.parseColor(color))
                    }

                    // Set click listener for the entire item
                    root.setOnClickListener {
                        GlobalScope.launch {
                            onTransactionClick(transaction) // Lanzar corrutina para la función suspendida
                        }
                    }
                }
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