package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.UpisaneAnkete

@Dao
interface UpisaneAnketeDao {
    @Query("SELECT * FROM upisaneankete")
    suspend fun getAll(): List<UpisaneAnkete>

    @Query("SELECT * FROM upisaneankete WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): UpisaneAnkete

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg ankete: UpisaneAnkete)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<UpisaneAnkete>)
}