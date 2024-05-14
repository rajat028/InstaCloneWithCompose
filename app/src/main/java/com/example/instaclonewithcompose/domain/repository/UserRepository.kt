package com.example.instaclonewithcompose.domain.repository

import com.example.instaclonewithcompose.domain.model.User


interface UserRepository {
    
    suspend fun upsertUser(user: User)
    suspend fun getUserDetails(email: String) : User?
    suspend fun isEmailExist(email: String) : Boolean
}