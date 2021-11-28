package com.syed.budgettracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syed.budgettracker.model.Transaction

@Database(entities = arrayOf(Transaction::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}