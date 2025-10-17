package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.repository.UnidadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubcarpetasViewModel : ViewModel() {

    private val unidadesRepository = UnidadesRepository()
    private val _subcarpetasState = MutableStateFlow<SubcarpetasState>(SubcarpetasState.Loading)
    val subcarpetasState: StateFlow<SubcarpetasState> = _subcarpetasState

    fun loadSubcarpetas(unidadId: Int, token: String) {
        viewModelScope.launch {
            _subcarpetasState.value = SubcarpetasState.Loading
            try {
                val response = unidadesRepository.getSubcarpetas(unidadId, token)
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
}

sealed class SubcarpetasState {
    object Loading : SubcarpetasState()
    data class Success(val subcarpetas: List<Subcarpeta>) : SubcarpetasState()
    data class Error(val message: String) : SubcarpetasState()
}
