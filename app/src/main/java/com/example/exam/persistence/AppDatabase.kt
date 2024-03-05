package com.example.exam.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exam.model.Car

@Database(entities = [Car::class], version = 16, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun AppDao(): AppDao
}