package com.obittech.applocker.ui.navigation

import androidx.navigation.NavController
import com.obittech.applocker.ui.NavigationRoutes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() {
    private var navController: NavController? = null

    fun setNavController(controller: NavController) {
        this.navController = controller
    }

    fun navigateToHome(clearBackStack: Boolean = true) {
        navController?.navigate(NavigationRoutes.HOME) {
            if (clearBackStack) {
                popUpTo(0)
            }
        }
    }

    fun navigateToOnboarding() {
        navController?.navigate(NavigationRoutes.ONBOARDING) {
            popUpTo(0)
        }
    }

    fun navigateToSettings() {
        navController?.navigate(NavigationRoutes.SETTINGS)
    }

    fun navigateToAbout() {
        navController?.navigate(NavigationRoutes.ABOUT)
    }

    fun popBack() {
        navController?.popBackStack()
    }
}