package ba.etf.rma22.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.getgrupe
import ba.etf.rma22.projekat.data.k
import ba.etf.rma22.projekat.data.models.Grupa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FragmentPoruka : Fragment() {
    private lateinit var poruka : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var scope= CoroutineScope(Job() + Dispatchers.Main)
        var view: View = inflater.inflate(R.layout.fragment_poruka, container, false)
        poruka=view.findViewById(R.id.tvPoruka)
        var args=arguments
        var grupa = Grupa(0,"","")
        scope.launch {

            grupa = getgrupe().first { i -> i.id == args?.getString("id11")?.toInt() }


            if (args != null) {
                poruka.text = "Uspješno ste upisani u\n" +
                        "grupu ${grupa.naziv} istraživanja ${grupa.nazivIstrazivanja}!"
            }
        }
        return view
    }
}