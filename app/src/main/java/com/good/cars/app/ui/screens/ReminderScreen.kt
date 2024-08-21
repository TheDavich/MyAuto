package com.good.cars.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.good.cars.app.R
import com.good.cars.app.database.Reminder
import com.good.cars.app.ui.composables.CustomBottomBar
import com.good.cars.app.ui.viewModel.CarViewModel
import com.good.cars.app.ui.viewModel.ReminderViewModel
import com.good.cars.app.util.CARS_SCREEN
import com.good.cars.app.util.CREATE_REMINDER_SCREEN
import com.good.cars.app.util.REMINDER_SCREEN
import com.good.cars.app.util.SETTINGS_SCREEN



@Composable
fun ReminderScreen(
    navHostController: NavHostController,
    reminderViewModel: ReminderViewModel,
    carViewModel: CarViewModel
) {
    val reminders by reminderViewModel.reminders.collectAsState(initial = emptyList())
    val cars by carViewModel.cars.collectAsState(initial = emptyList())
    var bottomNavState by rememberSaveable { mutableStateOf(1) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffff5c00),
        bottomBar = {
            CustomBottomBar(
                items = listOf(
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.car_nav)),
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.calendar_nav)),
                    NavItemState(unselectedIcon = painterResource(id = R.drawable.settings_nav))
                ),
                selectedIndex = bottomNavState,
                onItemSelected = { index ->
                    bottomNavState = index
                    when (index) {
                        0 -> navHostController.navigate(CARS_SCREEN)
                        1 -> navHostController.navigate(REMINDER_SCREEN)
                        2 -> navHostController.navigate(SETTINGS_SCREEN)
                    }
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
                text = "Напоминание ТО",
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
                reminders.forEach { reminder ->
                    val car = cars.find { it.id == reminder.carId }
                    if (car != null) {
                        ReminderCard(
                            carName = car.name,
                            carYear = car.year,
                            reminderDate = reminder.reminderDate,
                            onDeleteClick = {
                                reminderToDelete = reminder
                                showDialog = true
                            }
                        )
                    }
                }
                CreateReminderCard {
                    navHostController.navigate(CREATE_REMINDER_SCREEN)
                }
            }
        }
    }

    if (showDialog && reminderToDelete != null) {
        showDeleteReminderDialog(
            reminder = reminderToDelete!!,
            onDismiss = { showDialog = false },
            onConfirm = {
                reminderViewModel.deleteReminder(reminderToDelete!!)
                showDialog = false
            }
        )
    }
}

@Composable
fun showDeleteReminderDialog(
    reminder: Reminder,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Вы уверены?")
        },
        text = {
            Text(text = "Вы действительно хотите удалить это напоминание?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}

@Composable
fun CreateReminderCard(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff712800)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            text = "Создать напоминание",
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        )
    }
}


@Composable
fun ReminderCard(
    carName: String,
    carYear: Int,
    reminderDate: String,
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff712800)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "ТО для $carName, $carYear",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
                )
                Text(
                    text = "Дата прохождения ТО: $reminderDate",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.close_buttonm),
                contentDescription = "Close",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .clickable { onDeleteClick() }
            )
        }
    }
}
