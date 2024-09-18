package com.example.myapplication4.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.viewHolders.CategoriesViewHolder
import com.example.myapplication4.R

//class CategoriesAdapter(
//    private val categories: List<Categoria>,
//    private val onClickListener: (Categoria) -> Unit
//) : RecyclerView.Adapter<CategoriesViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
//        return CategoriesViewHolder(view)
//    }
//
//    override fun getItemCount() = categories.size
//
//    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
//        val category = categories[position]
//        holder.render(category, holder.itemView)
//        holder.itemView.setOnClickListener { onClickListener(category) }
//    }
//}

class CategoriesAdapter(
    private var categories: List<Categoria>,
    private val onClickListener: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_new, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]
        holder.render(category, holder.itemView)
        holder.itemView.setOnClickListener { onClickListener(category) }
    }

    fun updateCategories(newCategories: List<Categoria>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}