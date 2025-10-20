package com.example.bls.data.repository

import com.example.bls.data.model.Clase
import com.example.bls.data.model.Profesor
import com.example.bls.data.model.ProfesorResumen
import com.example.bls.data.network.ApiService
import com.example.bls.data.network.RetrofitClient
import retrofit2.Response

class ProfesorRepository {
    private val apiService: ApiService = RetrofitClient.instance

    suspend fun getProfesores(token: String): Response<List<Profesor>> {
        return apiService.getProfesores("Bearer $token")
    }

    suspend fun getResumenAsignaciones(username: String, token: String): Response<ProfesorResumen> {
        return apiService.getResumenAsignaciones(username, "Bearer $token")
    }

    suspend fun getClasesProfesor(profesorUsername: String, token: String): Response<List<Clase>> {
        return apiService.getClasesProfesor(profesorUsername, "Bearer $token")
    }
}
