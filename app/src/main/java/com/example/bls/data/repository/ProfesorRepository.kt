package com.example.bls.data.repository

import com.example.bls.data.model.Clase
import com.example.bls.data.model.Profesor
import com.example.bls.data.model.ProfesorResumen
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import retrofit2.Response

class ProfesorRepository(private val token: String) {
    private val apiService: ApiService by lazy {
        ApiConfig.client { token }.create(ApiService::class.java)
    }

    suspend fun getProfesores(): Response<List<Profesor>> {
        return apiService.getProfesores()
    }

    suspend fun getResumenAsignaciones(username: String): Response<ProfesorResumen> {
        return apiService.getResumenAsignaciones(username)
    }

    suspend fun getClasesProfesor(profesorUsername: String): Response<List<Clase>> {
        return apiService.getClasesProfesor(profesorUsername)
    }
}
