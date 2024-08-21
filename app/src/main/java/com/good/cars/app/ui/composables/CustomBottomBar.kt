package com.good.cars.app.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.good.cars.app.ui.screens.NavItemState
import com.good.cars.app.util.CARS_SCREEN
import com.good.cars.app.util.REMINDER_SCREEN
import com.good.cars.app.util.SETTINGS_SCREEN

@Composable
fun CustomBottomBar(
    items: List<NavItemState>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavHostController
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(44.dp))
            .background(Color.White)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Icon(
                    painter = item.unselectedIcon,
                    contentDescription = null,
                    tint = if (selectedIndex == index) Color(0xffff5c00) else Color(0xffbfbfbf),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onItemSelected(index)
                            when (index) {
                                0 -> navController.navigate(CARS_SCREEN) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    launchSingleTop = true
                                }
                                1 -> {
                                    navController.navigate(REMINDER_SCREEN)
                                }
                                2 -> navController.navigate(SETTINGS_SCREEN) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                )
            }
        }
    }
}

