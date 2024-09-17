package com.example.myapplication4.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.databinding.ItemCategoryBinding


class CategoryAdapter(private val onCategoryClick: (Categoria) -> Unit) :
    ListAdapter<Categoria, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Categoria) {
            binding.tvCategoryName.text = category.nombre
//            binding.viewColor.setBackgroundColor(android.graphics.Color.parseColor(category.color))
//            binding.divider.setBackgroundColor(android.graphics.Color.parseColor(category.color))
            binding.root.setOnClickListener { onCategoryClick(category) }
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Categoria>() {
    override fun areItemsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        return oldItem.nombre == newItem.nombre
    }

    override fun areContentsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        return oldItem == newItem
    }
}