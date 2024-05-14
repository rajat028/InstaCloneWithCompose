package com.example.instaclonewithcompose.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.instaclonewithcompose.domain.model.User

@Dao
interface UserDao {
    
    @Upsert
    suspend fun upsertUser(user: User)
    
    @Query("Select * from User where email = :emailId")
    suspend fun getUserDetails(emailId: String) : User?
    
    @Query("SELECT COUNT() FROM User WHERE email = :emailId")
    suspend fun isEmailExist(emailId: String): Int
}