package com.weatherapp.db.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( entities = [LocalCity::class], version = 1, exportSchema = false)
abstract class LocalRoomDatabase: RoomDatabase() {
    abstract fun localCityDao() : LocalCityDAO
}