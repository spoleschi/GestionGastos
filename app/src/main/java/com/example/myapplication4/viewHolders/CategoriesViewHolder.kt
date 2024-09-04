package com.example.myapplication4.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Categoria
import com.example.myapplication4.R

class CategoriesViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
    private val divided: View = view.findViewById(R.id.tvCategoryName)
    fun render(category: Categoria){
        tvCategoryName.text = "EJEMPLO"
    }
}