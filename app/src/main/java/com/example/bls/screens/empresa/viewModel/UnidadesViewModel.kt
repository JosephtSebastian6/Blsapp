package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Unidad
import com.example.bls.data.repository.UnidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UnidadesViewModel : ViewModel() {

    private val unidadesRepository = UnidadRepository()
    private val _unidadesState = MutableStateFlow<UnidadesState>(UnidadesState.Loading)
    val unidadesState: StateFlow<UnidadesState> = _unidadesState

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadUnidades(token: String) {
        viewModelScope.launch {
            _unidadesState.value = UnidadesState.Loading
            try {
                val response = unidadesRepository.getUnidades(token)
                if (response.isSuccessful && response.body() != null) {
                    _unidadesState.value = UnidadesState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    _unidadesState.value = UnidadesState.Error("Error ${response.code()}: $errorBody")
                }
            } catch (t: Throwable) {
                _unidadesState.value = UnidadesState.Error("Error de conexión: ${t.message}")
            }
        }
    }

    fun crearUnidad(nombre: String, descripcion: String?, token: String) {
        viewModelScope.launch {
            val result = unidadesRepository.crearUnidad(nombre, descripcion, token)
            if (result.isSuccess) {
                _successMessage.value = "Unidad creada correctamente"
                loadUnidades(token)
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
