package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa

@Dao
interface GrupaDao {
    @Query("SELECT * FROM grupa")
    suspend fun getAll(): List<Grupa>

    @Query("SELECT * FROM grupa WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): Grupa

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertG(vararg ankete: Grupa)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<Grupa>)
}