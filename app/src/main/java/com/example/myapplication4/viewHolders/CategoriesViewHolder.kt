package com.example.myapplication4.viewHolders

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Categoria
import com.example.myapplication4.R

//class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//    private val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
//    private val divider: View = view.findViewById(R.id.divider)
//    private val itemLayout: View = view.findViewById(R.id.itemLayout)
//
//    fun render(categoria: Categoria, view: View) {
//        tvCategoryName.text = categoria.nombre
//
//        // Obtener el color hexadecimal de la categoría
//        val hexColor = categoria.color // Suponiendo que tienes un atributo colorHexadecimal en Categoria
//
//        // Convertir el color hexadecimal a un entero
//        val colorInt = Color.parseColor("#$hexColor")
//
//        // Aplicar el color al divider
//        itemLayout.setBackgroundColor((colorInt))
//    }
//}

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvCategoryName: TextView = view.findViewById(R.id.categoryNameTextView)
    private val vCategoryColor: View = view.findViewById(R.id.categoryColorView)


    //private val divider: View = view.findViewById(R.id.divider)
    //private val itemLayout: View = view.findViewById(R.id.itemLayout)

    fun render(categoria: Categoria, view: View) {
        tvCategoryName.text = categoria.nombre

        // Convertir el color hexadecimal a un entero
        val colorInt = Color.parseColor(categoria.color)

        // Aplicar el color al divider/círculo
        //itemLayout.setBackgroundColor((colorInt))
        vCategoryColor.setBackgroundColor(colorInt)

    }
}
