package com.example.bls.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.UsuarioResponse
import com.example.bls.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val token: String = savedStateHandle.get<String>("AUTH_TOKEN") ?: ""
    private val username: String = savedStateHandle.get<String>("username") ?: "" 
    
    private val repository = ProfileRepository(token)

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        if (username.isNotEmpty()) {
            cargarPerfil(username)
        } else {
             _uiState.value = ProfileUiState.Error("No se especificó el usuario")
        }
    }

    fun cargarPerfil(user: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            val result = repository.getUsuario(user)
            if (result.isSuccess) {
                _uiState.value = ProfileUiState.Success(result.getOrNull()!!)
            } else {
                _uiState.value = ProfileUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val usuario: UsuarioResponse) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
