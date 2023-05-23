package ba.etf.rma22.projekat.data.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity
data class Pitanje(
  @PrimaryKey @SerializedName("id") val id:Int,
  @ColumnInfo(name="naziv") @SerializedName("naziv")var naziv: String,
  @ColumnInfo(name="tekstPitanja") @SerializedName("tekstPitanja")var tekstPitanja: String,
  @ColumnInfo(name="opcije")@TypeConverters(OcjeneTypeConverter::class) @SerializedName("opcije")var opcije: ArrayList<String>,
  @ColumnInfo(name="AnketumId")@SerializedName("AnketumId")var AnketumId:Int)
class OcjeneTypeConverter{
  @TypeConverter
  fun fromString(value:String?):ArrayList<String>{
    val listType =object:TypeToken<ArrayList<String>>(){}.type
    return Gson().fromJson(value,listType)
  }
  @TypeConverter
  fun fromArrayList(list:ArrayList<String?>):String{
    return Gson().toJson(list)
  }
}