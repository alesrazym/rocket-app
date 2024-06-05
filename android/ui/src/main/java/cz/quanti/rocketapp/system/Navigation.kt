package cz.quanti.rocketapp.system

import androidx.navigation.navOptions
import cz.quanti.rocketapp.android.ui.R

fun provideNavOptions() = navOptions {
    anim {
        enter = R.anim.slide_in
        exit = R.anim.fade_out
        popEnter = R.anim.fade_in
        popExit = R.anim.slide_out
    }
}
