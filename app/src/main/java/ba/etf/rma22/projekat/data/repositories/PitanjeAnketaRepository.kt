package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.getpitanja
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.RMA22DB


object PitanjeAnketaRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
    suspend fun getPitanja(idAnkete:Int):List<Pitanje> {
        if(!online) {
            var i = db.pitanjeDao().getAll()
            return i.filter { a -> a.AnketumId == idAnkete };
        }
        var x=getpitanja(idAnkete)
        for(i in x){
            db.pitanjeDao().insertAll(i)
        }
    return x}
}