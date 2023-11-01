package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.quanti.razym.rocketapp.databinding.FragmentRocketListBinding
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RocketListFragment : Fragment() {

    companion object {
        fun newInstance() = RocketListFragment()
    }

    private val viewModel by activityViewModel<RocketListViewModel>()

    private var _binding: FragmentRocketListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.rocketLiveData.observe(this) { rockets ->
            binding.rocketList.adapter = RocketListAdapter(rockets)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRocketListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}