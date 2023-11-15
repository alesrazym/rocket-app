package cz.quanti.razym.rocketapp.system

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.quanti.razym.rocketapp.ui.theme.RocketAppTheme
import org.koin.android.ext.android.getKoin

class RocketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RocketAppTheme {
                RocketApp()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun RocketApp() {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            contentColor = RocketAppTheme.colors.onBackground,
        ) {
            // As this scaffold is used only to place snackbar, we do not use inner padding.

            CompositionLocalProvider(
                LocalSnackbar provides snackbarHostState,
            ) {
                RocketNavHost(navController)
            }
        }
    }

    @Composable
    private fun RocketNavHost(navController: NavHostController) {
        NavHost(navController = navController, startDestination = RocketListScreen.route) {
            rocketListScreen(
                onRocketItemClick = { rocket ->
                    navController.navigateToRocketDetail(rocket.id, rocket.name, getKoin().get())
                }
            )
            rocketDetailScreen(
                onBackClick = { navController.popBackStack() },
                // TODO without args, is it worth of creating extension method,
                //  like navController.navigateToRocketDetail?
                onLaunchClick = { navController.navigate(RocketLaunchScreen.route) },
            )
            rocketLaunchScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
