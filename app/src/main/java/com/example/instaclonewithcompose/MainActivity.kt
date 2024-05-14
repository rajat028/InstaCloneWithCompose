package com.example.instaclonewithcompose

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.instaclonewithcompose.presentation.Home
import com.example.instaclonewithcompose.presentation.login.LoginScreen
import com.example.instaclonewithcompose.presentation.signup.SignUpScreen
import com.example.instaclonewithcompose.utils.Constants.EMAIL
import com.example.instaclonewithcompose.utils.PrefConstants.LOGGED_IN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppController(sharedPreferences)
        }
    }
}

@Composable
fun AppController(sharedPreferences: SharedPreferences) {
    val isUserLoggedIn = sharedPreferences.getBoolean(LOGGED_IN, false)
    var startDestination by remember { mutableStateOf(NavigationItem.Signup.route) }
    if(isUserLoggedIn) {
        startDestination = NavigationItem.Home.route
    }
    
    fun navigateToLogin(navController: NavHostController) {
        navController.navigate(NavigationItem.Login.route + "/rajat.arora@example.com")
    }
    
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationItem.Signup.route) {
            SignUpScreen(onLogInClick = {
                navigateToLogin(navController)
            }, onSignUpSuccessful = {
                navController.popBackStack()
                navigateToLogin(navController)
            })
        }
        composable(NavigationItem.Login.route + "/{$EMAIL}", arguments = listOf(
            navArgument(name = EMAIL) {
                type = NavType.StringType
            }
        )) { it ->
            it.arguments?.getString(EMAIL)?.let { email ->
                Log.e("EMAIL : ", email)
            }
            LoginScreen(onLoginSuccessful = {
                navController.navigate(NavigationItem.Home.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                startDestination = NavigationItem.Home.route
            })
        }
        
        composable(NavigationItem.Home.route) {
            Home()
        }
    }
}

