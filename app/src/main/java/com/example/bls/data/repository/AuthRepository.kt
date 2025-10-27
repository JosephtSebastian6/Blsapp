package com.example.bls.data.repository

import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ApiService
import com.example.bls.data.network.LoginRequest
import com.example.bls.data.network.LoginResponse
import retrofit2.Response

class AuthRepository {

    private val apiService: ApiService by lazy {
        ApiConfig.client { null }.create(ApiService::class.java)
    }

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }
}
