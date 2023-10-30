package cz.quanti.razym.rocketapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.domain.RocketModel

class RocketListAdapter(
    private val rockets: List<RocketModel>
) : RecyclerView.Adapter<RocketListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val title: TextView = itemView.findViewById(R.id.rocket_item_title)
        private val subtitle: TextView = itemView.findViewById(R.id.rocket_item_subtitle)

        fun bind(rocket: RocketModel) {
            title.text = rocket.name
            subtitle.text = rocket.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rocket_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rockets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rockets[position])
    }
}
