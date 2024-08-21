package com.good.cars.app.ui.viewModel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.good.cars.app.R
import com.good.cars.app.dao.ReminderDao
import com.good.cars.app.database.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderDao: ReminderDao
) : ViewModel() {

    val reminders: Flow<List<Reminder>> = reminderDao.getAllReminders()

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderDao.insertReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderDao.deleteReminder(reminder)
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderDao.updateReminder(reminder)
        }
    }

    fun getRemindersForCar(carId: Int): Flow<List<Reminder>> {
        return reminderDao.getRemindersForCar(carId)
    }

    fun deleteAllReminders() {
        viewModelScope.launch {
            reminderDao.deleteAllReminders()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNotification(context: Context, carName: String, date: LocalDate?, time: LocalTime?) {
        if (date != null && time != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    context.startActivity(intent)
                    return
                }
            }

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("carName", carName)
                putExtra("reminderDate", date.toString())
                putExtra("reminderTime", time.toString())
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, date.year)
                set(Calendar.MONTH, date.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                set(Calendar.HOUR_OF_DAY, time.hour)
                set(Calendar.MINUTE, time.minute)
                set(Calendar.SECOND, 0)
            }

            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Toast.makeText(context, "Напоминание установлено", Toast.LENGTH_SHORT).show()
            } catch (e: SecurityException) {
                Toast.makeText(context, "Необходимо разрешение на точные будильники", Toast.LENGTH_LONG).show()
            }
        }
    }


}


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val carName = intent.getStringExtra("carName")
        val reminderDate = intent.getStringExtra("reminderDate")
        val reminderTime = intent.getStringExtra("reminderTime")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (0..Int.MAX_VALUE).random()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for car reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Пройти ТО")
            .setContentText("Напоминание о прохождении ТО для автомобиля $carName.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}


