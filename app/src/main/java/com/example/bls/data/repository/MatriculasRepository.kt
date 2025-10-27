package com.example.bls.data.repository

import com.example.bls.data.model.Matricula
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import retrofit2.Response

class MatriculasRepository(private val token: String) {
    private val apiService: ApiService by lazy {
        ApiConfig.client { token }.create(ApiService::class.java)
    }

    suspend fun getMatriculas(): Response<List<Matricula>> {
        return apiService.getMatriculas("Bearer $token")
    }

    suspend fun toggleMatricula(username: String): Response<Unit> {
        return apiService.toggleMatricula(username, "Bearer $token")
    }
}
