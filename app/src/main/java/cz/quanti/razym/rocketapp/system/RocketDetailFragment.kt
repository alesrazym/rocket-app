package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRocketDetailBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.menu.findItem(R.id.action_launch)
            .setOnMenuItemClickListener {
                findNavController().navigate(
                    RocketDetailFragmentDirections.actionRocketDetailFragmentToRocketLaunchFragment(),
                    getKoin().get<NavOptions>(),
                )
                true
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        fun formatBurn(burnSec: Int?): String {
            return if (burnSec == null) {
                getString(R.string.unknown)
            } else {
                getString(R.string.seconds_burn_time, rocket.firstStage.burnTimeSec)
            }
        }

        fun formatReusable(reusable: Boolean): String {
            return if (reusable) {
                getString(R.string.reusable)
            } else {
                getString(R.string.not_reusable)
            }
        }
        binding.toolbar.title = rocket.name
        binding.rocketDetailOverview.text = rocket.overview
        binding.rocketDetailHeight.text = String.format("%.0fm", rocket.heightMeters)
        binding.rocketDetailDiameter.text = String.format("%.0fm", rocket.diameterMeters)
        binding.rocketDetailMass.text = String.format("%.0ft", rocket.massTons)
        binding.rocketDetailFirstReusable.text = formatReusable(rocket.firstStage.reusable)
        val firstStageEngines = rocket.firstStage.engines
        binding.rocketDetailFirstEngines.text = resources.getQuantityString(
            R.plurals.engines, firstStageEngines, firstStageEngines,
        )
        binding.rocketDetailFirstFuel.text = getString(R.string.tons_of_fuel, rocket.firstStage.fuelAmountTons)
        binding.rocketDetailFirstBurn.text = formatBurn(rocket.firstStage.burnTimeSec)
        binding.rocketDetailSecondReusable.text = formatReusable(rocket.secondStage.reusable)
        val secondStageEngines = rocket.secondStage.engines
        binding.rocketDetailSecondEngines.text = resources.getQuantityString(
            R.plurals.engines, secondStageEngines, secondStageEngines,
        )
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
            LayoutInflater.from(requireContext()),
            binding.rocketDetailPhotos,
            true,
        )

        view.rocketPhoto.load(it) {
            crossfade(true)
            // Just for fun placeholder & error.
            placeholder(R.drawable.rocket_idle)
            error(R.drawable.rocket_error)
        }
    }
}
