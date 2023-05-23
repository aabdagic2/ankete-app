package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.AnketaTaken

@Dao
interface AnketaTakenDao {
    @Query("SELECT * FROM anketataken")
    suspend fun getAll(): List<AnketaTaken>

    @Query("SELECT * FROM anketataken WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): AnketaTaken

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg ankete: AnketaTaken)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<AnketaTaken>)
}