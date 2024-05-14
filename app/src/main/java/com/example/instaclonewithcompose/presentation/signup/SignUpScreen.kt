package com.example.instaclonewithcompose.presentation.signup

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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.instaclonewithcompose.presentation.components.GradientBox
import com.example.instaclonewithcompose.presentation.components.PasswordTextField
import com.example.instaclonewithcompose.presentation.components.UsernameTextField
import com.example.instaclonewithcompose.utils.isSmallScreenHeight
import com.example.instaclonewithcompose.utils.rememberImeState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onLogInClick: () -> Unit,
    onSignUpSuccessful: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val isImeVisible by rememberImeState()
    val credentialsState = viewModel.credentialsState.value
    val context = LocalContext.current
    
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                SignUpViewModel.UiEvent.RegisterUser -> {
                    Toast.makeText(context, "Sign Up successful", Toast.LENGTH_SHORT).show()
                    onSignUpSuccessful()
                }
                
                is SignUpViewModel.UiEvent.ShowSnackBar -> {
                    Toast.makeText(context, event.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    @Composable
    fun existingUserLogin() {
        Spacer(modifier = Modifier.height(12.dp))
        
        val login = "Log In"
        val annotatedString = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                pushStringAnnotation(tag = login, annotation = login)
                append(login)
            }
        }
        ClickableText(text = annotatedString, onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { _ ->
                    onLogInClick.invoke()
                }
        })
    }
    
    Scaffold { paddingValues ->
        GradientBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
        ) {
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
                        text = "Sign Up",
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
                            viewModel.onEvent(SignupEvent.EnteredEmail(it))
                        },
                        text = credentialsState.email
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PasswordTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        label = "Password",
                        keyboardActions = KeyboardActions(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.onEvent(SignupEvent.EnteredPassword(it))
                        },
                        text = credentialsState.password
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PasswordTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        label = "Confirm Password",
                        keyboardActions = KeyboardActions(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = {
                            viewModel.onEvent(SignupEvent.EnteredConfirmPassword(it))
                        },
                        text = credentialsState.confirmPassword
                    )
                    
                    if (isImeVisible) {
                        Button(
                            onClick = { viewModel.onEvent(SignupEvent.OnSignUp) },
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
                                text = "Sign Up",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                            )
                        }
                        existingUserLogin()
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { viewModel.onEvent(SignupEvent.OnSignUp) },
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 44.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF004C92),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Sign Up",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500)
                                    )
                                )
                            }
                            existingUserLogin()
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