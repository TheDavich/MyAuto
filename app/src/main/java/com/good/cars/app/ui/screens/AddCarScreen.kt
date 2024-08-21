package com.good.cars.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.util.ADD_CAR_SCREEN
import com.good.cars.app.util.CARS_SCREEN
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddCarScreen(
    navHostController: NavHostController,
    carViewModel: CarViewModel,
    modifier: Modifier = Modifier
) {
    val pageCount = 2
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val coroutineScope = rememberCoroutineScope()

    var carName by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf(0) }
    var carMileage by remember { mutableStateOf(0) }
    var fuelExpense by remember { mutableStateOf(0) }
    var repairExpense by remember { mutableStateOf(0) }
    var repairDate by remember { mutableStateOf("") }
    var carIcon by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xffff5c00)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingTwo(
                        carName = carName,
                        carYear = carYear,
                        carMileage = carMileage,
                        onCarDataChange = { name, year, mileage ->
                            carName = name
                            carYear = year
                            carMileage = mileage
                        },
                        titleText = "Добавить автомобиль"
                    )
                    1 -> OnboardingThree(
                        fuelExpense = fuelExpense,
                        repairExpense = repairExpense,
                        onExpensesChange = { fuel, repair ->
                            fuelExpense = fuel
                            repairExpense = repair
                        },
                        onIconSelected = { icon ->
                            carIcon = icon
                        },
                        repairDate = repairDate,
                        onRepairDateChange = { date ->
                            repairDate = date
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Column {
                    CustomDotsIndicator(
                        currentPage = pagerState.currentPage,
                        pageCount = pageCount
                    )
                    Text(
                        text = "Отменить",
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = modifier
                            .padding(top = 16.dp)
                            .clickable {
                                navHostController.popBackStack()
                            }
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier.size(64.dp)
                ) {
                    CircularProgressIndicator(
                        progress = (pagerState.currentPage + 1) / pageCount.toFloat(),
                        strokeWidth = 4.dp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = modifier.fillMaxSize()
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage < pageCount - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {
                                    carViewModel.addCar(
                                        name = carName,
                                        year = carYear,
                                        mileage = carMileage,
                                        fuelExpense = fuelExpense,
                                        repairExpense = repairExpense,
                                        repairDate = repairDate,
                                        carIcon = carIcon
                                    )
                                    navHostController.navigate(CARS_SCREEN) {
                                        popUpTo(ADD_CAR_SCREEN) { inclusive = true }
                                    }
                                }
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "",
                            tint = Color(0xffff5c00)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun AddCarScreenPreview() {
    AddCarScreen(
        navHostController = rememberNavController(),
        carViewModel = viewModel()
    )
}
