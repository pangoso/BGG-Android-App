import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.example.bgg.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bgg.Games
import java.net.URL

class CustomAdapter(private val mList: List<Games>, private val context: Context, private val clickListener: (Long) -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_line_game, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Game = mList[position]

        holder.nameTV.text = Game.name
        holder.yearTV.text = Game.year
        if (Game.rank >= 0) {
            holder.rankTV.text = Game.rank.toString()
            holder.itemView.setOnClickListener{
                clickListener(mList[position].id)
            }
        }
        holder.numberTV.text = (position+1).toString()
        val url = URL(Game.thumbnail)
        Glide.with(this.context)
            .load(url)
            .into(holder.imageView);
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image)
        val nameTV: TextView = itemView.findViewById(R.id.nameTV)
        val rankTV: TextView = itemView.findViewById(R.id.rankTV)
        val yearTV: TextView = itemView.findViewById(R.id.yearTV)
        val numberTV: TextView = itemView.findViewById(R.id.numberTV)
    }
}