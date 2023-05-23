package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.getodgovorianketa
import ba.etf.rma22.projekat.data.getodgovoriat
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.RMA22DB
import ba.etf.rma22.projekat.data.postaviodgovoranketa
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.getPoceteAnkete

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object OdgovorRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
    suspend fun getOdgovoriAnketa(idAnkete:Int):List<Odgovor>{
        return if(online){

            var x=getodgovorianketa(idAnkete)
            for(i in x)
                db.odgovorDao().insertAll(i)
            x
        } else{
            var x=  db.anketaTakenDao().getAll().filter { a->a.AnketumId==idAnkete }.first()
            db.odgovorDao().getAll().filter { a->a.id==x.id }
        }

    }
    suspend fun postaviOdgovorAnketa(idAnketaTaken:Int,idPitanje:Int,odgovor:Int):Int{
        if(online) {
            var x= getodgovoriat(idAnketaTaken)
          var y=postaviodgovoranketa(idAnketaTaken, idPitanje, odgovor).first
           var x1= getodgovoriat(idAnketaTaken)
            for(i in x1){
                if(x.filter { a->a.AnketaTakenId==i.AnketaTakenId}.isEmpty()){
                    db.odgovorDao().insertAll(i)
                }
            }
            return y
        }
        return -1
}}