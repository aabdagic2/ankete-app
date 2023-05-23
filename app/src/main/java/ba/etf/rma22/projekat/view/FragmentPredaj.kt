package ba.etf.rma22.projekat.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import ba.etf.rma22.projekat.*
import ba.etf.rma22.projekat.data.brojOdgovorenih
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository.postaviOdgovorAnketa
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.getPoceteAnkete
import ba.etf.rma22.projekat.data.sveAnkete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

lateinit var progres1 : MutableLiveData<Int>
fun mRound(value: Float, factor: Double): Double {
    return Math.round(value / factor) * factor
}
class FragmentPredaj : Fragment() {
lateinit var progres: TextView
lateinit var predaj : Button
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_predaj,container,false)
        progres=view.findViewById(R.id.progresTekst)
        progres.text = ((brojDoSadOdgovorenih.toFloat()/ brojPitanja)*100f).toInt().toString() +"%"
        predaj=view.findViewById(R.id.dugmePredaj)
            predaj.setOnClickListener {
                scope.launch {
                    for(i in pitanjaIdevi.indices){

                        arguments?.getString("idAnkete")?.toInt()?.let { it1 -> pitanjaIdevi.getOrNull(i)
                            ?.let { it2 ->
                                odgovorKojeTrebaPoslatiSaPredaj.getOrNull(i)?.let { it3 ->
                                    postaviOdgovorAnketa(it1,
                                        it2, it3
                                    )
                                }
                            } }
                    }

                    pitanjaIdevi= arrayListOf<Int>()
                    odgovorKojeTrebaPoslatiSaPredaj= arrayListOf()
                    var  i= Intent(view?.context, MainActivity::class.java)
                    startActivity(i)}
            }
        return view
    }

    override fun onResume() {
        super.onResume()

            progres.text = ((brojDoSadOdgovorenih.toFloat()/ brojPitanja)*100f).toInt().toString() +"%"
        predaj= view?.findViewById(R.id.dugmePredaj)!!
        predaj.setOnClickListener {
            scope.launch {
                for(i in pitanjaIdevi.indices){

                    arguments?.getString("idAnkete")?.toInt()?.let { it1 -> pitanjaIdevi.getOrNull(i)
                        ?.let { it2 ->
                            odgovorKojeTrebaPoslatiSaPredaj.getOrNull(i)?.let { it3 ->
                                postaviOdgovorAnketa(it1,
                                    it2, it3
                                )
                            }
                        } }
                }

                pitanjaIdevi= arrayListOf<Int>()
                odgovorKojeTrebaPoslatiSaPredaj= arrayListOf()
                var  i= Intent(view?.context, MainActivity::class.java)
                startActivity(i)}
        } }
}