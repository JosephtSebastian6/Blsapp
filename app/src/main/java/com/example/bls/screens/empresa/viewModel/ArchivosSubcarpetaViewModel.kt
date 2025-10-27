package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bls.data.network.ArchivoEmpresa
import com.example.bls.data.repository.ArchivosSubcarpetaRepository
import kotlinx.coroutines.launch
import java.io.File

class ArchivosSubcarpetaViewModel(
    private val repository: ArchivosSubcarpetaRepository,
    private val unidadId: Int,
    private val subcarpetaId: Int
) : ViewModel() {

    private val _archivos = MutableLiveData<List<ArchivoEmpresa>>()
    val archivos: LiveData<List<ArchivoEmpresa>> = _archivos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    fun cargarArchivos(token: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.getArchivos(unidadId, subcarpetaId, token)
            _loading.value = false
            if (result.isSuccess) {
                _archivos.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = "Error cargando archivos: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun subirArchivos(files: List<File>, token: String) {
        if (files.isEmpty()) {
            _errorMessage.value = "Selecciona al menos un archivo"
            return
        }

        _loading.value = true
        viewModelScope.launch {
            val result = repository.uploadFiles(unidadId, subcarpetaId, files, token)
            _loading.value = false
            if (result.isSuccess) {
                _successMessage.value = "Archivos subidos correctamente"
                cargarArchivos(token)
            } else {
                _errorMessage.value = "Error subiendo archivos: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun adjuntarLink(nombre: String, url: String, token: String) {
        if (nombre.trim().isEmpty() || url.trim().isEmpty()) {
            _errorMessage.value = "Nombre y URL son requeridos"
            return
        }

        _loading.value = true
        viewModelScope.launch {
            val result = repository.crearLink(unidadId, subcarpetaId, nombre, url, token)
            _loading.value = false
            if (result.isSuccess) {
                _successMessage.value = "Link adjuntado correctamente"
                cargarArchivos(token)
            } else {
                _errorMessage.value = "Error adjuntando link: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun eliminarArchivo(archivoId: String, token: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.eliminarArchivo(unidadId, subcarpetaId, archivoId, token)
            _loading.value = false
            if (result.isSuccess) {
                _successMessage.value = "Archivo eliminado correctamente"
                cargarArchivos(token)
            } else {
                _errorMessage.value = "Error eliminando archivo: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun limpiarArchivos(token: String) {
        _archivos.value?.forEach { archivo ->
            if (!archivo.es_link) {
                eliminarArchivo(archivo.id, token)
            }
        }
    }
}
