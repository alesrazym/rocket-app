package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.databinding.FragmentRocketDetailBinding
import org.koin.android.ext.android.getKoin

class RocketDetailFragment : Fragment() {

    private var _binding: FragmentRocketDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RocketDetailFragmentArgs by navArgs()

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

        binding.rocketDetailText.text = "Rocket detail: ${args.rocketId}"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}