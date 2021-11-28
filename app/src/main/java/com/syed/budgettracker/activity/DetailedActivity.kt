package com.syed.budgettracker.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

class DetailedActivity : AppCompatActivity() {


    private lateinit var lladdtransactions: LinearLayout
    private lateinit var labellayout : TextInputLayout
    private lateinit var labelinput : TextInputEditText
    private lateinit var amountlayout : TextInputLayout
    private lateinit var amountinput : TextInputEditText
    private lateinit var descriptionlayout : TextInputLayout
    private lateinit var descriptioninput : TextInputEditText
    private lateinit var updatetransaction : Button
    private lateinit var closebutton: ImageButton
    private lateinit var rootcoorlayout: ConstraintLayout
    private lateinit var transaction: Transaction
    private lateinit var txtTip :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        lladdtransactions=findViewById(R.id.lladdtransactions)
        labellayout=findViewById(R.id.labellayout)
        labelinput=findViewById(R.id.labelinput)
        amountlayout=findViewById(R.id.amountlayout)
        amountinput=findViewById(R.id.amountinput)
        descriptioninput=findViewById(R.id.descriptioninput)
        descriptionlayout=findViewById(R.id.descriptionlayout)
        updatetransaction=findViewById(R.id.updatetransaction)
        rootcoorlayout=findViewById(R.id.rootcoorlayout)
        closebutton=findViewById(R.id.closebutton)
        txtTip=findViewById(R.id.txtTip)

        // get the transaction from the onbindviewholder to here and force typecast it to Transation type
        transaction = intent.getSerializableExtra("transaction") as Transaction

        labelinput.setText(transaction.label)
        amountinput.setText(transaction.amount.toString()) // android studio will take care of text and double contingency!!
        descriptioninput.setText(transaction.description)


        rootcoorlayout.setOnClickListener {
            this.window.decorView.clearFocus()
            val coorclick =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            coorclick.hideSoftInputFromWindow(it.windowToken,0)

        }
//        txtTip.setOnClickListener {
//
//            intent= Intent(this@DetailedActivity,MainActivity::class.java)
//            startActivity(intent)
//        }
        labelinput.addTextChangedListener {
            updatetransaction.visibility= View.VISIBLE
            if(it!!.isEmpty()==false){
                labellayout.error=null

            }

        }
        amountinput.addTextChangedListener {
            updatetransaction.visibility= View.VISIBLE
            if(it!!.isEmpty()==false){
                amountlayout.error=null

            }

        }
        descriptioninput.addTextChangedListener {
            updatetransaction.visibility= View.VISIBLE


        }


        updatetransaction.setOnClickListener {
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
                val transaction= Transaction(transaction.id,labelref, amountref, descriptionref)
                update(transaction)
                Toast.makeText(this@DetailedActivity,"Transaction Updated Successfully",Toast.LENGTH_SHORT).show()
            }


        }
        closebutton.setOnClickListener {
            finish()

        }





    }


    private fun update(transaction: Transaction) //object of the data class
    {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()

        }
    }
}