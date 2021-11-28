package com.syed.budgettracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var label: String,
    var amount: Double,
    var description: String?
) : Serializable{

}
