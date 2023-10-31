package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.presentation.RocketListAdapter
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RocketListFragment : Fragment() {

    companion object {
        fun newInstance() = RocketListFragment()
    }

    private val viewModel by activityViewModel<RocketListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.rocketLiveData.observe(this) { rockets ->
            val recycler = requireView().findViewById<RecyclerView>(R.id.rocket_list)
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = RocketListAdapter(rockets)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rocket_list, container, false)
    }
}