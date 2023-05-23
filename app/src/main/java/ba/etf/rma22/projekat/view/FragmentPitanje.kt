package ba.etf.rma22.projekat.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.*
import ba.etf.rma22.projekat.data.*
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.repositories.AnketaRepository.getById
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository.getOdgovoriAnketa
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository.postaviOdgovorAnketa
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FragmentPitanje() : Fragment() {
   var zadnji: TextView? = null
private lateinit var textView1 : TextView
    lateinit var listView1 : ListView
   lateinit var v:View
    var stariProgres :Float =0f
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val args = arguments


        var view: View = inflater.inflate(R.layout.fragment_pitanje, container, false)
        textView1 = view.findViewById(R.id.tekstPitanja)
        if (args != null) {
            textView1.text = args.getString("tekst")
        }
        listView1 = view.findViewById(R.id.odgovoriLista)
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_list_item_1,
           args?.getStringArrayList("opcije") as MutableList<String>
        )
        listView1.adapter = adapter
        var odgovor: String? = null
        var anketa: Anketa?=null
        scope.launch {
            anketa = args?.getString("idAnkete")?.let { getById(it.toInt()) }
            var brojPitanja = anketa?.let { PitanjeAnketaRepository.getPitanja(it.id).size }
            stariProgres = anketa?.progres!!
            println(args.getString("idAT"))
                if(args.getString("idAnkete")?.let { getOdgovoriAnketa(it.toInt()).filter { a-> a.AnketaTakenId == args.getString("idAT")!!
                        .toInt() }.size } !=0){
                    args.getString("idAnkete")
                        ?.let { getOdgovoriAnketa(it.toInt()).first{ a->a.id.equals(args.getInt("idAT")) }.odgovoreno }
                        ?.let { listView1.setSelection(it) }
                    listView1.onItemClickListener=null
                }
                else {
                    listView1.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            val selectedFromList: String = listView1.getItemAtPosition(position).toString()

                            if (zadnji != null) {
                                (zadnji)?.setTextColor(Color.parseColor("#000000"))
                            }
                            (view as TextView).setTextColor(Color.parseColor("#0000FF"))
                            zadnji = view
                            if (brojPitanja != null) {
                                if (args != null) {
                                    if (!pitanjaIdevi.contains(args.getInt("id"))) {
                                        if (args != null) {
                                            pitanjaIdevi.add(args.getInt("id"))
                                            brojDoSadOdgovorenih++
                                        }
                                        odgovorKojeTrebaPoslatiSaPredaj.add(position)
                                    } else{
                                        odgovorKojeTrebaPoslatiSaPredaj.set(pitanjaIdevi.indexOf(args.getInt("id")),position)
                                    }
                                }

                                trenutniProgres = (brojOdgovorenih.toFloat() / brojPitanja.toFloat())
                            }

                        }}
            }



            v = view
            return view
        }

    override fun onResume() {
        super.onResume()
        val args = arguments

        var odgovor: String? = null

        var brojPitanja = args?.getInt("broj_pitanja")

        var anketa: Anketa?=null
        scope.launch {
            anketa = args?.getString("idAnkete")?.let { getById(it.toInt()) }
            var brojPitanja = anketa?.let { PitanjeAnketaRepository.getPitanja(it.id).size }
            stariProgres = anketa?.progres!!
            if (args != null) {
                println(args.getString("idAT"))
            }
            if (args != null) {
                if(args.getString("idAnkete")?.let { getOdgovoriAnketa(it.toInt()).filter { a-> a.id == args.getString("idAT")!!
                        .toInt() }.size } !=0){
                    args.getString("idAnkete")
                        ?.let { getOdgovoriAnketa(it.toInt()).first{ a->a.id.equals(args.getInt("idAT")) }.odgovoreno }
                        ?.let { listView1.setSelection(it) }
                    listView1.onItemClickListener=null
                } else{
                    listView1.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            val selectedFromList: String = listView1.getItemAtPosition(position).toString()

                            if (zadnji != null) {
                                (zadnji)?.setTextColor(Color.parseColor("#000000"))
                            }
                            (view as TextView).setTextColor(Color.parseColor("#0000FF"))
                            zadnji = view
                            if (brojPitanja != null) {
                                if (args != null) {
                                    if (!pitanjaIdevi.contains(args.getInt("id"))) {
                                        if (args != null) {
                                            pitanjaIdevi.add(args.getInt("id"))
                                            brojDoSadOdgovorenih++
                                        }
                                        odgovorKojeTrebaPoslatiSaPredaj.add(position)
                                    } else{
                                        odgovorKojeTrebaPoslatiSaPredaj.set(pitanjaIdevi.indexOf(args.getInt("id")),position)
                                    }
                                }

                                trenutniProgres = (brojOdgovorenih.toFloat() / brojPitanja.toFloat())
                            }

                        }}
            }
        }
    }}
