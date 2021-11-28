package com.syed.budgettracker.database

import androidx.room.*
import com.syed.budgettracker.model.Transaction

// We have got two methods in the room library which are very convenient :-) 1] Query Methods   2] Convenience methods   I am using both here
@Dao
interface TransactionDao {
    // Query method allows custom sql queries
    @Query("SELECT * From transactions")
    fun getAll(): List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction) // vararg enables the class to take in multiple arguments and make the associated identifier 'transaction' an array of those multiple parameters

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)



}