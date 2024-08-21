package com.good.cars.app.di

import android.app.Application
import androidx.room.Room
import com.good.cars.app.dao.CarDao
import com.good.cars.app.dao.ReminderDao
import com.good.cars.app.database.AppDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCarDao(database: AppDatabase): CarDao {
        return database.carDao()
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }
}
