package cz.quanti.razym.rocketapp.system

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object RocketList : Screen("rocketList")

    data object RocketDetail : Screen(
        route = "rocketDetail/{rocketId}",
        navArguments = listOf(
            navArgument("rocketId") { type = NavType.StringType },
        )
    ) {
        fun createRoute(rocketId: String) = "rocketDetail/${rocketId}"
    }

    data object RocketLaunch : Screen("rocketLaunch")
}