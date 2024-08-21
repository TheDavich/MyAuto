package com.good.cars.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.good.cars.app.R
import com.good.cars.app.prefs.PreferenceManager
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.util.ADD_CAR_SCREEN
import com.good.cars.app.util.CARS_SCREEN
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    carViewModel: CarViewModel,
) {
    val pageCount = 4
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val coroutineScope = rememberCoroutineScope()

    var carName by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf(0) }
    var carMileage by remember { mutableStateOf(0) }
    var fuelExpense by remember { mutableStateOf(0) }
    var repairExpense by remember { mutableStateOf(0) }
    var repairDate by remember {
        mutableStateOf("")
    }
    var totalExpense by remember { mutableStateOf(0) }
    var carIcon by remember { mutableStateOf(0) }
    val context = LocalContext.current



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffff5c00))
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingOne()
                    1 -> OnboardingTwo(
                        carName = carName,
                        carYear = carYear,
                        carMileage = carMileage,
                        onCarDataChange = { name, year, mileage ->
                            carName = name
                            carYear = year
                            carMileage = mileage
                        },
                        titleText = "Давай добавим твою первую машину"
                    )


                    2 -> OnboardingThree(
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

                    3 -> OnboardingFour()
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Column {
                    CustomDotsIndicator(
                        currentPage = pagerState.currentPage,
                        pageCount = pageCount
                    )
                    Text(
                        text = "Пропустить",
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable {
                                val preferenceManager = PreferenceManager(context)
                                preferenceManager.setOnboardingCompleted(true)

                                navHostController.navigate(CARS_SCREEN) {
                                    popUpTo(ADD_CAR_SCREEN) { inclusive = true }
                                }
                            }
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(64.dp)
                ) {
                    CircularProgressIndicator(
                        progress = (pagerState.currentPage + 1) / pageCount.toFloat(),
                        strokeWidth = 4.dp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxSize()
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

                                    val preferenceManager = PreferenceManager(context)
                                    preferenceManager.setOnboardingCompleted(true)

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
                        modifier = Modifier.size(48.dp),
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
fun OnboardingFour() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffff5c00)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Готово! Теперь можем перейти в приложение",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))


        Image(
            painter = painterResource(id = R.drawable.blue_car_icon),
            contentDescription = "Blue car",
            modifier = Modifier
                .padding(horizontal = 45.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun OnboardingThree(
    fuelExpense: Int,
    repairExpense: Int,
    onExpensesChange: (Int, Int) -> Unit,
    onIconSelected: (Int) -> Unit,
    repairDate: String,
    onRepairDateChange: (String) -> Unit,
) {
    var fuel by remember { mutableStateOf(fuelExpense.toString()) }
    var repair by remember { mutableStateOf(repairExpense.toString()) }
    var selectedOption by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        onIconSelected(selectedOption)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffff5c00))
            .padding(16.dp)
    ) {
        Text(
            text = "Еще немного!",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(
                bottom = 24.dp,
                top = 32.dp
            )
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Укажите примерный расход на бензин в месяц",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = fuel,
                    onValueChange = {
                        fuel = it
                        onExpensesChange(fuel.toIntOrNull() ?: 0, repair.toIntOrNull() ?: 0)
                    },
                    label = {
                        Text("Например, 15000", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Сколько примерно вы тратите на ремонт машины в месяц?",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = repair,
                    onValueChange = {
                        repair = it
                        onExpensesChange(fuel.toIntOrNull() ?: 0, repair.toIntOrNull() ?: 0)
                    },
                    label = {
                        Text("Например, 15000", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Когда последний раз проходили ТО?",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = repairDate,
                    onValueChange = { date ->
                        onRepairDateChange(date)
                    },
                    label = {
                        Text("Например, 12.12.2023", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Выберите иконку для автомобиля",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        50.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 2.dp,
                                    color = Color(0xff964900),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background(Color.White)
                                .clickable {
                                    selectedOption = 1
                                    onIconSelected(selectedOption)
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.white_car_icon),
                                contentDescription = "White car",
                                modifier = Modifier.size(125.dp)
                            )
                        }
                        RadioButton(
                            selected = selectedOption == 1,
                            onClick = {
                                selectedOption = 1
                                onIconSelected(selectedOption)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 8.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 2.dp,
                                    color = Color(0xff964900),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .background(Color.White)
                                .clickable {
                                    selectedOption = 2
                                    onIconSelected(selectedOption)
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.black_car_icon),
                                contentDescription = "Black car",
                                modifier = Modifier.size(125.dp)
                            )
                        }
                        RadioButton(
                            selected = selectedOption == 2,
                            onClick = {
                                selectedOption = 2
                                onIconSelected(selectedOption)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White,
                                unselectedColor = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun OnboardingTwo(
    carName: String,
    carYear: Int,
    carMileage: Int,
    onCarDataChange: (String, Int, Int) -> Unit,
    titleText: String
) {
    var name by remember { mutableStateOf(carName) }
    var year by remember { mutableStateOf(carYear.toString()) }
    var mileage by remember { mutableStateOf(carMileage.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffff5c00))
            .padding(16.dp)
    ) {
        Text(
            text = titleText,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .padding(bottom = 24.dp, top = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Введите полное название машины",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        onCarDataChange(name, year.toIntOrNull() ?: 0, mileage.toIntOrNull() ?: 0)
                    },
                    label = {
                        Text("Например, Lada XRay", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Укажите год машины",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = year,
                    onValueChange = {
                        year = it
                        onCarDataChange(name, year.toIntOrNull() ?: 0, mileage.toIntOrNull() ?: 0)
                    },
                    label = {
                        Text("Например, 2007", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Укажите пробег машины",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
                TextField(
                    value = mileage,
                    onValueChange = {
                        mileage = it
                        onCarDataChange(name, year.toIntOrNull() ?: 0, mileage.toIntOrNull() ?: 0)
                    },
                    label = {
                        Text("Например, 120000", color = Color(0xff97a2b0))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color(0xff974900),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color(0xff974900),
                        unfocusedTextColor = Color(0xff974900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xff974900),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}



@Composable
fun OnboardingOne() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
        ) {
            Text(
                text = "Следи за своей машиной правильно",
                color = Color.White,
                fontWeight = FontWeight.W700,
                fontSize = 27.sp,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                text = "Пробег, ремонт, бензин, ТО - все в одном приложении",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.yellow_car),
            contentDescription = "",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun CustomDotsIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int,
    normalDotColor: Color = Color.White.copy(alpha = 0.45f),
    selectedDotColor: Color = Color.White,
    normalDotSize: Dp = 8.dp,
    selectedDotWidth: Dp = 24.dp,
    selectedDotHeight: Dp = 8.dp,
    spacing: Dp = 8.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until pageCount) {
            if (i == currentPage) {
                Box(
                    modifier = modifier
                        .requiredWidth(selectedDotWidth)
                        .requiredHeight(selectedDotHeight)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = selectedDotColor)
                )
            } else {
                Box(
                    modifier = modifier
                        .size(normalDotSize)
                        .background(color = normalDotColor, shape = CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        navHostController = rememberNavController(),
        carViewModel = hiltViewModel()
    )
}