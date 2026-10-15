package com.gahejodev.smartexpense

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "transactions")
data class ExpenseTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Long,
    val type: String,
    val category: String,
    val note: String,
    val date: String
)

@Entity(tableName = "budgets")
data class MonthlyBudget(
    @PrimaryKey val month: String,
    val amount: Long
)

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun observeTransactions(): Flow<List<ExpenseTransaction>>

    @Insert
    suspend fun insertTransaction(transaction: ExpenseTransaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("SELECT * FROM budgets")
    fun observeBudgets(): Flow<List<MonthlyBudget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBudget(budget: MonthlyBudget)
}

@Database(entities = [ExpenseTransaction::class, MonthlyBudget::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun dao(): ExpenseDao
    companion object {
        @Volatile private var instance: ExpenseDatabase? = null
        fun get(context: Context): ExpenseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, ExpenseDatabase::class.java, "expense-db").build().also { instance = it }
            }
    }
}
