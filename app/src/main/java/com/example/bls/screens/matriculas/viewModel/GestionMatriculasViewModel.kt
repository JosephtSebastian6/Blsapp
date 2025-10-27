package com.example.bls.screens.matriculas.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Matricula
import com.example.bls.data.repository.MatriculasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MatriculasState {
    object Loading : MatriculasState()
    data class Success(val matriculas: List<Matricula>) : MatriculasState()
    data class Error(val message: String) : MatriculasState()
}

class GestionMatriculasViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val token: String = savedStateHandle.get<String>("AUTH_TOKEN") ?: ""
    private val repository = MatriculasRepository(token)
    private val _matriculasState = MutableStateFlow<MatriculasState>(MatriculasState.Loading)
    val matriculasState: StateFlow<MatriculasState> = _matriculasState

    fun loadMatriculas() {
        viewModelScope.launch {
            _matriculasState.value = MatriculasState.Loading
            try {
                val response = repository.getMatriculas()
                if (response.isSuccessful && response.body() != null) {
                    _matriculasState.value = MatriculasState.Success(response.body()!!)
                } else {
                    _matriculasState.value = MatriculasState.Error("Error al cargar las matrículas")
                }
            } catch (e: Exception) {
                _matriculasState.value = MatriculasState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun toggleMatricula(username: String) {
        viewModelScope.launch {
            try {
                val response = repository.toggleMatricula(username)
                if (response.isSuccessful) {
                    // Recargar la lista para reflejar el cambio
                    loadMatriculas()
                } else {
                    // Opcional: podrías querer mostrar un error más específico aquí
                    _matriculasState.value = MatriculasState.Error("Error al actualizar la matrícula")
                }
            } catch (e: Exception) {
                 _matriculasState.value = MatriculasState.Error("Error de conexión al actualizar: ${e.message}")
            }
        }
    }
}
