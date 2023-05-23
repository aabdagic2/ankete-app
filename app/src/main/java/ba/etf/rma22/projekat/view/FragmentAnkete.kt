package ba.etf.rma22.projekat.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.getpoceteankete
import ba.etf.rma22.projekat.data.getupisane
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.repositories.AnketaRepository.getUpisane
import ba.etf.rma22.projekat.view.AnketaListAdapter
import ba.etf.rma22.projekat.viewmodel.AnketaListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.util.*

class FragmentAnkete : Fragment() {
    private lateinit var listaAnketa: RecyclerView;
    private lateinit var listaAnketaAdapter: AnketaListAdapter
    private var anketaListViewModel = AnketaListViewModel()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_ankete, container, false)
        super.onCreate(savedInstanceState)

        val filteri: List<String> = listOf(
            "Sve ankete",
            "Sve moje ankete",
            "Urađene ankete",
            "Buduće ankete",
            "Prošle ankete"
        )


        val spinner = view.findViewById<Spinner>(R.id.filterAnketa)

            if (spinner != null) {
                val adapter =
                    ArrayAdapter(view.context, android.R.layout.simple_spinner_item, filteri)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        if (filteri[position] == "Sve moje ankete") {
                            scope.launch {
                                listaAnketaAdapter.updateAnkete(anketaListViewModel.getMyAnkete())
                            }
                        } else if (filteri[position] == "Sve ankete") {
                            scope.launch {
                                listaAnketaAdapter.updateAnkete(anketaListViewModel.getAllAnkete(1))
                            }
                        } else if (filteri[position] == "Urađene ankete") {
                            scope.launch {
                                listaAnketaAdapter.updateAnkete(anketaListViewModel.getDoneAnkete())
                            }
                        } else if (filteri[position] == "Buduće ankete") {
                            scope.launch {
                                listaAnketaAdapter.updateAnkete(anketaListViewModel.getFuture())
                            }
                        } else {
                            scope.launch {
                                listaAnketaAdapter.updateAnkete(anketaListViewModel.getNotTakenAnkete())
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
            }

            listaAnketa = view.findViewById(R.id.listaAnketa)
            listaAnketa.setLayoutManager(
                GridLayoutManager(
                    view.context,
                    2,
                    GridLayoutManager.VERTICAL,
                    false
                )
            )
            val dividerItemDecoration =
                DividerItemDecoration(listaAnketa.context, GridLayoutManager.VERTICAL)
            listaAnketa.addItemDecoration(dividerItemDecoration)
            listaAnketaAdapter = AnketaListAdapter(arrayListOf()) { movie -> intentAnketa(movie) }
            listaAnketa.adapter = listaAnketaAdapter


            return view
        }

        private fun intentAnketa(anketa: Anketa) {
            scope.launch {
                if (anketa.datumPocetak <= Date() && getUpisane().filter { a -> a.id.equals(anketa.id) }
                        .isNotEmpty()) {
                    val intent = Intent(view?.context, MainActivity::class.java)
                    intent.putExtra("id", anketa.id.toString())
                    startActivity(intent)
                }
            }
        }

}