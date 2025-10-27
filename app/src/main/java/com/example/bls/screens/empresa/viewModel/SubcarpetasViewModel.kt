package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.repository.UnidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubcarpetasViewModel : ViewModel() {

    private val unidadRepository = UnidadRepository()
    private val _subcarpetasState = MutableStateFlow<SubcarpetasState>(SubcarpetasState.Loading)
    val subcarpetasState: StateFlow<SubcarpetasState> = _subcarpetasState

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadSubcarpetas(unidadId: Int, token: String) {
        viewModelScope.launch {
            _subcarpetasState.value = SubcarpetasState.Loading
            try {
                val response = unidadRepository.getSubcarpetas(unidadId, token)
                if (response.isSuccessful && response.body() != null) {
                    _subcarpetasState.value = SubcarpetasState.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                    _subcarpetasState.value = SubcarpetasState.Error("Error ${response.code()}: $errorBody")
                }
            } catch (t: Throwable) {
                _subcarpetasState.value = SubcarpetasState.Error("Error de conexión: ${t.message}")
            }
        }
    }

    fun crearSubcarpeta(unidadId: Int, nombre: String, descripcion: String?, token: String) {
        viewModelScope.launch {
            val result = unidadRepository.crearSubcarpeta(unidadId, nombre, descripcion, token)
            if (result.isSuccess) {
                _successMessage.value = "Subcarpeta creada correctamente"
                loadSubcarpetas(unidadId, token)
            } else {
                _errorMessage.value = "Error creando subcarpeta: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun editarSubcarpeta(unidadId: Int, subcarpetaId: Int, nombre: String, descripcion: String?, token: String) {
        viewModelScope.launch {
            val result = unidadRepository.editarSubcarpeta(unidadId, subcarpetaId, nombre, descripcion, token)
            if (result.isSuccess) {
                _successMessage.value = "Subcarpeta actualizada correctamente"
                loadSubcarpetas(unidadId, token)
            } else {
                _errorMessage.value = "Error editando subcarpeta: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun eliminarSubcarpeta(unidadId: Int, subcarpetaId: Int, token: String) {
        viewModelScope.launch {
            val result = unidadRepository.eliminarSubcarpeta(unidadId, subcarpetaId, token)
            if (result.isSuccess) {
                _successMessage.value = "Subcarpeta eliminada correctamente"
                loadSubcarpetas(unidadId, token)
            } else {
                _errorMessage.value = "Error eliminando subcarpeta: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun toggleSubcarpeta(unidadId: Int, subcarpetaId: Int, token: String) {
        viewModelScope.launch {
            val result = unidadRepository.toggleSubcarpeta(unidadId, subcarpetaId, token)
            if (result.isSuccess) {
                _successMessage.value = "Estado de subcarpeta actualizado correctamente"
                loadSubcarpetas(unidadId, token)
            } else {
                _errorMessage.value = "Error actualizando subcarpeta: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun clearMessages() {
        _successMessage.value = null
        _errorMessage.value = null
    }
}

sealed class SubcarpetasState {
    object Loading : SubcarpetasState()
    data class Success(val subcarpetas: List<Subcarpeta>) : SubcarpetasState()
    data class Error(val message: String) : SubcarpetasState()
}
