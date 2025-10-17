package com.example.bls.data.network

import com.example.bls.data.model.LoginRequest
import com.example.bls.data.model.LoginResponse
import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.model.Unidad
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("auth/unidades/")
    suspend fun getUnidades(@Header("Authorization") token: String): Response<List<Unidad>>

    @GET("auth/unidades/{unidad_id}/subcarpetas")
    suspend fun getSubcarpetas(
        @Path("unidad_id") unidadId: Int,
        @Header("Authorization") token: String
    ): Response<List<Subcarpeta>>
}
