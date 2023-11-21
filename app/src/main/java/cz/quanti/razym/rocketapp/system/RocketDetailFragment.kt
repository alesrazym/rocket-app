package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.databinding.FragmentRocketDetailBinding
import cz.quanti.razym.rocketapp.databinding.RocketDetailPhotoItemBinding
import cz.quanti.razym.rocketapp.model.RocketDetail
import cz.quanti.razym.rocketapp.model.Stage
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel

class RocketDetailFragment : Fragment() {

    private val viewModel by viewModel<RocketDetailViewModel>()

    private var _binding: FragmentRocketDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RocketDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState
                .onStart {
                    viewModel.fetchRocket(args.rocketId)
                }
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    processState(it.state)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRocketDetailBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.menu.findItem(R.id.action_launch)
            .setOnMenuItemClickListener {
                findNavController().navigate(
                    RocketDetailFragmentDirections.actionRocketDetailFragmentToRocketLaunchFragment(),
                    getKoin().get<NavOptions>()
                )
                true
            }

        binding.rocketDetailCompose.setContent {
            RocketappTheme {
                RocketDetailFragmentContent(viewModel)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    private fun RocketDetailFragmentContent(viewModel: RocketDetailViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        if (uiState.state is RocketDetailViewModel.UiState.Success) {
            RocketDetailFragmentContent((uiState.state as RocketDetailViewModel.UiState.Success).rocket)
        }
    }

    @Composable
    private fun RocketDetailFragmentContent(rocket: RocketDetail) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(id = R.string.rocket_detail_overview),
                style = MaterialTheme.typography.titleMedium
                )
            Text(
                text = rocket.overview,
                style = MaterialTheme.typography.bodyMedium
                )
        }
    }

    @Preview
    @Composable
    fun RocketDetailFragmentContentPreview() {
        RocketappTheme {
            RocketDetailFragmentContent(previewRocketDetail())
        }
    }

    private fun processState(state: RocketDetailViewModel.UiState) {
        when (state) {
            is RocketDetailViewModel.UiState.Success -> {
                binding.rocketDetailStatus.visibility = View.GONE
                binding.rocketDetailContent.visibility = View.VISIBLE
                binding.toolbar.menu.findItem(R.id.action_launch).isVisible = true
                populateRocketDetail(state.rocket)
            }

            is RocketDetailViewModel.UiState.Error -> {
                binding.rocketDetailStatus.visibility = View.VISIBLE
                binding.rocketDetailContent.visibility = View.GONE
                binding.rocketDetailStatus.text = getString(R.string.getting_rocket_detail_failed)
                binding.toolbar.menu.findItem(R.id.action_launch).isVisible = false
            }

            is RocketDetailViewModel.UiState.Loading -> {
                binding.rocketDetailStatus.text =
                    getString(R.string.getting_rocket_detail_in_progress)
                binding.toolbar.menu.findItem(R.id.action_launch).isVisible = false
            }
        }
    }

    // TODO util class for formatting, rounding and unit handling.
    // TODO is there any library for this? Or build in kotlin?
    private fun populateRocketDetail(rocket: RocketDetail) {
        fun formatBurn(burnSec: Int?) : String {
            return if (burnSec == null) {
                getString(R.string.unknown)
            } else {
                getString(R.string.seconds_burn_time, rocket.firstStage.burnTimeSec)
            }
        }
        fun formatReusable(reusable: Boolean) : String {
            return if (reusable) {
                getString(R.string.reusable)
            } else {
                getString(R.string.not_reusable)
            }
        }
        binding.toolbar.title = rocket.name
        binding.rocketDetailHeight.text = String.format("%.0fm", rocket.heightMeters)
        binding.rocketDetailDiameter.text = String.format("%.0fm", rocket.diameterMeters)
        binding.rocketDetailMass.text = String.format("%.0ft", rocket.massTons)
        binding.rocketDetailFirstReusable.text = formatReusable(rocket.firstStage.reusable)
        val firstStageEngines = rocket.firstStage.engines
        binding.rocketDetailFirstEngines.text = resources.getQuantityString(
            R.plurals.engines, firstStageEngines, firstStageEngines)
        binding.rocketDetailFirstFuel.text = getString(R.string.tons_of_fuel, rocket.firstStage.fuelAmountTons)
        binding.rocketDetailFirstBurn.text = formatBurn(rocket.firstStage.burnTimeSec)
        binding.rocketDetailSecondReusable.text = formatReusable(rocket.secondStage.reusable)
        val secondStageEngines = rocket.secondStage.engines
        binding.rocketDetailSecondEngines.text = resources.getQuantityString(
            R.plurals.engines, secondStageEngines, secondStageEngines)
        binding.rocketDetailSecondFuel.text = getString(R.string.tons_of_fuel, rocket.secondStage.fuelAmountTons)
        binding.rocketDetailSecondBurn.text = formatBurn(rocket.secondStage.burnTimeSec)
        binding.rocketDetailPhotos.removeAllViews()

        // Set the limit, not to flood app when not handled in any kind of clever presentation.
        rocket.flickrImages
            .take(10)
            .forEach { createImageViewAndLoad(it) }
    }

    private fun createImageViewAndLoad(it: String) {
        val view = RocketDetailPhotoItemBinding.inflate(
            LayoutInflater.from(requireContext()), binding.rocketDetailPhotos, true
        )

        view.rocketPhoto.load(it) {
            crossfade(true)
            // Just for fun placeholder & error.
            placeholder(R.drawable.rocket_idle)
            error(R.drawable.rocket_error)
        }
    }

    private fun previewRocketDetail(): RocketDetail {
        return RocketDetail(
            id = "1",
            name = "Falcon 1",
            overview = "Falcon 1 was an expendable launch system privately developed and manufactured by SpaceX during 2006-2009. On 28 September 2008, Falcon 1 became the first privately-developed liquid-fuel launch vehicle to go into orbit around the Earth.",
            heightMeters = 22.25,
            diameterMeters = 1.68,
            massTons = 30.0,
            firstStage = Stage(
                reusable = false,
                engines = 1,
                fuelAmountTons = 44.3,
                burnTimeSec = 169
            ),
            secondStage = Stage(
                reusable = false,
                engines = 1,
                fuelAmountTons = 3.38,
                burnTimeSec = 378
            ),
            flickrImages = listOf(
                "https://farm1.staticflickr.com/929/28787338307_3453a11a77_b.jpg",
                "https://farm4.staticflickr.com/3955/32915197674_eee74d81bb_b.jpg",
                "https://farm1.staticflickr.com/293/32312415025_6841e30bf1_b.jpg",
                "https://farm1.staticflickr.com/623/23660653516_5b6cb301d1_b.jpg",
                "https://farm6.staticflickr.com/5518/31579784413_d853331601_b.jpg"
            )
        )
    }
}