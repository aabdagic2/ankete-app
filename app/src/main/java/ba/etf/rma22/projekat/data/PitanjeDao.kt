package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Pitanje
@Dao
interface PitanjeDao {
    @Query("SELECT * FROM pitanje")
    suspend fun getAll(): List<Pitanje>

    @Query("SELECT * FROM pitanje WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): Pitanje

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg pitanja: Pitanje)

    @Delete
    suspend fun delete(pitanje: Pitanje)
}