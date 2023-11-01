package cz.quanti.razym.rocketapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.quanti.razym.rocketapp.databinding.RocketListItemBinding
import cz.quanti.razym.rocketapp.domain.RocketModel

class RocketListAdapter(
    private val rockets: List<RocketModel>
) : RecyclerView.Adapter<RocketListAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: RocketListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rocket: RocketModel) {
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
