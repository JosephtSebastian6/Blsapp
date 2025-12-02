package com.example.bls.data.repository

import com.example.bls.data.model.UsuarioResponse
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import retrofit2.Response

class ProfileRepository(private val token: String) {
    private val apiService: ApiService by lazy {
        ApiConfig.client { token }.create(ApiService::class.java)
    }

    suspend fun getUsuario(username: String): Result<UsuarioResponse> {
        return try {
            val response = apiService.getUsuario(username)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
