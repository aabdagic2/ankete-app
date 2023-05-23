package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje

@Dao
interface IstrazivanjeDao {
    @Query("SELECT * FROM istrazivanje")
    suspend fun getAll(): List<Istrazivanje>

    @Query("SELECT * FROM istrazivanje WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): Istrazivanje

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg ankete: Istrazivanje)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<Istrazivanje>)
}