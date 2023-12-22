package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.quanti.razym.rocketapp.ui.theme.RocketappTheme
import org.koin.android.ext.android.getKoin

class RocketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RocketappTheme {
                RocketApp()
            }
        }
    }

    @Composable
    private fun RocketApp() {
        val navController = rememberNavController()

        RocketNavHost(navController)
    }

    @Composable
    private fun RocketNavHost(navController: NavHostController) {
        NavHost(navController = navController, startDestination = RocketListScreen.route) {
            rocketListScreen(
                onRocketItemClick = { rocket, name ->
                    navController.navigateToRocketDetail(rocket.id, name, getKoin().get())
                },
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
