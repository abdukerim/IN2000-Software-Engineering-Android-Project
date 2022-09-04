package kerim.android.bade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import androidx.fragment.app.activityViewModels
import kerim.android.bade.data.Tsery
import okhttp3.internal.format

class DetailsFragment(private val position : LatLng) : Fragment() {

    companion object {
        fun newInstance(p:LatLng) = DetailsFragment(p)
    }

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val view : View = inflater.inflate(R.layout.fragment_details, container, false)
        view.clipToOutline
        val placeName = view.findViewById<View>(R.id.text_detail_placeName) as TextView
        placeName.text = position.toString()

        if (viewModel.getStaticList().value == null) {
            return view
        }

        for (i in (viewModel.getStaticList().value as ArrayList<Tsery>)) {
            if(position == LatLng(i.header.extra.pos.lat.toDouble(),i.header.extra.pos.lon.toDouble())) {
                placeName.text = i.header.extra.name
                val summary = i.header.extra.pos.summary
                val vaerIcon : ImageView = view.findViewById(R.id.vaerIkon)
                if (summary != null) { //vet ikke hvorfor den klager; har crashet med en nullpointerexception
                    vaerIcon.setImageResource(
                        vaerIcon.context.resources.getIdentifier(
                            summary,
                            "drawable",
                            vaerIcon.context.packageName
                        )
                    )}
                //(view.findViewById<View>(R.id) as TextView).text = i.header.extra.pos.summary
                (view.findViewById<View>(R.id.bade_temp) as TextView).text = format("Vann Temp")
                (view.findViewById<View>(R.id.vannTempTxt) as TextView).text = format("%s °C" , i.observations[0].body.value)
                (view.findViewById<View>(R.id.air_temp) as TextView).text = format("Luft Temp %s °C" , i.header.extra.pos.airTemp)
                (view.findViewById<View>(R.id.wind) as TextView).text = format("%s m/s", i.header.extra.pos.windSpeed)
                (view.findViewById<View>(R.id.distance) as TextView).text = String.format("%.1f km",i.header.extra.pos.distance.toFloat().div(1000))
                (view.findViewById<View>(R.id.duration) as TextView).text = format("Tar %s å kjøre", i.header.extra.pos.time)
            }
        }
        return view
    }
}