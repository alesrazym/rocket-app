package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.quanti.razym.rocketapp.databinding.FragmentRocketListBinding
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class RocketListFragment : Fragment() {

    companion object {
        fun newInstance() = RocketListFragment()
    }

    private val viewModel by viewModel<RocketListViewModel>()

    private var _binding: FragmentRocketListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.rocketLiveData.observe(this) { rocketsResult ->
            when (rocketsResult.status) {
                Status.SUCCESS -> {
                    binding.rocketListLayout.isRefreshing = false
                    binding.rocketList.adapter =
                        RocketListAdapter(rocketsResult.data ?: emptyList())
                }
                Status.ERROR -> {
                    binding.rocketListLayout.isRefreshing = false
                    // TODO error view
                }
                Status.LOADING -> {
                    binding.rocketListLayout.isRefreshing = true
                    // do nothing.
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRocketListBinding.inflate(inflater, container, false)
        
        binding.rocketListLayout.setOnRefreshListener(viewModel::refreshRockets)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}