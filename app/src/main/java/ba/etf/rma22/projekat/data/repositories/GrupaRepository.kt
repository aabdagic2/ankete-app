package ba.etf.rma22.projekat.data.repositories


import android.content.Context

import ba.etf.rma22.projekat.data.getgrupezaistrazivanje
import ba.etf.rma22.projekat.data.getistrazivanja
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.RMA22DB
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository.getGrupe
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository.getIstrazivanja

object GrupaRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
    suspend fun getGrupeZaIstrazivanje(idIstrazivanja:Int):List<Grupa>{
        if(online){
            getGrupe()
        return getgrupezaistrazivanje(idIstrazivanja)}

        else{

            var y= db.istrazivanjeDao().getAll().first { a -> a.id == idIstrazivanja }
            var x=db.grupaDao().getAll().filter { a->a.nazivIstrazivanja==y.naziv }
            return x
        }
    }

}