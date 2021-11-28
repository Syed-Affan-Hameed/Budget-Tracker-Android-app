package com.syed.budgettracker.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.syed.budgettracker.database.AppDatabase
import com.syed.budgettracker.R
import com.syed.budgettracker.model.Transaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var lladdtransactions: LinearLayout
    private lateinit var labellayout :TextInputLayout
    private lateinit var labelinput : TextInputEditText
    private lateinit var amountlayout :TextInputLayout
    private lateinit var amountinput : TextInputEditText
    private lateinit var descriptionlayout :TextInputLayout
    private lateinit var descriptioninput : TextInputEditText
    private lateinit var btnaddtransaction :Button
    private lateinit var closebutton: ImageButton
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        lladdtransactions=findViewById(R.id.lladdtransactions)
        labellayout=findViewById(R.id.labellayout)
        labelinput=findViewById(R.id.labelinput)
        amountlayout=findViewById(R.id.amountlayout)
        amountinput=findViewById(R.id.amountinput)
        descriptioninput=findViewById(R.id.descriptioninput)
        descriptionlayout=findViewById(R.id.descriptionlayout)
        btnaddtransaction=findViewById(R.id.btnaddtransaction)
        closebutton=findViewById(R.id.closebutton)
        constraintLayout=findViewById(R.id.constraintLayout)

        constraintLayout.setOnClickListener {
            this.window.decorView.clearFocus()
            val coorclick =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            coorclick.hideSoftInputFromWindow(it.windowToken,0)

        }


        labelinput.addTextChangedListener {

            if(it!!.isEmpty()==false){
                labellayout.error=null

            }

        }
        amountinput.addTextChangedListener {

            if(it!!.isEmpty()==false){
                amountlayout.error=null

            }

        }


        btnaddtransaction.setOnClickListener {
            val labelref :String =labelinput.text.toString()
            val descriptionref: String =descriptioninput.text.toString()
            val amountref:Double?=amountinput.text.toString().toDoubleOrNull()

            if(labelref.isEmpty()){
                labellayout.error="Please add a label"

            }
            else if(amountref==null){
                amountlayout.error="Please add a valid amount"
            }
            else{
                val transaction= Transaction(id=0,labelref, amountref, descriptionref)
                insert(transaction)
            }

        }
        closebutton.setOnClickListener {
            finish()

        }





    }


    private fun insert(transaction: Transaction) //object of the data class
    {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()

        }
    }
}