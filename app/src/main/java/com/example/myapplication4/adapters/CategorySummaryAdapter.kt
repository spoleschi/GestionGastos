package com.example.myapplication4.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication4.clases.CategorySummary
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.databinding.ItemCategorySummaryBinding
import android.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CategorySummaryAdapter :
    ListAdapter<CategorySummary, CategorySummaryAdapter.ViewHolder>(CategorySummaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategorySummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCategorySummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categorySummary: CategorySummary) {
            binding.apply {
                categoryName.text = categorySummary.category.nombre
                categoryTotal.text = String.format("$%.2f", categorySummary.total)
                categoryPercentage.text = String.format("%.1f%%", categorySummary.percentage)

                // Aplicar el color de la categoría
                categoryIndicator.setBackgroundColor(Color.parseColor(categorySummary.category.color))

                // Configurar la barra de progreso
                progressBar.progress = categorySummary.percentage.toInt()
                progressBar.progressTintList = ColorStateList.valueOf(
                    Color.parseColor(categorySummary.category.color)
                )
            }
        }
    }


    class CategorySummaryDiffCallback : DiffUtil.ItemCallback<CategorySummary>() {
        override fun areItemsTheSame(
            oldItem: CategorySummary,
            newItem: CategorySummary
        ): Boolean {
            return oldItem.category.id == newItem.category.id
        }

        override fun areContentsTheSame(
            oldItem: CategorySummary,
            newItem: CategorySummary
        ): Boolean {
            return oldItem == newItem
        }
    }
}