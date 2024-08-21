package com.good.cars.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.R
import com.good.cars.app.database.Car
import com.good.cars.app.ui.composables.CarInfoBottomSheetContent
import com.good.cars.app.ui.composables.CustomBottomBar
import com.good.cars.app.ui.composables.CustomModalSheet
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.util.ADD_CAR_SCREEN

@Composable
fun CarsScreen(
    navHostController: NavHostController,
    carViewModel: CarViewModel,
    modifier: Modifier = Modifier
) {
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    val selectedCar by carViewModel.selectedCar.collectAsState()
    var isModalVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xffff5c00),
        bottomBar = {
            CustomBottomBar(
                items = listOf(
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.car_nav)),
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.calendar_nav)),
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.settings_nav))
                ),
                selectedIndex = 0,
                onItemSelected = {},
                navController = navHostController
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Мои авто",
                fontSize = 28.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp)
            )

            cars.forEach { car ->
                Spacer(modifier = modifier.height(16.dp))
                AddedCarCard(
                    car = car,
                    onAddedCarClick = {
                        carViewModel.selectCar(car)
                        isModalVisible = true
                    },
                    onDeleteCarClick = {
                        carViewModel.deleteCar(car)
                    }
                )
            }

            Spacer(modifier = modifier.height(16.dp))

            AddCarCard {
                navHostController.navigate(ADD_CAR_SCREEN)
            }
        }
    }

    if (isModalVisible && selectedCar != null) {
        CustomModalSheet(
            visible = isModalVisible,
            onDismiss = { isModalVisible = false }
        ) {
            CarInfoBottomSheetContent(
                car = selectedCar!!,
                onClose = { isModalVisible = false }
            )
        }
    }
}



@Composable
fun AddCarCard(
    onAddCarClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onAddCarClick() }
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xff712800))
                .fillMaxWidth()
                .padding(top = 59.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_car),
                contentDescription = "Add car",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 18.dp)
            )
            Text(
                text = "Добавить автомобиль",
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun AddedCarCard(
    car: Car,
    onAddedCarClick: () -> Unit,
    onDeleteCarClick: () -> Unit
) {
    val carIconResource = when (car.carIcon) {
        1 -> R.drawable.white_car_icon
        else -> R.drawable.black_car_icon
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(Color(0xffff9b62))
            .clickable(onClick = onAddedCarClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = carIconResource),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(190.dp)
            )
            Text(
                text = "${car.name}, ${car.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.close_buttonm),
            contentDescription = "Close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .clickable {
                    showDialog = true
                }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Удалить авто") },
            text = { Text(text = "Вы уверенны, что хотите удалить эту машину?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteCarClick()
                    showDialog = false
                }) {
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




data class NavItemState(
    val unselectedIcon: Painter,
)


@Preview
@Composable
fun CarsScreenPreview() {
    CarsScreen(
        navHostController = rememberNavController(),
        carViewModel = hiltViewModel()
    )
}