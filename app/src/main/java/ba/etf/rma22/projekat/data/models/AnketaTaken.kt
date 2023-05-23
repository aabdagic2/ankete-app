package ba.etf.rma22.projekat.data.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*
@Entity(
    tableName ="AnketaTaken",foreignKeys= arrayOf(
            ForeignKey(entity = Anketa::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("AnketumId")))
)

data class AnketaTaken (
    @PrimaryKey @SerializedName("id") var id: Int,
    @ColumnInfo(name="student") @SerializedName("student")var student:String,
    @ColumnInfo(name="datumRada")@TypeConverters(Converters::class)@SerializedName("student")var datumRada: Date,
    @ColumnInfo(name="progres")@SerializedName("progres")var progres:Float,
    @ColumnInfo(name=("AnketumId"))var AnketumId:Int)