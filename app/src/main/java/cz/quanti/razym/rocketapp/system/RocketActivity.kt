package cz.quanti.razym.rocketapp.system

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        NavHost(navController = navController, startDestination = Screen.RocketList.route) {
            composable(Screen.RocketList.route) {
                RocketListScreen(
                    onItemClick = { rocket ->
                        navController.navigate(
                            Screen.RocketDetail.createRoute(rocket.id),
                            getKoin().get<NavOptions>()
                        )
                    }
                )
            }
            composable(
                route = Screen.RocketDetail.route,
                arguments = Screen.RocketDetail.navArguments,
            ) {
                RocketDetailScreen(
                    rocketId = it.arguments!!.getString(Screen.RocketDetail.navArguments[0].name)!!,
                    onBackClick = { navController.popBackStack() },
                    onLaunchClick = { navController.navigate(Screen.RocketLaunch.route) },
                )
            }
            composable(Screen.RocketLaunch.route) {
                RocketLaunchScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
