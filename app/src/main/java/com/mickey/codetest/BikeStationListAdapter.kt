import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mickey.codetest.BikeStation
import com.mickey.codetest.R

class BikeStationListAdapter(
    context: Context,
    private val resource: Int,
    private val bikeStations: List<BikeStation>
) : ArrayAdapter<BikeStation>(context, resource, bikeStations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val bikeStation = bikeStations[position]

        val snoTextView = view.findViewById<TextView>(R.id.snoTextView)
        val sareaTextView = view.findViewById<TextView>(R.id.sareaTextView)
        val snaTextView = view.findViewById<TextView>(R.id.snaTextView)

        snoTextView.text = "${bikeStation.sbi} / ${bikeStation.tot}"
        sareaTextView.text = bikeStation.sarea
        snaTextView.text = bikeStation.sna

        // 設置背景顏色
        val colorResId = if (position % 2 != 0) R.color.grayBackground else R.color.white
        view.setBackgroundResource(colorResId)

        return view
    }
}