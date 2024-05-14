package com.example.instaclonewithcompose.presentation.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.instaclonewithcompose.presentation.components.GradientBox
import com.example.instaclonewithcompose.presentation.components.PasswordTextField
import com.example.instaclonewithcompose.presentation.components.UsernameTextField
import com.example.instaclonewithcompose.utils.isSmallScreenHeight
import com.example.instaclonewithcompose.utils.rememberImeState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onLoginSuccessful: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val isImeVisible by rememberImeState()
    val credentialsState = viewModel.credentialsState.value
    val context = LocalContext.current
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                LoginViewModel.UiEvent.LoginUser -> {
                    Toast.makeText(context, "LogIn successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccessful()
                }
                
                is LoginViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    GradientBox(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val animatedUpperSectionRatio by animateFloatAsState(
                targetValue = getHeightAccordingToKeyboardVisibility(
                    isImeVisible
                ), label = ""
            )
            AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(animatedUpperSectionRatio),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welcome To InstaClone",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isSmallScreenHeight()) {
                    Spacer(modifier = Modifier.fillMaxSize(0.05f))
                } else {
                    Spacer(modifier = Modifier.fillMaxSize(0.01f))
                }
                
                Text(
                    text = "Log in",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                
                if (isSmallScreenHeight()) {
                    Spacer(modifier = Modifier.fillMaxSize(0.05f))
                } else {
                    Spacer(modifier = Modifier.fillMaxSize(0.01f))
                }
                
                UsernameTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    label = "Username",
                    keyboardActions = KeyboardActions(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onValueChange = {
                        viewModel.onEvent(LoginEvent.EnteredEmail(it))
                    },
                    text = credentialsState.email
                )
                Spacer(modifier = Modifier.height(20.dp))
                
                PasswordTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    label = "Password",
                    keyboardActions = KeyboardActions(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = {
                        viewModel.onEvent(LoginEvent.EnteredPassword(it))
                    },
                    text = credentialsState.password
                )
                
                if (isImeVisible) {
                    Button(
                        onClick = { viewModel.onEvent(LoginEvent.OnLogin)  },
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp)
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF004C92),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Log in",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                        )
                    }
                    
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Button(
                            onClick = { viewModel.onEvent(LoginEvent.OnLogin) },
                            Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF004C92),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Log in",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getHeightAccordingToKeyboardVisibility(isImeVisible: Boolean) =
    if (isImeVisible) 0f else 0.35f