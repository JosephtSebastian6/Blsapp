package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Unidad
import com.example.bls.data.repository.UnidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UnidadesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val token: String = savedStateHandle.get<String>("AUTH_TOKEN") ?: ""
    private val userRole: String = savedStateHandle.get<String>("USER_ROLE") ?: ""
    private val unidadesRepository = UnidadRepository(token)
    private val _unidadesState = MutableStateFlow<UnidadesState>(UnidadesState.Loading)
    val unidadesState: StateFlow<UnidadesState> = _unidadesState

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadUnidades() {
        viewModelScope.launch {
            _unidadesState.value = UnidadesState.Loading
            try {
                // Lógica diferenciada por rol
                val response = if (userRole.equals("estudiante", ignoreCase = true)) {
                    unidadesRepository.getUnidadesEstudiante()
                } else {
                    unidadesRepository.getUnidades()
                }

                if (response.isSuccessful && response.body() != null) {
                    _unidadesState.value = UnidadesState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    // Si el error es 401/403 y es estudiante, ya estamos usando el endpoint correcto,
                    // así que si falla es un error real.
                    _unidadesState.value = UnidadesState.Error("Error ${response.code()}: $errorBody")
                }
            } catch (t: Throwable) {
                _unidadesState.value = UnidadesState.Error("Error de conexión: ${t.message}")
            }
        }
    }

    fun crearUnidad(nombre: String, descripcion: String?) {
        if (userRole.equals("estudiante", ignoreCase = true)) {
            _errorMessage.value = "Los estudiantes no pueden crear unidades"
            return
        }
        viewModelScope.launch {
            val result = unidadesRepository.crearUnidad(nombre, descripcion)
            if (result.isSuccess) {
                _successMessage.value = "Unidad creada correctamente"
                loadUnidades()
            } else {
                _errorMessage.value = "Error creando unidad: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun clearMessages() {
        _successMessage.value = null
        _errorMessage.value = null
    }
}

sealed class UnidadesState {
    object Loading : UnidadesState()
    data class Success(val unidades: List<Unidad>) : UnidadesState()
    data class Error(val message: String) : UnidadesState()
}
