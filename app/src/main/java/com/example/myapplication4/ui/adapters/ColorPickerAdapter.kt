package com.example.myapplication4.ui.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.R

class ColorPickerAdapter(
    private val colors: List<String>,
    private val onColorSelected: (String) -> Unit
) : RecyclerView.Adapter<ColorPickerAdapter.ColorViewHolder>() {

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val colorView: View = view.findViewById(R.id.viewColor)

        fun bind(color: String, onColorSelected: (String) -> Unit) {
//            colorView.setBackgroundColor(Color.parseColor(color))
//            itemView.setOnClickListener { onColorSelected(color) }
            //Modifico porque los colorPickers se veían cuadrados. Creo un drawable para cada color...
            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_shape) as GradientDrawable
            drawable.setColor(Color.parseColor(color))
            colorView.background = drawable
            itemView.setOnClickListener { onColorSelected(color) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], onColorSelected)
    }

    override fun getItemCount() = colors.size
}