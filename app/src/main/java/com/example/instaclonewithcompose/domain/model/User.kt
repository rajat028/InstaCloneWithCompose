package com.example.instaclonewithcompose.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val email: String,
    val password: String,
    val name: String = "user",
    val age: Int = 18,
)
data class InvalidCredentialsException(val exceptionMessage: String) : Exception()
