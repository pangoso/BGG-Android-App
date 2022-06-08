import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bgg.R
import com.example.bgg.Ranks

class HistoryAdapter(private val mList: List<Ranks>, private val context: Context) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_line_rank, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Rank = mList[position]

        holder.dateTV.text = Rank.date
        holder.numberTV.text = Rank.place.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val dateTV: TextView = itemView.findViewById(R.id.dateTV)
        val numberTV: TextView = itemView.findViewById(R.id.numberTV)
    }

}
