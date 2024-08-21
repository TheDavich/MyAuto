package com.good.cars.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.good.cars.app.dao.CarDao
import com.good.cars.app.database.Car
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CarViewModel @Inject constructor(
    private val carDao: CarDao
) : ViewModel() {
    val cars: Flow<List<Car>> = carDao.getAllCars()

    private val _selectedCar = MutableStateFlow<Car?>(null)
    val selectedCar: StateFlow<Car?> get() = _selectedCar

    fun selectCar(car: Car) {
        _selectedCar.value = car
    }

    fun addCar(
        name: String,
        year: Int,
        mileage: Int,
        fuelExpense: Int,
        repairExpense: Int,
        repairDate: String,
        carIcon: Int
    ) {
        val car = Car(
            name = name,
            year = year,
            mileage = mileage,
            fuelExpense = fuelExpense,
            repairExpense = repairExpense,
            repairDate = repairDate,
            carIcon = carIcon
        )
        viewModelScope.launch {
            carDao.insertCar(car)
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            carDao.deleteCar(car)
        }
    }

    fun deleteAllCars() {
        viewModelScope.launch {
            carDao.deleteAllCars()
        }
    }
}

