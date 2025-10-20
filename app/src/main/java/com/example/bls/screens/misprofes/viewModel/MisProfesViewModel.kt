package com.example.bls.screens.misprofes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Clase
import com.example.bls.data.model.Profesor
import com.example.bls.data.model.ProfesorResumen
import com.example.bls.data.repository.ProfesorRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfeConResumen(
    val profesor: Profesor,
    val resumen: ProfesorResumen?
)

sealed class MisProfesState {
    object Loading : MisProfesState()
    data class Success(val profes: List<ProfeConResumen>) : MisProfesState()
    data class Error(val message: String) : MisProfesState()
}

sealed class GruposState {
    object Idle : GruposState()
    object Loading : GruposState()
    data class Success(val grupos: List<Clase>) : GruposState()
    data class Error(val message: String) : GruposState()
}

class MisProfesViewModel : ViewModel() {

    private val profesorRepository = ProfesorRepository()
    private val _profesState = MutableStateFlow<MisProfesState>(MisProfesState.Loading)
    val profesState: StateFlow<MisProfesState> = _profesState

    private val _gruposState = MutableStateFlow<GruposState>(GruposState.Idle)
    val gruposState: StateFlow<GruposState> = _gruposState

    fun loadProfes(token: String) {
        viewModelScope.launch {
            _profesState.value = MisProfesState.Loading
            try {
                val profesResponse = profesorRepository.getProfesores(token)
                if (profesResponse.isSuccessful && profesResponse.body() != null) {
                    val profesores = profesResponse.body()!!
                    val profesConResumen = profesores.map { profesor ->
                        async {
                            val resumenResponse = profesorRepository.getResumenAsignaciones(profesor.username, token)
                            ProfeConResumen(profesor, if (resumenResponse.isSuccessful) resumenResponse.body() else null)
                        }
                    }.awaitAll()
                    _profesState.value = MisProfesState.Success(profesConResumen)
                } else {
                    _profesState.value = MisProfesState.Error("Error al obtener la lista de profesores")
                }
            } catch (t: Throwable) {
                _profesState.value = MisProfesState.Error("Error de conexión: ${t.message}")
            }
        }
    }

    fun loadGrupos(profesorUsername: String, token: String) {
        viewModelScope.launch {
            _gruposState.value = GruposState.Loading
            try {
                val response = profesorRepository.getClasesProfesor(profesorUsername, token)
                if (response.isSuccessful && response.body() != null) {
                    val todasLasClases = response.body()!!
                    // Filtrar para obtener solo los grupos reales
                    val gruposReales = todasLasClases.filter { it.esGrupoPorUnidad }
                    _gruposState.value = GruposState.Success(gruposReales)
                } else {
                    _gruposState.value = GruposState.Error("Error al cargar los grupos del profesor.")
                }
            } catch (t: Throwable) {
                _gruposState.value = GruposState.Error("Error de conexión: ${t.message}")
            }
        }
    }
    
    fun clearGruposState() {
        _gruposState.value = GruposState.Idle
    }
}
