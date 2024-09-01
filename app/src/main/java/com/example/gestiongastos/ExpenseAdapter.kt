//Ejemplo de un adaptador para que actúa como un puente entre los datos y las vistas
// que se muestran en el RecyclerView

package com.example.gestiongastos

//class ExpenseAdapter(private val expenses: List<Expense>) :
//    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)
//        val categoryName: TextView = view.findViewById(R.id.categoryName)
//        val expensePercentage: TextView = view.findViewById(R.id.expensePercentage)
//        val expenseAmount: TextView = view.findViewById(R.id.expenseAmount)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.expense_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val expense = expenses[position]
//        holder.categoryIcon.setImageResource(expense.iconResourceId)
//        holder.categoryName.text = expense.category
//        holder.expensePercentage.text = "${expense.percentage}%"
//        holder.expenseAmount.text = "$ ${expense.amount}"
//    }
//
//    override fun getItemCount() = expenses.size
//}