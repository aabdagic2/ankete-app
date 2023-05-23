package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.models.Account
import ba.etf.rma22.projekat.data.models.RMA22DB

object AccountRepository {
    lateinit var db: RMA22DB
    var online:Boolean = false
 var acHash:String="c7a060af-3862-4d99-908c-b7c996a33ef6"
    fun setDB(c: Context, o:Boolean){
        this.online =o
        this.db = RMA22DB.getInstance(c)
    }
  suspend fun postaviHash(acHash:String):Boolean{
      db.accountDao().deleteaccounts()
      this.acHash=acHash
      db.accountDao().insert(Account(acHash))
       return true
    }
   suspend fun getHash():String{
       if(db.accountDao().get().isEmpty())
      return acHash
      return db.accountDao().get().first().acHash
    }

}