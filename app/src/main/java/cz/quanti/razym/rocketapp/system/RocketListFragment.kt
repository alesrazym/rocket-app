package cz.quanti.razym.rocketapp.system

import android.icu.text.DateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.databinding.FragmentRocketListBinding
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.mp.KoinPlatform.getKoin
import java.util.Date
import java.util.Locale

class RocketListFragment : Fragment() {

    private val viewModel by viewModel<RocketListViewModel>()

    private var _binding: FragmentRocketListBinding? = null
    private val binding get() = _binding!!

    private val firstFlightFormat = DateFormat.getDateInstance(
        DateFormat.MEDIUM,
        Locale.getDefault()
    )

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
                    RocketList(rockets = state.rockets)
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
    private fun RocketList(rockets: List<Rocket>) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(rockets) { rocket ->
                RocketListItem(rocket, onClick = {
                    findNavController().navigate(
                        RocketListFragmentDirections
                            .actionRocketListFragmentToRocketDetailFragment(rocket.id),
                        getKoin().get<NavOptions>()
                    )
                })
            }
        }
    }

    @Composable
    private fun RocketListItem(rocket: Rocket, onClick: () -> Unit) {
        Row (
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.rocket),
                contentDescription = stringResource(R.string.rocket_icon_content_description),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rocket.name,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(16.dp))

                FirstFlightText(date = rocket.firstFlight)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = stringResource(R.string.arrow_icon_content_description),
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    private fun FirstFlightText(date: Date?) {
        val str = if (date == null) {
            stringResource(
                R.string.first_flight,
                stringResource(R.string.first_flight_unknown)
            )
        } else {
            stringResource(
                R.string.first_flight,
                firstFlightFormat.format(date)
            )
        }

        Text(
            text = str,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    @Preview
    @Composable
    private fun RocketListPreview() {
        RocketList(
            rockets = List(4) {
                previewRocket()
            }
        )
    }

    @Preview
    @Composable
    private fun RocketListItemPreview() {
        RocketListItem(
            rocket = previewRocket(),
            onClick = {},
        )
    }

    private fun previewRocket() = Rocket(
        "Falcon 9",
        RocketData.firstFlightParser.parse("2010-06-04"),
        "falcon9",
    )
}