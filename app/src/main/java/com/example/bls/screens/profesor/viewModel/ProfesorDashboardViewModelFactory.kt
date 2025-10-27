package com.example.bls.screens.profesor.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfesorDashboardViewModelFactory(private val username: String, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfesorDashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfesorDashboardViewModel(username, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
