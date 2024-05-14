package com.example.instaclonewithcompose.domain.usecases

import com.example.instaclonewithcompose.domain.model.InvalidCredentialsException
import com.example.instaclonewithcompose.domain.model.User
import com.example.instaclonewithcompose.domain.repository.UserRepository
import com.example.instaclonewithcompose.utils.isValidEmail
import com.example.instaclonewithcompose.utils.isValidPassword

class SignUpUseCase(private val repository: UserRepository) {
    
    suspend fun registerUser(email: String, password: String, confirmPassword: String) {
        if (email.isValidEmail().not()) {
            throw InvalidCredentialsException("Invalid Email")
        } else if (password.isValidPassword().not()) {
            throw InvalidCredentialsException("Invalid Password")
        } else if (password != confirmPassword) {
            throw InvalidCredentialsException("Password must be same")
        } else {
            if (repository.isEmailExist(email)) {
                throw InvalidCredentialsException("Email already exist")
            } else {
                repository.upsertUser(User(email, password))
            }
        }
    }
}