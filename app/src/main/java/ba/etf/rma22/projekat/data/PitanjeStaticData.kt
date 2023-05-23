package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeAnketa
import ba.etf.rma22.projekat.data.repositories.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

public suspend fun getpitanja(idAnkete : Int): List<Pitanje> {
    return withContext(Dispatchers.IO) {

        val pitanja = arrayListOf<Pitanje>()
        val url1 = ApiConfig.baseURL+"/anketa/$idAnkete/pitanja" //1
        val url = URL(url1) //2
        (url.openConnection() as? HttpURLConnection)?.run { //3
            val result = this.inputStream.bufferedReader().use { it.readText() } //4 //5
            val results = JSONArray(result)
            for (i in 0 until results.length()) {//7
                val pitanje = results.getJSONObject(i)
                val id=pitanje.getInt("id")
                val naziv = pitanje.getString("naziv")
                val tekstPitanja = pitanje.getString("tekstPitanja")
                val opcije=pitanje.getJSONArray("opcije")
                val p=pitanje.getJSONObject("PitanjeAnketa")
                val anketumid=p.getInt("AnketumId")
                var o:ArrayList<String> = arrayListOf()
                for(i in 0 until opcije.length()){
                    o.add(opcije[i] as String)
                }
                pitanja.add(Pitanje(id,naziv,tekstPitanja,o,anketumid))

            }
        }
        return@withContext pitanja

    }

}

