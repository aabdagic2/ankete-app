package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UpisaneGrupe(@PrimaryKey @SerializedName("id") val id:Int,
                        @ColumnInfo(name="naziv") @SerializedName("naziv")var naziv:String,
                        @ColumnInfo(name="nazivIstrazivanja") @SerializedName("nazivIstrazivanja")var nazivIstrazivanja:String)