package com.good.cars.app.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.R
import com.good.cars.app.util.CARS_SCREEN
import com.good.cars.app.util.REMINDER_SCREEN
import com.good.cars.app.util.SETTINGS_SCREEN

@Composable
fun CustomBottomNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(44.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BottomAppBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .height(61.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.car_nav),
                    contentDescription = "Car",
                    tint = if (selectedItem == 0) Color(0xffff5c00) else Color(0xffbfbfbf),
                    modifier = Modifier
                        .clickable {
                            selectedItem = 0
                            navController.navigate(CARS_SCREEN) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                        .size(32.dp)
                )

                Spacer(modifier = Modifier.width(80.dp))

                Icon(
                    painter = painterResource(id = R.drawable.calendar_nav), // Replace with your calendar icon resource
                    contentDescription = "Calendar",
                    tint = if (selectedItem == 1) Color(0xffff5c00) else Color(0xffbfbfbf),
                    modifier = Modifier
                        .clickable {
                            selectedItem = 1
                            navController.navigate(REMINDER_SCREEN) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                        .size(32.dp)
                )

                Spacer(modifier = Modifier.width(80.dp))

                Icon(
                    painter = painterResource(id = R.drawable.settings_nav), // Replace with your settings icon resource
                    contentDescription = "Settings",
                    tint = if (selectedItem == 2) Color(0xffff5c00) else Color(0xffbfbfbf),
                    modifier = Modifier
                        .clickable {
                            selectedItem = 2
                            navController.navigate(SETTINGS_SCREEN) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                        .size(32.dp)
                )
            }
        }
    }
}




@Preview
@Composable
fun CustomBottomNavigationBarPreview() {
    CustomBottomNavigationBar(
        navController = rememberNavController()
    )
}
