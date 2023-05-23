package ba.etf.rma22.projekat

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.repositories.*
import ba.etf.rma22.projekat.data.repositories.AnketaRepository.setDB
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository.getOdgovoriAnketa
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.getPoceteAnkete
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.zapocniAnketu
import ba.etf.rma22.projekat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

var pitanjaIdevi = arrayListOf<Int>()
var odgovorKojeTrebaPoslatiSaPredaj = arrayListOf<Int>()
var trenutniProgres:Float=0f
var brojDoSadOdgovorenih:Int =0
var brojPitanja:Int =0
class MainActivity : AppCompatActivity(){
    private lateinit var viewPager : ViewPager2
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
   lateinit var adapter: ViewPagerAdapter
     fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    private var context: Context? = null

    fun context(): Context? {
        return context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trenutniProgres=0f
        brojDoSadOdgovorenih=0
        brojPitanja=0
  setDB(applicationContext,isOnline(this@MainActivity))
        IstrazivanjeRepository.setDB(applicationContext,isOnline(this@MainActivity))
        IstrazivanjeIGrupaRepository.setDB(applicationContext,isOnline(this@MainActivity))
        OdgovorRepository.setDB(applicationContext,isOnline(this@MainActivity))
        PitanjeAnketaRepository.setDB(applicationContext,isOnline(this@MainActivity))
        TakeAnketaRepository.setDB(applicationContext,isOnline(this@MainActivity))
        GrupaRepository.setDB(applicationContext,isOnline(this@MainActivity))
        AccountRepository.setDB(applicationContext,isOnline(this@MainActivity))
        if(intent.hasExtra("payload")){
scope.launch {
    intent.getStringExtra("payload")?.let { AccountRepository.postaviHash(it) }
}
        }
if(intent.hasExtra("id")){
    viewPager = findViewById(R.id.pager)
    var f1 = FragmentPredaj()
    var fragmenti : MutableList<Fragment> = mutableListOf()
    var id =intent.getStringExtra("id")
    var pitanja : List<Pitanje> = arrayListOf()
    var ac=this

    var odgovori= listOf<Odgovor>()
        scope.launch {
           var aT= getPoceteAnkete()?.findLast { a->a.AnketumId== id?.toInt() }
          if(aT==null)
            aT = id?.toInt()?.let { zapocniAnketu(it) }
            else odgovori=getOdgovoriAnketa(aT.AnketumId)
            trenutniProgres= aT?.progres!!
            if (id != null) {
                pitanja = PitanjeAnketaRepository.getPitanja(id.toInt())
            }
            if (aT != null) {
                f1.arguments = Bundle().apply {
            putString("idAnkete", aT?.id.toString())
        }
                brojPitanja=pitanja.size
                if (pitanja != null) {
                    for (i in pitanja.indices) {
                        var f = FragmentPitanje()
                        f.arguments = Bundle().apply {
                            putString("idAnkete",id)
                            putInt("id", pitanja[i].id)
                           // putInt("broj_pitanja", i)
                            putString("naziv", pitanja[i].naziv)
                            putString("tekst", pitanja[i].tekstPitanja)
                            putStringArrayList("opcije", pitanja[i].opcije)
                            putString("idAT", aT!!.id.toString())
                            if(odgovori.getOrNull(i)!=null){
                                putInt("odgovoreno",odgovori.get(i).odgovoreno)
                                brojDoSadOdgovorenih++
                            }
                        }
                        fragmenti.add(f)
                    }
                }
            }
             fragmenti.add(f1)
            adapter = ViewPagerAdapter(fragmenti as ArrayList<Fragment>, ac)
            viewPager.setOffscreenPageLimit(fragmenti.size/2 +1);
            viewPager.setAdapter(adapter);
        }
    }
        else{

    viewPager = findViewById(R.id.pager)
    var br: Int
    br = 0
    var i1 : Fragment =FragmentIstrazivanje()
    i1.arguments=Bundle().apply {
        putBoolean("connection",isOnline(this@MainActivity))
    }
    var i2:Fragment=FragmentAnkete()
    i2.arguments=Bundle().apply {putBoolean("connection",isOnline(this@MainActivity)) }
    var fragmenti: MutableList<Fragment> = mutableListOf(i2, i1)

    adapter = ViewPagerAdapter(fragmenti as ArrayList<Fragment>, this)
    viewPager.adapter = adapter
    viewPager.currentItem = br
    if (intent.hasExtra("id11")) {
        br = 1
        var f = FragmentPoruka()
        // println(intent.getStringExtra("id11"))
        f.arguments = Bundle().apply {
            putString("id11", intent.getStringExtra("id11"))
        }
        adapter.refreshFragment(1, f)

        viewPager.adapter = adapter
        viewPager.currentItem = br

    }
}
    }


}
