package com.example.myapplication4.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.clases.Categoria
import com.example.myapplication4.databinding.ItemCategoryNewBinding


//class CategoryAdapter(private val onCategoryClick: (Categoria) -> Unit) :
//    ListAdapter<Categoria, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
//        Log.d("CategoryAdapter", "onCreateViewHolder called")
//        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CategoryViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        val category = getItem(position)
//        Log.d("CategoryAdapter", "onBindViewHolder called for category: ${category.nombre}")
//        holder.bind(category)
//    }
//
//    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(category: Categoria) {
//            binding.tvCategoryName.text = category.nombre
//            Log.d("CategoryAdapter", "Binding category: ${category.nombre}")
//            binding.root.setOnClickListener { onCategoryClick(category) }
//        }
//    }
//}

class CategoryDiffCallback : DiffUtil.ItemCallback<Categoria>() {
    override fun areItemsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        return oldItem.nombre == newItem.nombre
    }

    override fun areContentsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        return oldItem == newItem
    }
}

class CategoryAdapter(private val onCategoryClick: (Categoria) -> Unit) :
    ListAdapter<Categoria, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Categoria) {
            binding.categoryNameTextView.text = category.nombre
            binding.categoryColorView.setBackgroundColor(Color.parseColor(category.color))
            itemView.setOnClickListener { onCategoryClick(category) }
        }
    }
}