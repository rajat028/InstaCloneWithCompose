package com.example.instaclonewithcompose.domain.usecases

import com.example.instaclonewithcompose.domain.model.InvalidCredentialsException
import com.example.instaclonewithcompose.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    
    suspend fun loginUser(email: String, password: String): Boolean {
        val registeredUser = userRepository.getUserDetails(email)
        if (registeredUser == null || registeredUser.password != password) {
            throw InvalidCredentialsException("Invalid credentials")
        }
        return true
    }
}