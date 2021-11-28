package com.syed.budgettracker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.syed.budgettracker.database.AppDatabase
import com.syed.budgettracker.R
import com.syed.budgettracker.model.Transaction
import com.syed.budgettracker.adapter.TransactionAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions: List<Transaction>
    private lateinit var oldTransactions: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtrupees: TextView
    private lateinit var txtbudgetrupees: TextView
    private lateinit var txtexpenserupees: TextView
    private lateinit var FloatingActionButton: FloatingActionButton
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        txtrupees = findViewById(R.id.txtrupees)
        txtbudgetrupees = findViewById(R.id.txtbudgetrupees)
        txtexpenserupees = findViewById(R.id.txtexpenserupees)
        FloatingActionButton = findViewById(R.id.FloatActionButton)

        // Database tasks are resource intensive so use background thread to execute tasks thus use coroutines functionality of kotlin
        db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)

        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }
        fetchAll()
        // the code below here adds the swipe to remove functionality to the recyclerview
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition]) // this is a method defined at the bottom
            }

        }
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)


        FloatingActionButton.setOnClickListener {

            intent = Intent(this@MainActivity, AddTransactionActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        fetchAll()
        super.onResume()

    }

    fun deleteTransaction(transaction: Transaction) {
        deletedTransaction = transaction
        oldTransactions = transactions
        // Deleting the transaction that user has swiped on using the filter method


        GlobalScope.launch {
            db.transactionDao().delete(transaction)
            transactions =
                transactions.filter { it.id != transaction.id } // keep all the transactions except the transaction with id of the swiped one
            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
                showSnackBar()

            }
        }


    }

    private fun showSnackBar() {
        val view: View = findViewById(R.id.coordinator)
        val snackbar: Snackbar = Snackbar.make(view, "Transaction Deleted!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete() // this is a user defined method defied below
        }.setActionTextColor(ContextCompat.getColor(this, R.color.moneyred))
            .setTextColor(ContextCompat.getColor(this, R.color.white)).show()
    }

    fun undoDelete(){
        GlobalScope.launch {
            db.transactionDao().insertAll(deletedTransaction)
            transactions=oldTransactions
            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
            }

        }





    }

    private fun fetchAll() {

        GlobalScope.launch {
            // code inside this will run in the background thread
            transactions = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }


        }
    }

    // function to add budget manager functionality to the app

    fun updateDashboard(): Unit {

        val totalamount: Double = transactions.map { it.amount }.sum()
        val totalbudget: Double = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        val totalexpenses: Double = Math.abs(totalamount - totalbudget)


        // updating the mapped values into the view

        txtrupees.text = "₹ ${totalamount}"
        txtbudgetrupees.text = "₹ ${totalbudget}"
        txtexpenserupees.text = "- ₹ ${totalexpenses}"


    }
}