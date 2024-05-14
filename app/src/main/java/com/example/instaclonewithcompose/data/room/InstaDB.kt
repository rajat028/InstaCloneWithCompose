package com.example.instaclonewithcompose.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.instaclonewithcompose.domain.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class InstaDB : RoomDatabase() {
    abstract val userDao: UserDao
    
    companion object {
        const val DATABASE_NAME = "insta_db"
    }
}
