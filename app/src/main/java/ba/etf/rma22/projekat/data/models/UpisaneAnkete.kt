package ba.etf.rma22.projekat.data.models
import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class Converterss {
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
@Entity
public data class UpisaneAnkete(
    @PrimaryKey @SerializedName("id") var id:Int,
    @ColumnInfo(name="naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "nazivIstrazivanja") @SerializedName("nazivIstrazivanja") var nazivIstrazivanja: String,
    @ColumnInfo(name="datumPocetak")@TypeConverters(Converters::class)@SerializedName("datumPocetak")var datumPocetak: Date,
    @ColumnInfo(name="datumKraj") @TypeConverters(Converters::class)@SerializedName("datumKraj") var datumKraj: Date,
    @ColumnInfo(name="datumRada") @TypeConverters(Converters::class)@SerializedName("datumRada") var datumRada: Date?,
    @ColumnInfo(name="trajanje") @SerializedName("trajanje")var trajanje: Int,
    @ColumnInfo(name="nazivGrupe") @SerializedName("nazivGrupe")var nazivGrupe: String,
    @ColumnInfo(name="progres") @SerializedName("progres") var progres: Float
)