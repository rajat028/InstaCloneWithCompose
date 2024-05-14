package com.example.instaclonewithcompose.data.repository

import com.example.instaclonewithcompose.data.room.UserDao
import com.example.instaclonewithcompose.domain.model.User
import com.example.instaclonewithcompose.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {
    
    override suspend fun upsertUser(user: User) {
        userDao.upsertUser(user)
    }
    
    override suspend fun getUserDetails(email: String): User? {
        return userDao.getUserDetails(email)
    }
    
    override suspend fun isEmailExist(email: String): Boolean {
        return userDao.isEmailExist(email) != 0
    }
}