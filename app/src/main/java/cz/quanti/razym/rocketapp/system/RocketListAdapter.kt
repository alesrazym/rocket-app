package cz.quanti.razym.rocketapp.system

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.databinding.RocketListItemBinding
import cz.quanti.razym.rocketapp.model.Rocket
import java.util.Locale

class RocketListAdapter(
    private val rockets: List<Rocket>,
    private val onItemClick: (Rocket) -> Unit
) : RecyclerView.Adapter<RocketListAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: RocketListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val firstFlightFormat = DateFormat.getDateInstance(
            DateFormat.MEDIUM,
            Locale.getDefault()
        )

        fun bind(rocket: Rocket) {
            val ctx = binding.root.context
            binding.rocketItemTitle.text = rocket.name
            binding.rocketItemSubtitle.text =
                if (rocket.firstFlight == null) {
                    ctx.getString(
                        R.string.first_flight,
                        ctx.getString(R.string.first_flight_unknown)
                    )
                } else {
                    ctx.getString(
                        R.string.first_flight,
                        firstFlightFormat.format(rocket.firstFlight))
                }
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
        holder.itemView.setOnClickListener {
            onItemClick(rockets[position])
        }
    }
}
