package cz.quanti.rocketapp.android.lib.uisystem.system

import androidx.navigation.NamedNavArgument

// TODO: don't know if such a base class is necessary.
abstract class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList(),
)
