package com.example.bls.data.repository

import com.example.bls.data.model.LoginRequest
import com.example.bls.data.model.LoginResponse
import com.example.bls.data.network.ApiService
import com.example.bls.data.network.RetrofitClient
import retrofit2.Response

class AuthRepository {
    private val apiService: ApiService = RetrofitClient.instance

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }
}
