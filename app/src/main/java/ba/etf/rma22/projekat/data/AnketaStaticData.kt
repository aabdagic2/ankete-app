package ba.etf.rma22.projekat.data


import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import ba.etf.rma22.projekat.data.repositories.ApiConfig
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
var sveAnkete = arrayListOf<Anketa>()
var brojOdgovorenih:Int=0
private val scope = CoroutineScope(Job() + Dispatchers.Main)
class Korisnik() {
    val ime: String="Neko";
    val prezime:String="Nekić";
    var upisanaIstrazivanja:MutableList<String> = mutableListOf()
    var proslagodina: String? =null;
init {
    scope.launch {
    var an= getupisane()
        var it=an.iterator()
        while (it.hasNext()){
    upisanaIstrazivanja.add(it.next().nazivIstrazivanja)}
}}
}
 var k= Korisnik();

suspend fun getupisane() : List<Anketa>{
    return withContext(Dispatchers.IO){
        var grupe= getupisanegrupe()
         var ankete = arrayListOf<Anketa>()
        for(element in grupe){
              var url1=ApiConfig.baseURL+"/grupa/${element.id}/ankete"
            var url=URL(url1)
            (url.openConnection()as? HttpURLConnection)?.run{
                val result = this.inputStream.bufferedReader().use { it.readText() } //4
                val results = JSONArray(result)//5
                for (i in 0 until results.length()) {//7
                    val anketa = results.getJSONObject(i)
                    val id = anketa.getInt("id")
                    val naziv = anketa.getString("naziv")
                    val datumPocetak = anketa.getString("datumPocetak")
                    val trajanje = anketa.getInt("trajanje")
                    val gp=datumPocetak.substring(0,4).toInt()
                    val mp=datumPocetak.substring(5,7).toInt()
                    val dp=datumPocetak.substring(8,10).toInt()
                    //println(naziv)
                    val datpoc=Calendar.getInstance()
                    datpoc.set(gp,mp-1,dp,0,0,0)
                    val datumKraj=Calendar.getInstance()
                    datumKraj.set(gp,mp-1,dp,0,0,0)
                    datumKraj.add(Calendar.DATE,trajanje)
                    var nIstrazivanje=""
                    var urll2=ApiConfig.baseURL+"/grupa/${element.id}/istrazivanje"
                    var url2=URL(urll2)
                    (url2.openConnection() as? HttpURLConnection)?.run {
                        var result=this.inputStream.bufferedReader().use { it.readText() }
                        var istrazivanje=JSONObject(result)
                        nIstrazivanje=istrazivanje.getString("naziv")
                    }
                    var a=Anketa(id, naziv, nIstrazivanje, datpoc.time, datumKraj.time , null, trajanje, "", 0f)
                    if(ankete.filter { i -> i.id==a.id&&i.naziv.equals(a.naziv) && i.nazivIstrazivanja.equals(a.nazivIstrazivanja) }
                            .isEmpty())
                        ankete.add(a)



                }
        }
    }
        return@withContext ankete
}}
suspend fun getbyid(id:Int):Anketa?{
    return withContext(Dispatchers.IO){
        var anketa= arrayListOf<Anketa>()
        var nazivIstrazivanja:String=""
        var url1=ApiConfig.baseURL+"/anketa/$id"
        val url=URL(url1)
        (url.openConnection()as? HttpURLConnection)?.run{
            val results=this.inputStream.bufferedReader().use { it.readText() }
            val anketa=JSONObject(results)
            if(anketa.has("message")){
                return@withContext null
            }
            val id = anketa.getInt("id")
            val naziv = anketa.getString("naziv")
            val datumPocetak = anketa.getString("datumPocetak")
            val trajanje = anketa.getInt("trajanje")
            val gp=datumPocetak.substring(0,4).toInt()
            val mp=datumPocetak.substring(5,7).toInt()
            val dp=datumPocetak.substring(8,10).toInt()
           // println(naziv)
            val datpoc=Calendar.getInstance()
            datpoc.set(gp,mp-1,dp,0,0,0)
            val datumKraj=Calendar.getInstance()
            datumKraj.set(gp,mp-1,dp,0,0,0)
            datumKraj.add(Calendar.DATE,trajanje)
            var istrazivanjaNaKojimaJeAnketa= arrayListOf<String>()
            var urll2=ApiConfig.baseURL+"/anketa/$id/grupa"
            var url2=URL(urll2)
            (url2.openConnection() as? HttpURLConnection)?.run {
                val result1 = this.inputStream.bufferedReader().use { it.readText() } //4
                val results1 = JSONArray(result1)//5
                    var grupa = results1.getJSONObject(0)
                    var urll3 = ApiConfig.baseURL + "/grupa/${grupa.getInt("id")}/istrazivanje"
                    var url3 = URL(urll3)
                    (url3.openConnection() as? HttpURLConnection)?.run {
                        val result2 = this.inputStream.bufferedReader().use { it.readText() } //4
                        val results2 = JSONObject(result2)//5
                         nazivIstrazivanja = results2.getString("naziv")

                }
            }
           return@withContext Anketa(id,naziv, nazivIstrazivanja, datpoc.time, datumKraj.time , null, trajanje, "", 0f)

        }
    }
}
suspend fun all1(offset: Int=-1):List<Anketa>{
    var ankete = arrayListOf<Anketa>()
    return withContext(Dispatchers.IO) {
        if(offset==-1){
            var off=1
            var br=0
            do{
                val url1 = ApiConfig.baseURL + "/anketa?offset=$off" //1
                val url = URL(url1) //2
                (url.openConnection() as? HttpURLConnection)?.run { //3
                    val result = this.inputStream.bufferedReader().use { it.readText() } //4
                    val results = JSONArray(result)//5
                    br=results.length()
                    for (i in 0 until results.length()) {//7
                        val anketa = results.getJSONObject(i)
                        val id = anketa.getInt("id")
                        val naziv = anketa.getString("naziv")
                        val datumPocetak = anketa.getString("datumPocetak")
                        val trajanje = anketa.getInt("trajanje")
                        val gp=datumPocetak.substring(0,4).toInt()
                        val mp=datumPocetak.substring(5,7).toInt()
                        val dp=datumPocetak.substring(8,10).toInt()
                        // println(naziv)
                        val datpoc=Calendar.getInstance()
                        datpoc.set(gp,mp-1,dp,0,0,0)
                        val datumKraj=Calendar.getInstance()
                        datumKraj.set(gp,mp-1,dp,0,0,0)
                        datumKraj.add(Calendar.DATE,trajanje)
                        var istrazivanjaNaKojimaJeAnketa= arrayListOf<String>()
                        var grupaAnketa= arrayListOf<String>()
                        var urll2=ApiConfig.baseURL+"/anketa/$id/grupa"
                        var url2=URL(urll2)
                        (url2.openConnection() as? HttpURLConnection)?.run{
                            val result1 = this.inputStream.bufferedReader().use { it.readText() } //4
                            val results1 = JSONArray(result1)//5
                            for(j in 0 until results1.length()){
                                var grupa=results1.getJSONObject(j)

                                var urll3=ApiConfig.baseURL+"/grupa/${grupa.getInt("id")}/istrazivanje"
                                var url3=URL(urll3)
                                (url3.openConnection() as? HttpURLConnection)?.run{
                                    val result2 = this.inputStream.bufferedReader().use { it.readText() } //4
                                    val results2 = JSONObject(result2)//5
                                    var i= results2
                                    var id=i.getInt("id")
                                    var naziv=i.getString("naziv")
                                    var godina=i.getInt("godina")
                                    if(!istrazivanjaNaKojimaJeAnketa.contains(naziv)){
                                        // println(naziv+grupa.getInt("id"))
                                        istrazivanjaNaKojimaJeAnketa.add(naziv)
                                    }

                                }
                            }
                        }
                        for(l in 0 until istrazivanjaNaKojimaJeAnketa.size){
                            ankete.add(Anketa(id, naziv, istrazivanjaNaKojimaJeAnketa[l], datpoc.time, datumKraj.time , null, trajanje, "", 0f))
                        }
                    }

                }
                off++;
            } while (br!=0)
        }
        else{
            val url1 = ApiConfig.baseURL + "/anketa?offset=$offset" //1
            val url = URL(url1) //2
            (url.openConnection() as? HttpURLConnection)?.run { //3
                val result = this.inputStream.bufferedReader().use { it.readText() } //4
                val results = JSONArray(result)//5
                for (i in 0 until results.length()) {//7
                    val anketa = results.getJSONObject(i)
                    val id = anketa.getInt("id")
                    val naziv = anketa.getString("naziv")
                    val datumPocetak = anketa.getString("datumPocetak")
                    val trajanje = anketa.getInt("trajanje")
                    val gp=datumPocetak.substring(0,4).toInt()
                    val mp=datumPocetak.substring(5,7).toInt()
                    val dp=datumPocetak.substring(8,10).toInt()
                    val datpoc=Calendar.getInstance()
                    datpoc.set(gp,mp-1,dp,0,0,0)
                    val datumKraj=Calendar.getInstance()
                    datumKraj.set(gp,mp-1,dp,0,0,0)
                    datumKraj.add(Calendar.DATE,trajanje)
                    var istrazivanjaNaKojimaJeAnketa= arrayListOf<String>()
                    var urll2=ApiConfig.baseURL+"/anketa/$id/grupa"
                    var url2=URL(urll2)
                    (url2.openConnection() as? HttpURLConnection)?.run{
                        val result1 = this.inputStream.bufferedReader().use { it.readText() } //4
                        val results1 = JSONArray(result1)//5
                        for(j in 0 until results1.length()){
                            var grupa=results1.getJSONObject(j)
                            // println(naziv+grupa.getString("naziv"))
                            var urll3=ApiConfig.baseURL+"/grupa/${grupa.getInt("id")}/istrazivanje"
                            var url3=URL(urll3)
                            (url3.openConnection() as? HttpURLConnection)?.run{
                                val result2 = this.inputStream.bufferedReader().use { it.readText() } //4
                                val results2 = JSONObject(result2)//5
                                var i= results2
                                var id=i.getInt("id")
                                var naziv=i.getString("naziv")
                                if(!istrazivanjaNaKojimaJeAnketa.contains(naziv)){
                                    // println()
                                    istrazivanjaNaKojimaJeAnketa.add(naziv)
                                }

                            }
                        }
                    }
                    for(l in 0 until istrazivanjaNaKojimaJeAnketa.size){
                        ankete.add(Anketa(id, naziv, istrazivanjaNaKojimaJeAnketa[l], datpoc.time, datumKraj.time , null, trajanje, "", 0f))
                    }
                }}
            return@withContext ankete

        }
        return@withContext ankete
    }
}
suspend fun allAnkete( offset:Int=-1): List<Anketa> {
    var ankete = arrayListOf<Anketa>()
    return withContext(Dispatchers.IO) {
        if(offset==-1){
             var off=1
            var br=0
            do{
            val url1 = ApiConfig.baseURL + "/anketa?offset=$off" //1
            val url = URL(url1) //2
            (url.openConnection() as? HttpURLConnection)?.run { //3
                val result = this.inputStream.bufferedReader().use { it.readText() } //4
                val results = JSONArray(result)//5
                br=results.length()
                for (i in 0 until results.length()) {//7
                    val anketa = results.getJSONObject(i)
                    val id = anketa.getInt("id")
                    val naziv = anketa.getString("naziv")
                    val datumPocetak = anketa.getString("datumPocetak")
                    val trajanje = anketa.getInt("trajanje")
                    val gp=datumPocetak.substring(0,4).toInt()
                    val mp=datumPocetak.substring(5,7).toInt()
                    val dp=datumPocetak.substring(8,10).toInt()
                   // println(naziv)
                    val datpoc=Calendar.getInstance()
                    datpoc.set(gp,mp-1,dp,0,0,0)
                    val datumKraj=Calendar.getInstance()
                    datumKraj.set(gp,mp-1,dp,0,0,0)
                    datumKraj.add(Calendar.DATE,trajanje)

                    ankete.add(Anketa(id, naziv, "", datpoc.time, datumKraj.time , null, trajanje, "", 0f))
}
                }


                off++;
            } while (br!=0)
        }
        else{
        val url1 = ApiConfig.baseURL + "/anketa?offset=$offset" //1
            val url = URL(url1) //2
            (url.openConnection() as? HttpURLConnection)?.run { //3
                val result = this.inputStream.bufferedReader().use { it.readText() } //4
                val results = JSONArray(result)//5
                for (i in 0 until results.length()) {//7
                    val anketa = results.getJSONObject(i)
                    val id = anketa.getInt("id")
                    val naziv = anketa.getString("naziv")
                    val datumPocetak = anketa.getString("datumPocetak")
                    val trajanje = anketa.getInt("trajanje")
                    val gp=datumPocetak.substring(0,4).toInt()
                    val mp=datumPocetak.substring(5,7).toInt()
                    val dp=datumPocetak.substring(8,10).toInt()
                    val datpoc=Calendar.getInstance()
                    datpoc.set(gp,mp-1,dp,0,0,0)
                    val datumKraj=Calendar.getInstance()
                    datumKraj.set(gp,mp-1,dp,0,0,0)
                    datumKraj.add(Calendar.DATE,trajanje)

                        ankete.add(Anketa(id, naziv, "", datpoc.time, datumKraj.time , null, trajanje, "", 0f))

                }}
            return@withContext ankete

        }
        return@withContext ankete
    }

    }
suspend fun doneAnkete(): List<Anketa> {
   var ankete= getupisane()
    var doneAnkete= arrayListOf<Anketa>()
    for(i in ankete.indices){
        if(OdgovorRepository.getOdgovoriAnketa(ankete[i].id).isNotEmpty()){
            doneAnkete.add(ankete[i])
        }
    }
    return doneAnkete
}

suspend fun futureAnkete(): List<Anketa> {
    return getupisane().filter{ a->(Date()<a.datumPocetak)}
}
suspend fun notTakenAnkete(): List<Anketa> {
    return getupisane().filter{ a->(Date()>(a.datumKraj))};
}