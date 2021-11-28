package com.syed.budgettracker.adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.syed.budgettracker.activity.DetailedActivity
import com.syed.budgettracker.R
import com.syed.budgettracker.model.Transaction

class TransactionAdapter(private var transactions:List<Transaction>):
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.txtlabel)
        val amount: TextView = view.findViewById(R.id.txtamount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
         val view=LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return TransactionHolder(view)  // the above class is being called to inflate the view into the viewholder of the recycler view
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction: Transaction = transactions[position] // dynamic position
        val context :Context = holder.label.context // extracting the context from one fo the views of the activity to know the status

        if(transaction.amount>=0){
            holder.amount.text=" + ₹%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.moneygreen))
        }
        else{
            holder.amount.text="- ₹%.2f".format(Math.abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.moneyred))
        }
        holder.label.text=transaction.label

        holder.itemView.setOnClickListener {
            val intent= Intent(context, DetailedActivity::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
    fun setData(transactions: List<Transaction>){
        this.transactions=transactions
        notifyDataSetChanged()

    }
}