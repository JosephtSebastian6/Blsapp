package com.example.bls.data.network

import com.example.bls.data.model.*
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

    @GET("auth/profesores/")
    suspend fun getProfesores(@Header("Authorization") token: String): Response<List<Profesor>>

    @GET("auth/profesores/{username}/resumen-asignaciones")
    suspend fun getResumenAsignaciones(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Response<ProfesorResumen>

    @GET("auth/clases/{profesor_username}")
    suspend fun getClasesProfesor(
        @Path("profesor_username") profesorUsername: String,
        @Header("Authorization") token: String
    ): Response<List<Clase>>
}
