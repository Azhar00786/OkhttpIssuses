package com.example.okhttpissues.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.okhttpissues.data.database_entities.CommentDataEntity
import com.example.okhttpissues.data.database_entities.IssuesEntity

@Database(
    entities = [IssuesEntity::class, CommentDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseGetter : RoomDatabase() {
    abstract fun IssuesDao(): IssuesDao
    abstract fun CommentDao(): CommentDao


    companion object {
        @Volatile
        var INSTANCE: DatabaseGetter? = null
        fun getInstance(context: Context): DatabaseGetter {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, DatabaseGetter::class.java, "AppDB"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}