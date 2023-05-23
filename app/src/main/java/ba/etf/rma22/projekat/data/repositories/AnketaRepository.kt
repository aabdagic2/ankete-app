package ba.etf.rma22.projekat.data.repositories

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.*
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository.getUpisaneGrupe
import java.util.*
import kotlin.collections.ArrayList


object AnketaRepository {

lateinit var db: RMA22DB
var online:Boolean = false
    suspend fun getUpisane(): List<Anketa>{
         if(!online){
var x=db.upisaneAnkete().getAll()
             var y=ArrayList<Anketa>()
             for(i in x){
                 y.add(Anketa(i.id,i.naziv,i.nazivIstrazivanja,i.datumPocetak,i.datumKraj,i.datumRada,i.trajanje,i.nazivGrupe,i.progres))
             }
             return y
        } else {
             var d=getupisane()
             for(i in d){
                 db.upisaneAnkete().insertAll(UpisaneAnkete(i.id,i.naziv,i.nazivIstrazivanja,i.datumPocetak,i.datumKraj,i.datumRada,i.trajanje,i.nazivGrupe,i.progres))

             }
            return d
        }
    }
fun setDB(c:Context,o:Boolean){
    online=o
    db= RMA22DB.getInstance(c)
}
suspend fun getById(id:Int):Anketa? {
    if (!online) {
     return db.anketaDao().findById(id.toLong())
    } else {
        return getbyid(id);
    }
}
   suspend fun getDone(): List<Anketa> {
       if (!online) {
           var ankete= getUpisane()
           var doneAnkete= arrayListOf<Anketa>()
           for(i in ankete.indices){
               if(OdgovorRepository.getOdgovoriAnketa(ankete[i].id).isNotEmpty()){
                   doneAnkete.add(ankete[i])
               }
           }
           return doneAnkete
       } else {
           return doneAnkete();
       }
   }

    suspend fun getFuture(): List<Anketa> {
        if (!online) {
            return getUpisane().filter{ a->(Date()<a.datumPocetak)}
        } else {
            return futureAnkete();
        }
    }

    suspend fun getNotTaken(): List<Anketa> {
        if (!online){
            return getUpisane().filter{ a->(Date()>(a.datumKraj))};
        }
        else{
        return notTakenAnkete();
    }}
    suspend fun all(offset:Int=-1):List<Anketa>{
        return if(!online){ db.anketaDao().getAll()}
        else{ var sve=all1(offset)

            for(s in sve){
                db.anketaDao().insertAll(s)}
            sve
        }
    }
   suspend fun getAll(offset:Int=-1):List<Anketa>{
       return if(!online){
           db.anketaDao().getAll()
       } else{
           var sve= allAnkete(offset)
           for(s in sve)
               db.anketaDao().insertAll(s)
           sve
       }
    }


}