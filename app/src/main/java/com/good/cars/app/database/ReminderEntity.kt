package com.good.cars.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val reminderDate: String, // format "dd.MM.yyyy"
    val reminderTime: String  // format "HH:mm"
)