package ba.etf.rma22.projekat.data.models

import android.content.Context
import androidx.room.*
import ba.etf.rma22.projekat.data.*
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val FORMATTER = SimpleDateFormat("dd.MM.yyyy")
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }


}
@Database(entities = arrayOf(Anketa::class,AnketaTaken::class,Grupa::class,Istrazivanje::class,Odgovor::class,Pitanje::class,PitanjeAnketa::class,Account::class,UpisaneGrupe::class,UpisaneAnkete::class), version = 1)
@TypeConverters(Converterss::class,OcjeneTypeConverter::class)
abstract class RMA22DB : RoomDatabase() {
    abstract fun anketaDao(): AnketaDao
    abstract fun anketaTakenDao(): AnketaTakenDao
    abstract fun grupaDao(): GrupaDao
    abstract fun istrazivanjeDao(): IstrazivanjeDao
    abstract fun odgovorDao(): OdgovorDao
    abstract fun pitanjeDao(): PitanjeDao
    abstract fun accountDao() : AccountDao
    abstract fun upisaneGrupeDao():UpisaneGrupeDao
    abstract fun upisaneAnkete():UpisaneAnketeDao
    companion object {
        private var INSTANCE: RMA22DB? = null

        fun getInstance(context: Context): RMA22DB {
            if (INSTANCE == null) {
                synchronized(RMA22DB::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RMA22DB::class.java,
                "RMA22DB"
            ).build()
    }
}