package cz.quanti.razym.rocketapp.system

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.quanti.razym.rocketapp.databinding.RocketListItemBinding
import cz.quanti.razym.rocketapp.model.Rocket

class RocketListAdapter(
    private val rockets: List<Rocket>
) : RecyclerView.Adapter<RocketListAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: RocketListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rocket: Rocket) {
            binding.rocketItemTitle.text = rocket.name
            binding.rocketItemSubtitle.text = rocket.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RocketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rockets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rockets[position])
    }
}
