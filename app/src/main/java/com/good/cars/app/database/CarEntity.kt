package com.good.cars.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val year: Int,
    val mileage: Int,
    val fuelExpense: Int,
    val repairExpense: Int,
    val repairDate: String,
    val carIcon: Int
)
