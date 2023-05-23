package ba.etf.rma22.projekat.data


import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import ba.etf.rma22.projekat.data.repositories.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.net.HttpURLConnection
import java.net.URL

public suspend fun upisiugrupu(idGrupa: Int):Boolean{
    return withContext(Dispatchers.IO){
        var url1 = ApiConfig.baseURL+"/grupa/$idGrupa/student/${AccountRepository.getHash()}"
        var url=URL(url1)
       var con=url.openConnection()
        var conn:HttpURLConnection= con as HttpURLConnection
        conn.requestMethod="POST"
        conn.run{
            val result=this.inputStream.bufferedReader().use { it.readText() }
           // println(result)
            val r=JSONObject(result)
            if(!r.getString("message").equals("Grupa not found.")){
                return@withContext TRUE
            }

        }
        return@withContext FALSE
    }
}
public suspend fun getgrupezaistrazivanje(idIstrazivanja:Int):List<Grupa>{
    return withContext(Dispatchers.IO){
        var url1 = ApiConfig.baseURL + "/istrazivanje/$idIstrazivanja"
        var url = URL(url1)
       lateinit var istrazivanje : Istrazivanje
        (url.openConnection() as? HttpURLConnection)?.run {
            val result = this.inputStream.bufferedReader().use { it.readText() }
            val results = JSONObject(result)//5
            val id = results.getInt("id")
            val naziv = results.getString("naziv")
            val godina = results.getInt("godina")
            istrazivanje = Istrazivanje(id, naziv, godina)}
        url1=ApiConfig.baseURL+"/grupa"
        url=URL(url1)
        var grupe = arrayListOf<Grupa>()
        (url.openConnection() as? HttpURLConnection)?.run {
            val result = this.inputStream.bufferedReader().use { it.readText() }
            val results = JSONArray(result)//5
            for (i in 0 until results.length()) {
                val anketa = results.getJSONObject(i)
                if(anketa.getString("IstrazivanjeId").equals(idIstrazivanja.toString())){
                val id = anketa.getInt("id")
                val naziv = anketa.getString("naziv")
                grupe.add(Grupa(id, naziv, istrazivanje.naziv))
            }
            }

        }
        return@withContext grupe
    }
}
public suspend fun getistrazivanja(offset:Int=-1): List<Istrazivanje> {
    return withContext(Dispatchers.IO) {
        if (offset == -1) {
            var istrazivanja = arrayListOf<Istrazivanje>()
            var i = 1
            var br=0
            do {
                var url1 = ApiConfig.baseURL + "/istrazivanje?offset=$i"
                var url = URL(url1)
                (url.openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                   var results = JSONArray(result)
                    br=results.length()
                    for (i in 0 until results.length()) {
                        val anketa = results.getJSONObject(i)
                        val id = anketa.getInt("id")
                        val naziv = anketa.getString("naziv")
                        val godina = anketa.getInt("godina")

                        istrazivanja.add(Istrazivanje(id,naziv, godina))
                    }
                }
                i++
            } while (br != 0)
            return@withContext istrazivanja
        } else {
            var istrazivanja = arrayListOf<Istrazivanje>()
            val url1 = ApiConfig.baseURL + "/istrazivanje?offset=$offset" //1
            val url = URL(url1) //2
            (url.openConnection() as? HttpURLConnection)?.run { //3
                val result = this.inputStream.bufferedReader().use { it.readText() } //4
                val results = JSONArray(result)//5
                for (i in 0 until results.length()) {
                    val anketa = results.getJSONObject(i)
                    val id = anketa.getInt("id")
                    val naziv = anketa.getString("naziv")
                    val godina = anketa.getInt("godina")

                    istrazivanja.add(Istrazivanje(id,naziv, godina))
                }
                return@withContext istrazivanja

            }
            return@withContext istrazivanja
        }
    }

}
public suspend fun  getgrupe():List<Grupa>{
    return withContext(Dispatchers.IO) {
        val url1 = ApiConfig.baseURL + "/grupa"
        val url = URL(url1)
        var grupe = arrayListOf<Grupa>()
        (url.openConnection() as? HttpURLConnection)?.run {
            val result = this.inputStream.bufferedReader().use { it.readText() }
            val results = JSONArray(result)//5
            for (i in 0 until results.length()) {
                val anketa = results.getJSONObject(i)
                val id = anketa.getInt("id")
                val naziv = anketa.getString("naziv")
                grupe.add(Grupa(id, naziv, ""))
            }

        }
        for (i in 0 until grupe.size) {
           var url1 = ApiConfig.baseURL + "/grupa/${grupe[i].id}/istrazivanje"
           var url = URL(url1)
            (url.openConnection() as? HttpURLConnection)?.run {
                val result = this.inputStream.bufferedReader().use { it.readText() }
                val results  =JSONObject(result)
                val naziv = results.getString("naziv")
                grupe[i].nazivIstrazivanja = naziv
            }


        }
        return@withContext grupe
    }

}
public suspend fun getupisanegrupe():List<Grupa>{
    return withContext(Dispatchers.IO) {
        var grupe = arrayListOf<Grupa>()
        var url1 = ApiConfig.baseURL + "/student/${AccountRepository.getHash()}/grupa"
        var url = URL(url1)
        (url.openConnection() as? HttpURLConnection)?.run {
            val result = this.inputStream.bufferedReader().use { it.readText() }
            val results = JSONArray(result)
            for (i in 0 until results.length()) {//7
                val grupa = results.getJSONObject(i)
                val id = grupa.getInt("id")
                val naziv = grupa.getString("naziv")
                grupe.add(Grupa(id, naziv, ""))
            }

        }
        for (i in 0 until grupe.size) {
            url1 = ApiConfig.baseURL + "/grupa/${grupe[i].id}/istrazivanje"
            url = URL(url1)
            (url.openConnection() as? HttpURLConnection)?.run {
                val result = this.inputStream.bufferedReader().use { it.readText() }
                val results  =JSONObject(result)
                    val naziv = results.getString("naziv")
                    grupe[i].nazivIstrazivanja = naziv
                }


        }
        return@withContext grupe
    }

}
public suspend fun getistrazivanjegodina(godina:Int): List<Istrazivanje> {
    return getistrazivanja().filter { n->n.godina==godina }
}
public suspend fun upisani(): List<Istrazivanje> {
    return getistrazivanja().filter { a-> k.upisanaIstrazivanja.contains(a.naziv) }
}