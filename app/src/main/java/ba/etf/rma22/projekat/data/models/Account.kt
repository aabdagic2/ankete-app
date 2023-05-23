package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Account(@PrimaryKey(autoGenerate = true) @SerializedName("id") var id:Int=0,
  @ColumnInfo(name="acHash") @SerializedName("acHash")var acHash:String){
  constructor(code: String) : this(0, code)
}