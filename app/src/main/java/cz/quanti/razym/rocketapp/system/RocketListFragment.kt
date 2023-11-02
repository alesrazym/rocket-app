package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.databinding.FragmentRocketListBinding
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.UiState
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.state) {
                        is UiState.Success -> {
                            binding.rocketListLayout.isRefreshing = false
                            binding.rocketListLoading.visibility = View.GONE
                            binding.rocketList.visibility = View.VISIBLE
                            binding.rocketList.adapter = RocketListAdapter(it.state.rockets)
                        }

                        is UiState.Error -> {
                            binding.rocketListLayout.isRefreshing = false
                            binding.rocketListLoading.visibility = View.VISIBLE
                            binding.rocketListLoading.text = getString(R.string.rockets_loading_error)
                            binding.rocketList.visibility = View.GONE
                        }

                        is UiState.Loading -> {
                            binding.rocketListLayout.isRefreshing = true
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRocketListBinding.inflate(inflater, container, false)

        binding.rocketListLayout.setOnRefreshListener(viewModel::fetchRockets)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}