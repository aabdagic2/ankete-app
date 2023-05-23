package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Odgovor

@Dao
interface OdgovorDao {
    @Query("SELECT * FROM odgovor")
    suspend fun getAll(): List<Odgovor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg odgovor: Odgovor)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<Odgovor>)
}