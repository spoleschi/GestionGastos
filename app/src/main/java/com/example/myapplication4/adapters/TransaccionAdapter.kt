package com.example.myapplication4.adapters
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication4.Clases.Gasto
import com.example.myapplication4.Clases.Ingreso
import com.example.myapplication4.Clases.Transaccion
import com.example.myapplication4.R
import java.text.SimpleDateFormat
import java.util.*

class TransaccionAdapter(private val transacciones: List<Transaccion>) :
    RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    class TransaccionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descripcion: TextView = view.findViewById(R.id.tvDescrip)
        val monto: TextView = view.findViewById(R.id.tvMonto)
        //        val fecha: TextView = view.findViewById(R.id.tvCat)
        val categoria: TextView = view.findViewById(R.id.tvCat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaccion, parent, false)
        return TransaccionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        val transaccion = transacciones[position]
        holder.descripcion.text = transaccion.desc
        holder.monto.text = "$ %.2f".format(transaccion.monto)
//        holder.fecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaccion.fecha)
        holder.categoria.text = transaccion.categoria.nombre

        holder.categoria.setBackgroundColor(Color.parseColor(transaccion.categoria.color))

        // Añadir lógica adicional aquí si es necesario manejar Gastos e Ingresos de manera diferente
        when (transaccion) {
            is Gasto -> {
                // Lógica específica para Gastos si es necesario
            }
            is Ingreso -> {
                // Lógica específica para Ingresos si es necesario
            }
        }
    }

    override fun getItemCount() = transacciones.size
}
