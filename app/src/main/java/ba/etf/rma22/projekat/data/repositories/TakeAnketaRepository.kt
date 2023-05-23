package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.getpoceteankete
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.RMA22DB
import ba.etf.rma22.projekat.data.zapocnianketu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object TakeAnketaRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
  suspend fun zapocniAnketu(idAnkete:Int): AnketaTaken? {
      if(online){
          var x=zapocnianketu(idAnkete)
          if (x != null) {
              db.anketaTakenDao().insertAll(x)
          };
 return x}
  return null;
  }

    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }

   suspend fun getPoceteAnkete():List<AnketaTaken>?{
       if(!online){
           return db.anketaTakenDao().getAll()
       }
       var x=getpoceteankete()
       if (x != null) {
           for(i in x)
               db.anketaTakenDao().insertAll(i)
       }
     return x
}}