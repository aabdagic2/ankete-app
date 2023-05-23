package ba.etf.rma22.projekat.data

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.UpisaneGrupe

@Dao
interface UpisaneGrupeDao {
    @Query("SELECT * FROM upisanegrupe")
    suspend fun getAll(): List<UpisaneGrupe>

    @Query("SELECT * FROM upisanegrupe WHERE id=:id LIMIT 1")
    suspend fun findById(id: Long): UpisaneGrupe

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertG(vararg ankete: UpisaneGrupe)

    @Transaction
    @Delete
    suspend fun deleteAll(cast: List<UpisaneGrupe>)
}