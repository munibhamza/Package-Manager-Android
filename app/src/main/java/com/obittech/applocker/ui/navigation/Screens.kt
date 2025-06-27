package com.obittech.applocker.ui.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Settings : Screens("settings")
    object About : Screens("about")
}