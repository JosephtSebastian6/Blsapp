package com.example.bls.data.repository

import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.model.Unidad
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import retrofit2.Response

class UnidadesRepository(private val token: String) {
    private val apiService: ApiService by lazy {
        ApiConfig.client { token }.create(ApiService::class.java)
    }

    suspend fun getUnidades(): Response<List<Unidad>> {
        return apiService.getUnidades()
    }

    suspend fun getSubcarpetas(unidadId: Int): Response<List<Subcarpeta>> {
        return apiService.getSubcarpetas(unidadId)
    }
}
