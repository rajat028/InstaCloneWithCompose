package com.example.instaclonewithcompose

enum class Screen {
    HOME,
    LOGIN,
    SIGNUP
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Signup : NavigationItem(Screen.SIGNUP.name)
}