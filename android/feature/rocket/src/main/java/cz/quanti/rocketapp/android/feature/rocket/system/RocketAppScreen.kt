package cz.quanti.rocketapp.android.feature.rocket.system

import androidx.navigation.NamedNavArgument
import cz.quanti.rocketapp.android.lib.uisystem.system.Screen

sealed class RocketAppScreen(
    route: String,
    navArguments: List<NamedNavArgument> = emptyList(),
) : Screen(route, navArguments)
