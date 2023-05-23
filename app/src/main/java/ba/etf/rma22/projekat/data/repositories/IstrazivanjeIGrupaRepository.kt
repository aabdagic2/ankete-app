package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.*
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.RMA22DB
import ba.etf.rma22.projekat.data.models.UpisaneGrupe

object IstrazivanjeIGrupaRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
    suspend fun getUpisaneGrupe():List<Grupa>{
        if(online){
            var x=getupisanegrupe()

            for(i in x) db.upisaneGrupeDao().insertG(UpisaneGrupe(i.id,i.naziv,i.nazivIstrazivanja))
      return  x}
        var x=db.upisaneGrupeDao().getAll()
        var y=ArrayList<Grupa>()
        for (i in x){
            y.add(Grupa(i.id,i.naziv,i.nazivIstrazivanja))
        }
        return y
    }
    suspend fun getIstrazivanja(offset:Int=-1):List<Istrazivanje>{
        if(online){
      var x=getistrazivanja(offset)
            for(i in x)
                db.istrazivanjeDao().insertAll(i)
        return x
        }
        return db.istrazivanjeDao().getAll()
    }
    suspend fun getGrupe():List<Grupa>{
        if(online){
            var x= getgrupe()
            for(i in x)
                db.grupaDao().insertG(i)
            return x
        }
        return db.grupaDao().getAll()
    }
    suspend fun getGrupeZaIstrazivanje(idIstrazivanja:Int):List<Grupa>{
        return getgrupezaistrazivanje(idIstrazivanja)
    }
    suspend fun upisiUGrupu(idGrupa:Int):Boolean{
        if(online)
        return upisiugrupu(idGrupa)
        return false
    }

}