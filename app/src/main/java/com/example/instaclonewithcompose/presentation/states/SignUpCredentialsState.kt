package com.example.instaclonewithcompose.presentation.states

data class SignUpCredentialsState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
)
