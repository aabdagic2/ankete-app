package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import ba.etf.rma22.projekat.data.repositories.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

suspend fun zapocnianketu(idAnkete:Int):AnketaTaken?{
    return withContext(Dispatchers.IO) {
        val url1 = ApiConfig.baseURL+"/student/${AccountRepository.getHash()}/anketa/$idAnkete" //1
        var url=URL(url1)
        var con=url.openConnection()
        var conn:HttpURLConnection= con as HttpURLConnection
        conn.requestMethod="POST"
        var a: AnketaTaken?=null
        conn.run{
            val result = this.inputStream.bufferedReader().use { it.readText() }
            val jo = JSONObject(result)
           // println(result)
            if(jo.has("id")){
            val id=jo.getInt("id")
            val idA=jo.getInt("AnketumId")
            val student=jo.getString("student")
            val progres=jo.getDouble("progres").toFloat()
            val datumRada=jo.getString("datumRada")
            val gp=datumRada.substring(0,4).toInt()
            val mp=datumRada.substring(5,7).toInt()
            val dp=datumRada.substring(8,10).toInt()
            val datpoc=Calendar.getInstance()
            datpoc.set(gp,mp-1,dp)
            a=AnketaTaken(id,student, datpoc.time,progres,idA)
        }}
        return@withContext a
    }
}
suspend fun getpoceteankete():List<AnketaTaken>?{
    return withContext(Dispatchers.IO) {
        var pitanja = arrayListOf<AnketaTaken>()
        val url1 = ApiConfig.baseURL+"/student/${AccountRepository.getHash()}/anketataken" //1
        val url = URL(url1) //2
        (url.openConnection() as? HttpURLConnection)?.run { //3
            val result = this.inputStream.bufferedReader().use { it.readText() } //4
            val jo = JSONArray(result)//5
            for (i in 0 until jo.length()) {//7
                val pitanje = jo.getJSONObject(i)
                val id = pitanje.getInt("id")
                val student = pitanje.getString("student")
                val progres = pitanje.getDouble("progres").toFloat()
                val datumRada = pitanje.getString("datumRada")
                val idA=pitanje.getInt("AnketumId")
                val gp = datumRada.substring(0, 4).toInt()
                val mp = datumRada.substring(5, 7).toInt()
                val dp = datumRada.substring(8, 10).toInt()
                val datpoc = Calendar.getInstance()
                datpoc.set(gp, mp - 1, dp)

                pitanja.add(AnketaTaken(id, student, datpoc.time, progres,idA))

            }
        }
        if(pitanja.size==0){
            return@withContext null}
        return@withContext pitanja

    }
}