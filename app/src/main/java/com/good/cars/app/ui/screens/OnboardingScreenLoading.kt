package com.good.cars.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.R
import com.good.cars.app.prefs.PreferenceManager
import com.good.cars.app.util.CARS_SCREEN
import com.good.cars.app.util.ONBOARDING_SCREEN
import com.good.cars.app.util.ONBOARDING_SCREEN_LOADING
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreenLoading(navHostController: NavHostController) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }

    LaunchedEffect(Unit) {
        delay(1000)

        val nextDestination = if (preferenceManager.isOnboardingCompleted()) {
            CARS_SCREEN
        } else {
            ONBOARDING_SCREEN
        }

        navHostController.navigate(nextDestination) {
            popUpTo(ONBOARDING_SCREEN_LOADING) { inclusive = true }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffff5c00)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xffff5c00)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "icon"
            )
            Text(
                text = "Мое авто:",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.W700
            )
            Text(
                text = "Обслуживание",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(bottom = 75.dp)
            )
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun OnboardingScreenPreviewLoading() {
    OnboardingScreenLoading(
        navHostController = rememberNavController()
    )
}
