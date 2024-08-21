package com.good.cars.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.good.cars.app.dao.CarDao
import com.good.cars.app.dao.ReminderDao


@Database(entities = [Car::class, Reminder::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun reminderDao(): ReminderDao
}