package com.example.bls.data.repository

import com.example.bls.data.model.Subcarpeta
import com.example.bls.data.model.Unidad
import com.example.bls.data.network.*
import retrofit2.Response

class UnidadRepository(private val token: String) {
    private val apiService: ApiService by lazy {
        ApiConfig.client { token }.create(ApiService::class.java)
    }

    suspend fun getUnidades(): Response<List<Unidad>> {
        return apiService.getUnidades("Bearer $token")
    }

    suspend fun crearUnidad(nombre: String, descripcion: String?): Result<UnidadResponse> {
        return try {
            val response = apiService.crearUnidad("Bearer $token", UnidadCreate(nombre, descripcion))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSubcarpetas(unidadId: Int): Response<List<Subcarpeta>> {
        return apiService.getSubcarpetas(unidadId, "Bearer $token")
    }

    suspend fun crearSubcarpeta(unidadId: Int, nombre: String, descripcion: String?): Result<Subcarpeta> {
        return try {
            val response = apiService.crearSubcarpeta(unidadId, "Bearer $token", SubcarpetaCreate(nombre, descripcion))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editarSubcarpeta(unidadId: Int, subcarpetaId: Int, nombre: String, descripcion: String?): Result<Subcarpeta> {
        return try {
            val response = apiService.editarSubcarpeta(unidadId, subcarpetaId, "Bearer $token", SubcarpetaUpdate(nombre, descripcion))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarSubcarpeta(unidadId: Int, subcarpetaId: Int): Result<Unit> {
        return try {
            val response = apiService.eliminarSubcarpeta(unidadId, subcarpetaId, "Bearer $token")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleSubcarpeta(unidadId: Int, subcarpetaId: Int): Result<ToggleResponse> {
        return try {
            val response = apiService.toggleSubcarpeta(unidadId, subcarpetaId, "Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleUnidad(unidadId: Int): Response<Unidad> {
        return apiService.toggleUnidad(unidadId, "Bearer $token")
    }

    suspend fun toggleUnidadEstudiante(username: String, unidadId: Int): Result<Boolean> {
        return try {
            val response = apiService.toggleUnidadEstudiante(username, unidadId, "Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.habilitada)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
