package com.nezuko.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nezuko.data.db.dao.FileDao
import com.nezuko.data.db.entity.FileEntity
import kotlin.jvm.java

@Database(entities = [FileEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun fileDao(): FileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getDatabase(context: Context): AppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "file_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}