package cz.quanti.rocketapp.system

import androidx.navigation.NamedNavArgument

sealed class RocketAppScreen(
    route: String,
    navArguments: List<NamedNavArgument> = emptyList()
) : Screen(route, navArguments)
