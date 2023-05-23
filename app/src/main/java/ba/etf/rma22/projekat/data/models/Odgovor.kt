package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Odgovor(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id:Int=0,
    @ColumnInfo(name="AnketaTakenId")@SerializedName("AnketaTakenId") var AnketaTakenId:Int,
    @ColumnInfo(name="odgovoreno") @SerializedName("odgovoreno") var odgovoreno:Int) {
    constructor(code: Int,odgovoreno: Int) : this(0, code,odgovoreno)
}