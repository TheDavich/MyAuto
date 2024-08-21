package com.good.cars.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.good.cars.app.ui.screens.AddCarScreen
import com.good.cars.app.ui.screens.CarsScreen
import com.good.cars.app.ui.screens.CreateReminderScreen
import com.good.cars.app.ui.screens.OnboardingScreen
import com.good.cars.app.ui.screens.OnboardingScreenLoading
import com.good.cars.app.ui.screens.ReminderScreen
import com.good.cars.app.ui.screens.SettingsScreen
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.ui.viewModel.ReminderViewModel
import com.good.cars.app.util.ADD_CAR_SCREEN
import com.good.cars.app.util.CARS_SCREEN
import com.good.cars.app.util.CREATE_REMINDER_SCREEN
import com.good.cars.app.util.ONBOARDING_SCREEN
import com.good.cars.app.util.ONBOARDING_SCREEN_LOADING
import com.good.cars.app.util.REMINDER_SCREEN
import com.good.cars.app.util.SETTINGS_SCREEN


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    val carViewModel: CarViewModel = hiltViewModel()
    val reminderViewModel: ReminderViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = ONBOARDING_SCREEN_LOADING,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            OnboardingScreenLoading(navHostController = navController)
        }
        composable(
            route = ONBOARDING_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            OnboardingScreen(
                navHostController = navController,
                carViewModel = carViewModel
            )
        }

        // Cars screen (main screen)
        composable(
            route = CARS_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            CarsScreen(
                navHostController = navController,
                carViewModel = carViewModel
            )
        }

        // Add car screen
        composable(
            route = ADD_CAR_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            AddCarScreen(
                navHostController = navController,
                carViewModel = carViewModel
            )
        }

        // Reminder screen
        composable(
            route = REMINDER_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ReminderScreen(
                navHostController = navController,
                carViewModel = carViewModel,
                reminderViewModel = reminderViewModel
            )
        }

        composable(
            route = CREATE_REMINDER_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
        ) {
            CreateReminderScreen(
                navHostController = navController,
                carViewModel = carViewModel,
                reminderViewModel = reminderViewModel,
                context = context
            )
        }
        composable(
            route = SETTINGS_SCREEN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
        ) {
            SettingsScreen(
                navHostController = navController,
                carViewModel = carViewModel
            )
        }
    }
}