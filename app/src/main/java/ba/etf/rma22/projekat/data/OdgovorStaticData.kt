package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import ba.etf.rma22.projekat.data.repositories.ApiConfig
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.getPoceteAnkete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

suspend fun getodgovorianketa(idAnkete:Int):List<Odgovor>
{
    return withContext(Dispatchers.IO) {
        var poceteAnkete = getpoceteankete()
        val odgovori = arrayListOf<Odgovor>()
      // println(poceteAnkete?.get(0)?.AnketumId.toString()+idAnkete)
        if (poceteAnkete != null) {
         // println(poceteAnkete?.get(0)?.AnketumId.toString()+idAnkete)
            if(poceteAnkete.any { a -> a.AnketumId == idAnkete }){
                println(poceteAnkete?.get(0)?.AnketumId.toString()+idAnkete)
                val url1 = ApiConfig.baseURL+"/student/${AccountRepository.getHash()}/anketataken/${poceteAnkete.filter { a->a.AnketumId==idAnkete }.first().id}/odgovori" //1
                println(poceteAnkete.filter { a->a.AnketumId==idAnkete }.first().id)
                val url = URL(url1) //2
                (url.openConnection() as? HttpURLConnection)?.run { //3
                    val result = this.inputStream.bufferedReader().use { it.readText() } //4
                    val jo = JSONArray(result)//5

                    for (i in 0 until jo.length()) {//7
                        val odgovor = jo.getJSONObject(i)
                        //val id=odgovor.getInt("id")
                        val odgovoreno = odgovor.getInt("odgovoreno")
                        val aid=odgovor.getInt("AnketaTakenId")
                        val p=odgovor.getInt("PitanjeId")
                        odgovori.add(Odgovor(aid,odgovoreno))

                    }
                }
                return@withContext odgovori

            }
        }
        return@withContext odgovori
    }
}
suspend fun getodgovoriat(idAnketaTaken: Int): ArrayList<Odgovor> {
    return withContext(Dispatchers.IO)
    {
        var url1=ApiConfig.baseURL+"/student/${AccountRepository.getHash()}/anketataken/$idAnketaTaken/odgovori"
        var urll=URL(url1)
        var brpitanja=0
        var brodgovorenih=0
        var x=ArrayList<Odgovor>()
        (urll.openConnection() as? HttpURLConnection)?.run{
            val result = this.inputStream.bufferedReader().use{it.readText()}
            val results = JSONArray(result)
           for (i in 0 until results.length()){
               val odgovor = results.getJSONObject(i)
               //val id=odgovor.getInt("id")
               val odgovoreno = odgovor.getInt("odgovoreno")
               val aid=odgovor.getInt("AnketaTakenId")
               val p=odgovor.getInt("PitanjeId")
               x.add(Odgovor(aid,odgovoreno))
           }
        }
        return@withContext x
    }

}
suspend fun postaviodgovoranketa(idAnketaTaken:Int,idPitanje:Int,odgovor:Int):Pair<Int,Int>{
    return withContext(Dispatchers.IO){
        var url1=ApiConfig.baseURL+"/student/${AccountRepository.getHash()}/anketataken/$idAnketaTaken/odgovori"
        var urll=URL(url1)
        var brpitanja=0
        var brodgovorenih=0
        (urll.openConnection() as? HttpURLConnection)?.run{
            val result = this.inputStream.bufferedReader().use{it.readText()}
            val results = JSONArray(result)
            brodgovorenih=results.length()
            }
         url1=ApiConfig.baseURL+"/anketa/${getPoceteAnkete()?.filter{ p->p.id==idAnketaTaken}?.first()?.AnketumId}/pitanja"
         urll=URL(url1)
        (urll.openConnection() as? HttpURLConnection)?.run{
            val result = this.inputStream.bufferedReader().use{it.readText()}
            val results = JSONArray(result)
            brpitanja=results.length()
        }
        brodgovorenih++;
        var progres:Int= (mRound(brodgovorenih.toFloat()/brpitanja,0.2)*100).toInt()
       //   println(progres.toString()+"###############3")
        val url = URL("https://rma22ws.herokuapp.com/student/${AccountRepository.getHash()}/anketataken/${idAnketaTaken}/odgovor")
        val http = url.openConnection() as HttpURLConnection
        http.requestMethod = "POST"
        http.doOutput = true
        http.setRequestProperty("Accept", "application/json")
        http.setRequestProperty("Content-Type", "application/json")

        val data = "{\n  \"odgovor\": $odgovor,\n  \"pitanje\": $idPitanje,\n  \"progres\": $progres\n}"
           // println(odgovor.toString()+idAnketaTaken)
        val out: ByteArray = data.toByteArray(StandardCharsets.UTF_8)

        val stream: OutputStream = http.outputStream
        stream.write(out)

      println(http.responseCode.toString() + " " + http.responseMessage)
        http.disconnect()
        return@withContext Pair<Int,Int>(progres,0)
    }
}
fun mRound(value: Float, factor: Double): Double {
    return Math.round(value / factor) * factor
}
