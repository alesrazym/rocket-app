package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.databinding.FragmentRocketListBinding
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RocketListFragment : Fragment() {

    private val viewModel by viewModel<RocketListViewModel>()

    private var _binding: FragmentRocketListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { processState(it.state) }
        }
    }

    private fun processState(state: UiState) {
        when (state) {
            is UiState.Success -> {
                binding.rocketListLayout.isRefreshing = false
                binding.rocketListLoading.visibility = View.GONE
                binding.rocketList.visibility = View.VISIBLE
                binding.rocketList.setContent {
                    LazyColumn {
                        items(state.rockets) { rocket ->
                            RocketListItem(rocket)
                            /*
                                                        {
                                                            findNavController().navigate(
                                                                RocketListFragmentDirections.actionRocketListFragmentToRocketDetailFragment(
                                                                    it.id
                                                                ),
                                                                getKoin().get<NavOptions>()
                                                            )
                            }
                            */
                        }
                    }
                }
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


    @Composable
    private fun RocketListItem(rocket: Rocket) {
        Row {
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.rocket),
                contentDescription = "Rocket icon",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = rocket.name,
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Preview
    @Composable
    private fun RocketListItemPreview() {
        RocketListItem(Rocket(
            "Falcon 9",
            RocketData.firstFlightParser.parse("2010-06-04"),
            "falcon9")
        )
    }
}