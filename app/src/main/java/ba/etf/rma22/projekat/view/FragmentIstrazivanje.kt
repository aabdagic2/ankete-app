package ba.etf.rma22.projekat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.k
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository.upisiUGrupu

import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository.getIstrazivanjeByGodina
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentIstrazivanje : Fragment() {
    private lateinit var odabirGodina: Spinner
    private lateinit var odabirIstrazivanja: Spinner
    private lateinit var odabirGrupa:Spinner
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view: View = inflater.inflate(R.layout.fragment_istrazivanje, container, false)
        odabirGodina = view.findViewById(R.id.odabirGodina)
        odabirIstrazivanja = view.findViewById(R.id.odabirIstrazivanja)
        odabirGrupa = view.findViewById(R.id.odabirGrupa)
        val upisiMe = view.findViewById<Button>(R.id.dodajIstrazivanjeDugme)
      upisiMe.isEnabled = false

            val godine: MutableList<String> = mutableListOf("", "1", "2", "3", "4", "5")
            var ist: String = "";
            var god: Int = 0;
            var grupId: Int = 0
            var ii: MutableList<String> = mutableListOf()


                var adapter1 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, godine)
                val godine1: MutableList<String>

                odabirGodina.adapter = adapter1
               odabirGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val istrazivanja3 = parent.getItemAtPosition(position).toString()
                        var g = 0
                        if (istrazivanja3 != "")
                            g = parent.getItemAtPosition(position).toString().toInt()
                        var i = listOf<Istrazivanje>()
                        scope.launch {
                            if (g != 0) {
                                i = getIstrazivanjeByGodina(g)
                                println("${i.size}+++++++++++")
                            }
                            val i2 = i.iterator()
                            ii = mutableListOf<String>("")
                            while (i2.hasNext()) {
                                var x = i2.next().naziv
                                println(x)
                                ii.add(x)
                            }

                            println(ii.size.toString() + "$$$$$$$$$$")
                            var adapter2 = ArrayAdapter<String>(
                                view.context,
                                android.R.layout.simple_spinner_item,
                                ii
                            )
                            odabirIstrazivanja.adapter = adapter2
                        }
                        odabirIstrazivanja.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent1: AdapterView<*>,
                                    view: View,
                                    position1: Int,
                                    id1: Long
                                ) {
                                    var gg: MutableList<String> = mutableListOf("")
                                    var istrazivanje = Istrazivanje(0, "", 0)
                                    var grupa = listOf<Grupa>()
                                    if (parent1.getItemAtPosition(position1).toString() != "") {
                                        istrazivanje = i.first { a ->
                                            a.naziv == parent1.getItemAtPosition(position1)
                                                .toString()
                                        }
                                        scope.launch {
                                            grupa = GrupaRepository.getGrupeZaIstrazivanje(istrazivanje.id)
                                            println(grupa.size.toString() + "+++++++++++")
                                            var g1 = grupa.iterator()
                                            while (g1.hasNext()) {
                                                gg.add(g1.next().naziv)
                                            }
                                            var adapter3 = ArrayAdapter<String>(
                                                view.context,
                                                android.R.layout.simple_spinner_item,
                                                gg
                                            )
                                            odabirGrupa.adapter = adapter3
                                        }
                                    odabirGrupa.onItemSelectedListener =
                                        object : AdapterView.OnItemSelectedListener {
                                            override fun onItemSelected(
                                                parent2: AdapterView<*>,
                                                view: View,
                                                position2: Int,
                                                id1: Long
                                            ) {
                                                if (istrazivanja3 != "" && parent1.getItemAtPosition(
                                                        position1
                                                    ).toString() != "" && parent2.getItemAtPosition(
                                                        position2
                                                    ).toString() != ""
                                                ) {
                                                    upisiMe.isEnabled = arguments?.getBoolean("connection")==true
                                                    ist = parent1.getItemAtPosition(position1)
                                                        .toString()
                                                    god = g
                                                    grupId = grupa.first { a ->
                                                        a.naziv.equals(
                                                            parent2.getItemAtPosition(position2)
                                                                .toString()
                                                        )
                                                    }.id

                                                }
                                            }

                                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                                TODO("Not yet implemented")
                                            }
                                        }
                                }}

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }

                            }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }

                 if(arguments?.getBoolean("connection")==true){
            upisiMe.setOnClickListener {
                scope.launch {
                    upisiUGrupu(grupId)
                }
                val intent = Intent(view.context, MainActivity::class.java)
                intent.putExtra("id11", grupId.toString())
                startActivity(intent)
            }}
            return view
        }


}