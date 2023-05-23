package ba.etf.rma22.projekat.data.repositories



import android.content.Context
import ba.etf.rma22.projekat.data.getistrazivanja
import ba.etf.rma22.projekat.data.getistrazivanjegodina
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.RMA22DB
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository.getIstrazivanja
import ba.etf.rma22.projekat.data.upisani

object IstrazivanjeRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
    public suspend fun  getIstrazivanjeByGodina(godina:Int) : List<Istrazivanje>{
        if(online){
            getIstrazivanja()
        return getistrazivanjegodina(godina)}
        return db.istrazivanjeDao().getAll().filter {a->a.godina==godina}
    }


    suspend fun getAll() : List<Istrazivanje>{
        if(online){
            var x=getistrazivanja()
            for(i in x)
            db.istrazivanjeDao().insertAll(i)
        return x;}
        return db.istrazivanjeDao().getAll()
    }


    suspend fun getUpisani() : List<Istrazivanje>{
        return upisani();
    }


}