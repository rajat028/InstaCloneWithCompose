package com.example.instaclonewithcompose.presentation.login

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instaclonewithcompose.domain.model.InvalidCredentialsException
import com.example.instaclonewithcompose.domain.usecases.LoginUseCase
import com.example.instaclonewithcompose.presentation.states.LoginCredentialsState
import com.example.instaclonewithcompose.utils.Constants.EMAIL
import com.example.instaclonewithcompose.utils.PrefConstants.LOGGED_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: LoginUseCase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    
    private val _credentialsState = mutableStateOf(LoginCredentialsState())
    val credentialsState: State<LoginCredentialsState> = _credentialsState
    
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    
    init {
        Log.e("Save State Email : ", "${savedStateHandle.get<String>(EMAIL)}")
    }
    
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredEmail -> {
                _credentialsState.value = _credentialsState.value.copy(email = event.value)
            }
            
            is LoginEvent.EnteredPassword -> {
                _credentialsState.value = _credentialsState.value.copy(password = event.value)
            }
            
            LoginEvent.OnLogin -> {
                loginUser()
            }
        }
    }
    
    private fun loginUser() {
        viewModelScope.launch {
            try {
                val loginSuccessful = useCase.loginUser(
                    _credentialsState.value.email,
                    _credentialsState.value.password
                )
                if (loginSuccessful) {
                    _eventFlow.emit(UiEvent.LoginUser)
                    sharedPreferences.edit().putBoolean(LOGGED_IN, true).apply()
                }
            } catch (exception: InvalidCredentialsException) {
                _eventFlow.emit(UiEvent.ShowToast(exception.exceptionMessage))
            }
        }
    }
    
    sealed class UiEvent {
        data class ShowToast(val message: String?) : UiEvent()
        object LoginUser : UiEvent()
    }
}