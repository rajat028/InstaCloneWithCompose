package com.example.instaclonewithcompose.presentation.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclonewithcompose.domain.model.InvalidCredentialsException
import com.example.instaclonewithcompose.domain.usecases.SignUpUseCase
import com.example.instaclonewithcompose.presentation.states.SignUpCredentialsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val useCase: SignUpUseCase) : ViewModel() {
    
    private val _credentialsState = mutableStateOf(SignUpCredentialsState())
    val credentialsState: State<SignUpCredentialsState> = _credentialsState
    
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    
    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EnteredEmail -> {
                _credentialsState.value = _credentialsState.value.copy(email = event.value)
            }
            
            is SignupEvent.EnteredPassword -> {
                _credentialsState.value = _credentialsState.value.copy(password = event.value)
            }
            
            is SignupEvent.EnteredConfirmPassword -> {
                _credentialsState.value =
                    _credentialsState.value.copy(confirmPassword = event.value)
            }
            
            is SignupEvent.OnSignUp -> {
                registerUser()
            }
        }
    }
    
    private fun registerUser() {
        viewModelScope.launch {
            try {
                useCase.registerUser(
                    email = _credentialsState.value.email,
                    password = _credentialsState.value.password,
                    confirmPassword = _credentialsState.value.confirmPassword
                )
                _eventFlow.emit(UiEvent.RegisterUser)
            } catch (exception: InvalidCredentialsException) {
                _eventFlow.emit(UiEvent.ShowSnackBar(exception.exceptionMessage))
            }
        }
    }
    
    sealed class UiEvent {
        data class ShowSnackBar(val message: String?) : UiEvent()
        object RegisterUser : UiEvent()
    }
}