package ba.etf.rma22.projekat.data

import androidx.room.*
import androidx.room.OnConflictStrategy.*
import ba.etf.rma22.projekat.data.models.Anketa

@Dao
interface AnketaDao {
    @Query("SELECT * FROM ANKETA")
    suspend fun getAll(): List<Anketa>

    @Query("SELECT * FROM anketa WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): Anketa

    @Insert(onConflict =IGNORE)
    suspend fun insertAll(vararg ankete: Anketa)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<Anketa>)


}