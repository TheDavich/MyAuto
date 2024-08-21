package com.good.cars.app.ui.screens

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.good.cars.app.MainActivity
import com.good.cars.app.R
import com.good.cars.app.database.Reminder
import com.good.cars.app.ui.composables.CustomBottomBar
import com.good.cars.app.ui.composables.CustomDropdownMenu
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.ui.viewModel.ReminderViewModel
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderScreen(
    navHostController: NavHostController,
    carViewModel: CarViewModel,
    reminderViewModel: ReminderViewModel,
    context: Context
) {
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    var selectedCarId by rememberSaveable { mutableStateOf<Int?>(null) }
    var bottomNavState by rememberSaveable { mutableStateOf(1) }
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var selectedTime by rememberSaveable { mutableStateOf<LocalTime?>(null) }
    var isDateDialogShown by rememberSaveable { mutableStateOf(false) }
    var isTimeDialogShown by rememberSaveable { mutableStateOf(false) }

    val selectedCar = cars.find { it.id == selectedCarId }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffff5c00),
        bottomBar = {
            CustomBottomBar(
                items = listOf(
                    NavItemState(
                        unselectedIcon = painterResource(id = R.drawable.car_nav)
                    ),
                    NavItemState(
                        unselectedIcon = painterResource(id = R.drawable.calendar_nav)
                    ),
                    NavItemState(
                        unselectedIcon = painterResource(id = R.drawable.settings_nav)
                    )
                ),
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
                text = "Создать напоминание",
                fontSize = 28.sp,
                fontWeight = FontWeight.W700,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(
                        text = "Укажите день, в который планируете пройти ТО",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    CustomTextField(
                        hint = selectedDate?.toString() ?: "Выберите дату",
                        onClick = { isDateDialogShown = true }
                    )
                }
                Column {
                    Text(
                        text = "Выберите автомобиль, для которого необходимо ТО",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    CustomDropdownMenu(
                        hint = "Выберите автомобиль",
                        options = cars.map { "${it.name}, ${it.year}" },
                        onOptionSelected = { selectedOptionIndex ->
                            selectedCarId = cars[selectedOptionIndex].id
                            carViewModel.selectCar(cars[selectedOptionIndex])
                        }
                    )
                }
                Column {
                    Text(
                        text = "Укажите время для напоминания",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    CustomTextField(
                        hint = selectedTime?.toString() ?: "Выберите время",
                        onClick = { isTimeDialogShown = true }
                    )
                }
                Button(
                    onClick = {
                        if (selectedCar != null && selectedDate != null && selectedTime != null) {
                            val reminder = Reminder(
                                carId = selectedCar.id,
                                reminderDate = selectedDate.toString(),
                                reminderTime = selectedTime.toString()
                            )
                            reminderViewModel.addReminder(reminder)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    reminderViewModel.scheduleNotification(context, selectedCar.name, selectedDate, selectedTime)
                                } else {
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                        MainActivity.REQUEST_NOTIFICATION_PERMISSION
                                    )
                                }
                            } else {
                                reminderViewModel.scheduleNotification(context, selectedCar.name, selectedDate, selectedTime)
                            }

                            navHostController.navigateUp()
                        } else {
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff712800)
                    )
                ) {
                    Text(
                        text = "Создать напоминание",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }

    if (isDateDialogShown) {
        DatePickerDialog(
            onDismissRequest = { isDateDialogShown = false },
            onDateChange = { date ->
                selectedDate = date
                isDateDialogShown = false
            },
            title = { Text("Выберите дату") }
        )
    }

    if (isTimeDialogShown) {
        TimePickerDialog(
            onDismissRequest = { isTimeDialogShown = false },
            onTimeChange = { time ->
                selectedTime = time
                isTimeDialogShown = false
            },
            title = { Text("Выберите время") }
        )
    }
}



@Composable
fun CustomTextField(
    hint: String = "",
    onClick: () -> Unit,
) {
    TextField(
        value = if (hint.isEmpty()) "" else hint,
        onValueChange = {},
        readOnly = true,
        placeholder = {
            Text(
                text = hint,
                color = Color(0xff97a2b0),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            cursorColor = Color(0xff974900),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(textAlign = TextAlign.Start, color = Color(0xff97a2b0)),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color(0xff974900),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        enabled = false
    )
}
