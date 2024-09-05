package com.example.gestiongastos.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestiongastos.R
import com.example.myfirstapplication.Category

class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvCategoryName:TextView = view.findViewById(R.id.tvCategoryName)
    private val divider:View = view.findViewById(R.id.divider)
    fun render(category: Category){
        tvCategoryName.text = "EJEMPLO"
    }

}