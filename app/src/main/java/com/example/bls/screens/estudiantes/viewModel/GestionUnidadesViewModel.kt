package com.example.bls.screens.estudiantes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Unidad
import com.example.bls.data.repository.UnidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GestionUnidadesState {
    object Loading : GestionUnidadesState()
    data class Success(val unidades: List<Unidad>) : GestionUnidadesState()
    data class Error(val message: String) : GestionUnidadesState()
}

class GestionUnidadesViewModel : ViewModel() {

    private val repository = UnidadRepository()

    private val _unidadesState = MutableStateFlow<GestionUnidadesState>(GestionUnidadesState.Loading)
    val unidadesState: StateFlow<GestionUnidadesState> = _unidadesState

    fun loadUnidades(token: String) {
        viewModelScope.launch {
            _unidadesState.value = GestionUnidadesState.Loading
            try {
                val response = repository.getUnidades(token)
                if (response.isSuccessful && response.body() != null) {
                    _unidadesState.value = GestionUnidadesState.Success(response.body()!!)
                } else {
                    _unidadesState.value = GestionUnidadesState.Error("Error al cargar las unidades.")
                }
            } catch (e: Exception) {
                _unidadesState.value = GestionUnidadesState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun toggleUnitForStudent(unitId: Int, studentName: String, token: String) {
        viewModelScope.launch {
            val result = repository.toggleUnidadEstudiante(studentName, unitId, token)

            if (result.isSuccess) {
                val newState = result.getOrThrow()
                val currentState = _unidadesState.value
                if (currentState is GestionUnidadesState.Success) {
                    val updatedList = currentState.unidades.map {
                        if (it.id == unitId) {
                            it.copy(habilitada = newState)
                        } else {
                            it
                        }
                    }
                    _unidadesState.value = GestionUnidadesState.Success(updatedList)
                }
            } else {
                _unidadesState.value = GestionUnidadesState.Error("Error al actualizar la unidad: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun toggleUnidad(unidadId: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.toggleUnidad(unidadId, token)
                if (response.isSuccessful) {
                    // Recargar la lista para reflejar el cambio
                    loadUnidades(token)
                } else {
                    // Opcional: Manejar error específico
                }
            } catch (e: Exception) {
                // Opcional: Manejar error de conexión
            }
        }
    }
}
