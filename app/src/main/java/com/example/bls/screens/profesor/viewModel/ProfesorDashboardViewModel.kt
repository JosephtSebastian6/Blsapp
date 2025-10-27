package com.example.bls.screens.profesor.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import com.example.bls.data.network.UsuarioResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfesorDashboardViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val username: String = savedStateHandle.get<String>("username") ?: ""
    private val token: String = savedStateHandle.get<String>("AUTH_TOKEN") ?: ""
    private val apiService: ApiService = ApiConfig.client { token }.create(ApiService::class.java)

    private val _uiState = MutableStateFlow<UsuarioResponse?>(null)
    val uiState: StateFlow<UsuarioResponse?> = _uiState

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            if (username.isNotEmpty() && token.isNotEmpty()) {
                try {
                    val response = apiService.getUsuario(username)
                    if (response.isSuccessful) {
                        _uiState.value = response.body()
                    }
                } catch (_: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun updateProfile(profileData: Map<String, Any?>) {
        viewModelScope.launch {
            if (token.isNotEmpty()) {
                try {
                    val response = apiService.updatePerfil(profileData)
                    if (response.isSuccessful) {
                        _uiState.value = response.body()
                    }
                } catch (_: Exception) {
                    // Handle error
                }
            }
        }
    }
}
