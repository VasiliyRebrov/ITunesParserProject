package com.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.data.common.SingletonHolder

@Database(entities = [Album::class, Track::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun dao(): AppDao

    companion object : SingletonHolder<AppDataBase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDataBase::class.java, "app.db"
        ).build()
    })
}