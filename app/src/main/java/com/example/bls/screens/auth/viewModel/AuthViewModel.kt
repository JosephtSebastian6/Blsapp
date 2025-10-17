package com.example.bls.screens.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.UserRole
import com.example.bls.data.model.LoginRequest
import com.example.bls.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = authRepository.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val userRole = mapRole(response.body()!!.userType)
                    if (userRole != null) {
                        _loginState.value = LoginState.Success(userRole)
                    } else {
                        _loginState.value = LoginState.Error("Rol de usuario desconocido")
                    }
                } else {
                    _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    private fun mapRole(role: String): UserRole? {
        return when (role.lowercase()) {
            "empresa" -> UserRole.EMPRESA
            "administrador" -> UserRole.ADMINISTRADOR
            "estudiante" -> UserRole.ESTUDIANTE
            "profesor" -> UserRole.PROFESOR
            else -> null
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val role: UserRole) : LoginState()
    data class Error(val message: String) : LoginState()
}
