package com.example.myapplication4.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Categoria
import com.example.myapplication4.R
import com.example.myapplication4.viewHolders.CategoriesViewHolder

class CategoriesAdapter(private val categories:List<Categoria>)
    :RecyclerView.Adapter<CategoriesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoriesViewHolder(view)
    }

//    override fun getItemCount(): Int {
//        return categories.size
//    }
    //Optimizo código
    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.render(categories[position])
    }
}