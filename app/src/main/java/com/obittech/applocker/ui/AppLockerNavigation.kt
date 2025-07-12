package com.obittech.applocker.ui

import com.obittech.applocker.ui.AppLockerDestinationsArgs.APP_ID_ARG
import com.obittech.applocker.ui.AppLockerScreens.APPS_LIST_SCREEN
import com.obittech.applocker.ui.AppLockerScreens.APP_DETAIL_SCREEN
import com.obittech.applocker.ui.AppLockerScreens.SETTINGS_SCREEN

private object AppLockerScreens {
    const val APPS_LIST_SCREEN = "apps_list"
    const val APP_DETAIL_SCREEN = "app_detail"
    const val SETTINGS_SCREEN = "settings"

}

object AppLockerDestinationsArgs {
    const val APP_ID_ARG = "appId"
    const val TITLE_ARG = "title"
}

object AppLockerDestinations {
    const val APPS_LIST_ROUTE = APPS_LIST_SCREEN
    const val APPS_DETAIL_ROUTE = "$APP_DETAIL_SCREEN/{$APP_ID_ARG}"
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
}

object NavigationRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
}



//class TodoNavigationActions(private val navController: NavHostController) {
//
//    fun navigateToTasks() {
//        navController.navigate(
//            AppLockerDestinations.APPS_LIST_ROUTE) {
////            popUpTo(navController.graph.findStartDestination().id) {
////                inclusive = !navigatesFromDrawer
////                saveState = navigatesFromDrawer
////            }
////            launchSingleTop = true
////            restoreState = navigatesFromDrawer
//        }
//    }
//
//    fun navigateToSettings() {
//        navController.navigate(AppLockerDestinations.SETTINGS_ROUTE) {
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
////            popUpTo(navController.graph.findStartDestination().id) {
////                saveState = true
////            }
////            // Avoid multiple copies of the same destination when
////            // reselecting the same item
////            launchSingleTop = true
////            // Restore state when reselecting a previously selected item
////            restoreState = true
//        }
//    }
//
//    fun navigateToAppDetail(appId: String) {
//        navController.navigate("$APP_DETAIL_SCREEN/$appId")
//    }
//
//}