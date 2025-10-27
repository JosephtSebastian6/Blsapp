package com.example.bls.screens.empresa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bls.data.repository.ArchivosSubcarpetaRepository

class ArchivosSubcarpetaViewModelFactory(
    private val repository: ArchivosSubcarpetaRepository,
    private val unidadId: Int,
    private val subcarpetaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivosSubcarpetaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArchivosSubcarpetaViewModel(repository, unidadId, subcarpetaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
