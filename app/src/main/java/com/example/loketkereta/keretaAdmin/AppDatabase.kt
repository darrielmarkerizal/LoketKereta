package com.example.loketkereta.keretaAdmin

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Kereta::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keretaDao(): KeretaDao
}