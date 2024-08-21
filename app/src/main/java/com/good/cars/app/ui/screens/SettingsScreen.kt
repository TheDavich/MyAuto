package com.good.cars.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.R
import com.good.cars.app.ui.composables.CustomBottomBar
import com.good.cars.app.ui.viewModel.CarViewModel

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    carViewModel: CarViewModel
) {
    val items = listOf(
        NavItemState(
            unselectedIcon = painterResource(id = R.drawable.car_nav)
        ),
        NavItemState(
            unselectedIcon = painterResource(id = R.drawable.calendar_nav)
        ),
        NavItemState(
            unselectedIcon = painterResource(id = R.drawable.settings_nav)
        )
    )

    var bottomNavState by rememberSaveable { mutableStateOf(2) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffff5c00),
        bottomBar = {
            CustomBottomBar(
                items = items,
                selectedIndex = bottomNavState,
                onItemSelected = { index ->
                    bottomNavState = index
                },
                navController = navHostController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
        ) {
            Text(
                text = "Настройки",
                fontSize = 28.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xff712800)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDialog = true }
                ) {
                    Text(
                        text = "Сбросить все машины",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W700,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Подтверждение") },
                text = { Text(text = "Вы собираетесь удалить все автомобили из базы данных, вы уверены, что хотите продолжить?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            carViewModel.deleteAllCars()
                            showDialog = false
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Нет")
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview(

) {
    SettingsScreen(
        navHostController = rememberNavController(),
        carViewModel = hiltViewModel()
    )
}

