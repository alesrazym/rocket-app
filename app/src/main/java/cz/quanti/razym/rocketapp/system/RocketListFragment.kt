package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.presentation.RocketListAdapter
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.Status
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RocketListFragment : Fragment() {

    companion object {
        fun newInstance() = RocketListFragment()
    }

    private val viewModel by activityViewModel<RocketListViewModel>()
    private lateinit var recyclerView : RecyclerView
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.rocketLiveData.observe(this) { rocketsResult ->
            when (rocketsResult.status) {
                Status.SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                    recyclerView.adapter = RocketListAdapter(rocketsResult.data ?: emptyList())
                }
                Status.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    // TODO error view
                }
                Status.LOADING -> {
                    swipeRefreshLayout.isRefreshing = true
                    // do nothing.
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_rocket_list, container, false)

        swipeRefreshLayout = view.findViewById(R.id.rocket_list_layout)
        swipeRefreshLayout.setOnRefreshListener(viewModel::refreshRockets)

        recyclerView = swipeRefreshLayout.findViewById(R.id.rocket_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}