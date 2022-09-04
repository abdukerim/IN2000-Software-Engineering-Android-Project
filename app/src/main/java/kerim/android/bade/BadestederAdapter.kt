package kerim.android.bade

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.google.android.gms.maps.model.LatLng
import kerim.android.bade.data.Tsery
import okhttp3.internal.format

class BadestederAdapter :
    RecyclerView.Adapter<BadestederAdapter.ViewHolder>() {


    private var tsery = listOf<Tsery>()
    fun setUpdatedData (tsery: List<Tsery>) {
        this.tsery = tsery
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val navn : TextView = view.findViewById(R.id.badested)
        val vanntemp : TextView = view.findViewById(R.id.vanntemp)
        val lufttemp : TextView = view.findViewById(R.id.lufttemp)
        val distanse : TextView = view.findViewById(R.id.distanse)
        val vaerIkon : ImageView = view.findViewById(R.id.vaerIkon)

        fun bind(item:View, pos: LatLng){
            item.setOnClickListener {
                (item.context as MainActivity).switchToInfoFragment(pos)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.badestedlayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView, LatLng(tsery[position].header.extra.pos.lat.toDouble(),tsery[position].header.extra.pos.lon.toDouble()))
        holder.navn.text = tsery[position].header.extra.name
        holder.vanntemp.text = format("%s °C" , tsery[position].observations[0].body.value)
        holder.lufttemp.text = format("%s °C" , tsery[position].header.extra.pos.airTemp)
        holder.distanse.text = String.format("%.1f km",tsery[position].header.extra.pos.distance.toFloat().div(1000))
        setImage(tsery[position].header.extra.pos.summary, holder)
    }


    override fun getItemCount(): Int {
        return tsery.size
    }

    private fun timeToReadable(time : String) : String {
        val year = time.subSequence(0,4)
        val month = time.subSequence(5,7)
        val day = time.subSequence(8,10)
        val hour = time.subSequence(11,13)
        val minute = time.subSequence(14,16)
        return "$day.$month.$year $hour:$minute"
    }

    private fun setImage(desc : String, holder: ViewHolder){
        if (desc == null) {
            return
        }
        val resId = holder.vaerIkon.context.resources.getIdentifier(desc,
            "drawable",holder.vaerIkon.context.packageName)
        holder.vaerIkon.setImageResource(resId)
    }
}
