package ba.etf.rma22.projekat.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository.getPoceteAnkete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AnketaListAdapter (
    private var ankete: List<Anketa>,
    private val onItemClicked: (anketa :Anketa) -> Unit) : RecyclerView.Adapter<AnketaListAdapter.AnketaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnketaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anketa, parent, false)
        return AnketaViewHolder(view)
    }
   private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun getItemCount(): Int = ankete.size
    private val  formatter: DateFormat=  SimpleDateFormat("dd.MM.yyyy");
    override fun onBindViewHolder(holder: AnketaViewHolder, position: Int) {
        holder.nazivAnkete.text = ankete[position].naziv;
        holder.nazivIstraživanja.text = ankete[position].nazivIstrazivanja;
        val context: Context =holder.statusAnketeImage.getContext()
        scope.launch {
        if(getPoceteAnkete()?.filter { a->a.datumRada!=null&&a.AnketumId==ankete[position].id }?.size!=0&&getPoceteAnkete()?.filter { a->a.datumRada!=null&&a.AnketumId==ankete[position].id }?.first()?.progres==100f){
            holder.datumAnkete.text="Anketa urađena: "+ formatter.format(getPoceteAnkete()?.filter { a->a.datumRada!=null&&a.AnketumId==ankete[position].id }
                ?.first()?.datumRada?.time ).toString()
            holder.statusAnketeImage.setImageResource(R.drawable.plava)}
        else if(Date()>ankete[position].datumKraj){holder.datumAnkete.text="Anketa zatvorena: "+formatter.format(ankete[position].datumKraj.time).toString()
            holder.statusAnketeImage.setImageResource(R.drawable.crvena)}
        else if(Date()<ankete[position].datumPocetak){holder.datumAnkete.text="Vrijeme aktiviranja: "+formatter.format(ankete[position].datumPocetak.time).toString()
            holder.statusAnketeImage.setImageResource(R.drawable.zuta)
        }
        else{
            holder.datumAnkete.text="Vrijeme zatvaranja: "+formatter.format(ankete[position].datumKraj.time).toString()
            holder.statusAnketeImage.setImageResource(R.drawable.zelena)
        }
        fun mRound(value: Float, factor: Double): Double {
            return Math.round(value / factor) * factor
        }}
        scope.launch {
            getPoceteAnkete()?.forEach { a -> println(a.AnketumId.toString() + ankete[position].id) }
            // print( getPoceteAnkete()?.findLast { a->a.AnketumId==ankete[position].id }?.progres!!)
            if (getPoceteAnkete()?.filter { a->a.AnketumId==ankete[position].id }!=null) {
                if (getPoceteAnkete()?.filter { a -> a.AnketumId == ankete[position].id }?.size != 0){
                    holder.progres.setProgress(getPoceteAnkete()?.filter { a -> a.AnketumId == ankete[position].id }
                        ?.first()?.progres?.toInt()!!)
            }
        }else{
                holder.progres.setProgress(0)
            }

        }
        holder.itemView.setOnClickListener{ onItemClicked(ankete[position]) }

    }
    fun updateAnkete(ankete: List<Anketa>) {
        this.ankete = ankete
        notifyDataSetChanged()
    }
    inner class AnketaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusAnketeImage: ImageView = itemView.findViewById(R.id.statusAnketeImage)
        val nazivAnkete: TextView = itemView.findViewById(R.id.nazivAnkete)
        val nazivIstraživanja: TextView = itemView.findViewById(R.id.nazivIstraživanja)
        val datumAnkete: TextView = itemView.findViewById(R.id.datumAnkete)
        val progres : ProgressBar =itemView.findViewById(R.id.progresZavrsetka)

    }
}