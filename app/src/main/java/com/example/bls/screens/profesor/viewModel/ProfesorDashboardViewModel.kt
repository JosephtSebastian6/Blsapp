package com.example.bls.screens.profesor.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.UsuarioApi
import com.example.bls.data.network.UsuarioResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfesorDashboardViewModel(private val username: String, private val token: String) : ViewModel() {

    private val usuarioApi: UsuarioApi = ApiConfig.client { token }.create(UsuarioApi::class.java)

    private val _uiState = MutableStateFlow<UsuarioResponse?>(null)
    val uiState: StateFlow<UsuarioResponse?> = _uiState

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = usuarioApi.getUsuario(username)
                if (response.isSuccessful) {
                    _uiState.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateProfile(profileData: Map<String, Any?>) {
        viewModelScope.launch {
            try {
                val response = usuarioApi.updatePerfil(profileData)
                if (response.isSuccessful) {
                    _uiState.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}