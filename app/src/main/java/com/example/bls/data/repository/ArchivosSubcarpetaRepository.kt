package com.example.bls.data.repository

import com.example.bls.data.network.ApiService
import com.example.bls.data.network.LinkCreate
import com.example.bls.data.network.LinkResponse
import com.example.bls.data.network.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File

class ArchivosSubcarpetaRepository(private val apiService: ApiService) {

    suspend fun uploadFiles(
        unidadId: Int, 
        subcarpetaId: Int, 
        files: List<File>,
        token: String
    ): Result<UploadResponse> {
        return try {
            val parts = files.map { file ->
                // Usamos la función de extensión asRequestBody para OkHttp 4.x
                val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("files", file.name, requestBody)
            }
            
            val response = apiService.uploadEmpresaFile(unidadId, subcarpetaId, parts)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArchivos(unidadId: Int, subcarpetaId: Int, token: String): Result<List<com.example.bls.data.network.ArchivoEmpresa>> {
        return try {
            val response = apiService.getEmpresaFiles(unidadId, subcarpetaId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarArchivo(unidadId: Int, subcarpetaId: Int, archivoId: String, token: String): Result<Unit> {
        return try {
            val response = apiService.deleteEmpresaFile(unidadId, subcarpetaId, archivoId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearLink(unidadId: Int, subcarpetaId: Int, nombre: String, url: String, token: String): Result<LinkResponse> {
        return try {
            val linkCreate = LinkCreate(nombre, url)
            val response = apiService.crearLinkEmpresa(unidadId, subcarpetaId, linkCreate)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadArchivo(unidadId: Int, subcarpetaId: Int, archivoId: String, token: String): Result<ResponseBody> {
        return try {
            val response = apiService.downloadEmpresaFile(unidadId, subcarpetaId, archivoId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
