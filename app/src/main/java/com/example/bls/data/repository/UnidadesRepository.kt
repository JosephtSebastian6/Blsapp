package com.example.bls.data.repository

import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.model.Unidad
import com.example.bls.data.network.ApiService
import com.example.bls.data.network.RetrofitClient
import retrofit2.Response

class UnidadesRepository {
    private val apiService: ApiService = RetrofitClient.instance

    suspend fun getUnidades(token: String): Response<List<Unidad>> {
        return apiService.getUnidades("Bearer $token")
    }

    suspend fun getSubcarpetas(unidadId: Int, token: String): Response<List<Subcarpeta>> {
        return apiService.getSubcarpetas(unidadId, "Bearer $token")
    }
}
