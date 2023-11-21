package cz.quanti.razym.rocketapp.system

import android.icu.text.DateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.mp.KoinPlatform.getKoin
import java.util.Locale
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
class RocketListFragment : Fragment() {

    private val viewModel by viewModel<RocketListViewModel>()

    private val firstFlightFormat = DateFormat.getDateInstance(
        DateFormat.MEDIUM,
        Locale.getDefault()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                Refreshing(viewModel)
            }
        }
    }

    @Composable
    private fun Refreshing(viewModel: RocketListViewModel) {
        val uiState by viewModel.uiState.collectAsState()

        Refreshing(uiState.loading, uiState.rockets, viewModel::fetchRockets) { rocket ->
            findNavController().navigate(
                RocketListFragmentDirections
                    .actionRocketListFragmentToRocketDetailFragment(rocket.id),
                getKoin().get<NavOptions>()
            )
        }
    }

    @Composable
    private fun Refreshing(
        refreshing: Boolean = false,
        rockets: List<Rocket>?,
        onRefresh: () -> Unit,
        onItemClick: (Rocket) -> Unit = {},
    ) {
        RocketappTheme {
            Column (Modifier.fillMaxSize()) {
                RocketListTitle(
                    text = R.string.rockets_title,
                )
                RocketListBox(
                    rockets = rockets,
                    refreshing = refreshing,
                    onRefresh = onRefresh,
                    onItemClick = onItemClick,
                )
            }
        }
    }

    @Composable
    private fun RocketListTitle(@StringRes text: Int) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = Start,
        )
    }

    @Composable
    private fun RocketListBox(
        rockets: List<Rocket>?,
        refreshing: Boolean,
        onRefresh: () -> Unit,
        onItemClick: (Rocket) -> Unit = {},
    ) {
        val state = rememberPullRefreshState(refreshing, onRefresh)

        Box(
            Modifier
                .pullRefresh(state)
                .fillMaxSize()
        ) {
            if (rockets == null && refreshing) {
                RocketListText(
                    text = R.string.rockets_loading,
                )
            } else {
                if (rockets == null)
                    // TODO pull to refresh does not work on text field only.
                    RocketListText(
                        text = R.string.rockets_loading_error,
                    )
                else
                    RocketList(
                        rockets = rockets,
                        onItemClick = onItemClick,
                    )
            }

            // TODO handle error messages

            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }

    @Composable
    private fun RocketListText(@StringRes text: Int) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .padding(16.dp),
            textAlign = Center,
        )
    }

    @Composable
    private fun RocketList(rockets: List<Rocket>, onItemClick: (Rocket) -> Unit = {}) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(rockets) { rocket ->
                RocketListItem(
                    rocket = rocket,
                    onClick = onItemClick,
                )
            }
        }
    }

    @Composable
    private fun RocketListItem(rocket: Rocket, onClick: (Rocket) -> Unit) {
        Row (
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick(rocket) },
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

    @Preview
    @Composable
    private fun RocketListTextPreview() {
        RocketListText(R.string.rockets_loading)
    }

    @Preview
    @Composable
    private fun RocketListTitlePreview() {
        RocketListTitle(R.string.rockets_title)
    }

    private fun previewRocket() = Rocket(
        "Falcon 9",
        RocketData.firstFlightParser.parse("2010-06-04"),
        "falcon9",
    )
}