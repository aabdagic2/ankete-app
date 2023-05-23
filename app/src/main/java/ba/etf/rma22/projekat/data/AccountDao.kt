package ba.etf.rma22.projekat.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Account

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(anketa: Account)
    @Query("SELECT * FROM Account")
    suspend fun get(): List<Account>

    @Query("DELETE FROM account")
    suspend fun deleteaccounts()
}