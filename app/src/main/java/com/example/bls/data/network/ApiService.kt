package com.example.bls.data.network

import com.example.bls.data.model.*
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("auth/usuario/{username}")
    suspend fun getUsuario(@Path("username") username: String): Response<UsuarioResponse>

    @PUT("auth/update-perfil")
    suspend fun updatePerfil(@Body perfil: Map<String, @JvmSuppressWildcards Any?>): Response<UsuarioResponse>

    @POST("auth/unidades/")
    suspend fun crearUnidad(
        @Body unidad: UnidadCreate
    ): Response<UnidadResponse>

    @GET("auth/unidades/")
    suspend fun getUnidades(): Response<List<Unidad>>

    @GET("auth/estudiantes/me/unidades-habilitadas")
    suspend fun getUnidadesEstudiante(): Response<List<Unidad>>

    @PUT("auth/unidades/{unidad_id}/toggle")
    suspend fun toggleUnidad(
        @Path("unidad_id") unidadId: Int
    ): Response<Unidad>

    @PUT("auth/estudiantes/{username}/unidades/{unidad_id}/toggle")
    suspend fun toggleUnidadEstudiante(
        @Path("username") username: String,
        @Path("unidad_id") unidadId: Int
    ): Response<ToggleResponse>

    @GET("auth/unidades/{unidad_id}/subcarpetas")
    suspend fun getSubcarpetas(
        @Path("unidad_id") unidadId: Int
    ): Response<List<Subcarpeta>>

    @POST("auth/unidades/{unidad_id}/subcarpetas")
    suspend fun crearSubcarpeta(
        @Path("unidad_id") unidadId: Int,
        @Body subcarpeta: SubcarpetaCreate
    ): Response<Subcarpeta>

    @PUT("auth/unidades/{unidad_id}/subcarpetas/{subcarpeta_id}")
    suspend fun editarSubcarpeta(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int,
        @Body subcarpeta: SubcarpetaUpdate
    ): Response<Subcarpeta>

    @DELETE("auth/unidades/{unidad_id}/subcarpetas/{subcarpeta_id}")
    suspend fun eliminarSubcarpeta(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int
    ): Response<Unit>

    @PUT("auth/unidades/{unidad_id}/subcarpetas/{subcarpeta_id}/toggle")
    suspend fun toggleSubcarpeta(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int
    ): Response<ToggleResponse>

    @Multipart
    @POST("auth/empresa/subcarpetas/{unidad_id}/{subcarpeta_id}/upload")
    suspend fun uploadEmpresaFile(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int,
        @Part files: List<MultipartBody.Part>
    ): Response<UploadResponse>

    @GET("auth/empresa/subcarpetas/{unidad_id}/{subcarpeta_id}/files")
    suspend fun getEmpresaFiles(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int
    ): Response<List<ArchivoEmpresa>>

    @DELETE("auth/empresa/subcarpetas/{unidad_id}/{subcarpeta_id}/files/{archivo_id}")
    suspend fun deleteEmpresaFile(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int,
        @Path("archivo_id") archivoId: String
    ): Response<Unit>

    @POST("auth/empresa/subcarpetas/{unidad_id}/{subcarpeta_id}/links")
    suspend fun crearLinkEmpresa(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int,
        @Body link: LinkCreate
    ): Response<LinkResponse>

    @GET("auth/empresa/subcarpetas/{unidad_id}/{subcarpeta_id}/files/{archivo_id}/download")
    suspend fun downloadEmpresaFile(
        @Path("unidad_id") unidadId: Int,
        @Path("subcarpeta_id") subcarpetaId: Int,
        @Path("archivo_id") archivoId: String
    ): Response<ResponseBody>

    @GET("auth/profesores/")
    suspend fun getProfesores(): Response<List<Profesor>>

    @GET("auth/profesores/{username}/resumen-asignaciones")
    suspend fun getResumenAsignaciones(
        @Path("username") username: String
    ): Response<ProfesorResumen>

    @GET("auth/clases/{profesor_username}")
    suspend fun getClasesProfesor(
        @Path("profesor_username") profesorUsername: String
    ): Response<List<Clase>>

    @GET("auth/matriculas/")
    suspend fun getMatriculas(): Response<List<Matricula>>

    @PUT("auth/matriculas/{username}/toggle")
    suspend fun toggleMatricula(
        @Path("username") username: String
    ): Response<Unit>

    @GET("estudiantes/me/dashboard")
    suspend fun getDashboardEstudiante(
    ): Response<DashboardEstudiante>

    @GET("estudiantes/me/unidades-habilitadas")
    suspend fun getUnidadesHabilitadas(
    ): Response<List<Unidad>>
}

@JsonClass(generateAdapter = true)
data class LoginRequest(val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class LoginResponse(
  val access_token: String,
  val token_type: String,
  val tipo_usuario: String?,
  val usuario: UsuarioResponse
)

data class ToggleResponse(val habilitada: Boolean)

data class UnidadCreate(
    val nombre: String,
    val descripcion: String? = null
)

data class UnidadResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String?
)

data class SubcarpetaCreate(
    val nombre: String,
    val descripcion: String? = null,
    val orden: Int = 0
)

data class SubcarpetaUpdate(
    val nombre: String? = null,
    val descripcion: String? = null,
    val orden: Int? = null
)

data class ArchivoEmpresa(
    val id: String,
    val nombre_original: String,
    val tipo: String,
    val tamano: Long,
    val fecha_subida: String,
    val es_link: Boolean,
    val url: String
)

data class LinkCreate(
    val nombre: String,
    val url: String
)

data class LinkResponse(
    val message: String,
    val link: ArchivoEmpresa,
    val unidad_id: Int,
    val subcarpeta_id: Int
)

data class UploadResponse(
    val message: String,
    val uploaded_files: List<UploadedFile>,
    val unidad_id: Int,
    val subcarpeta_id: Int,
    val uploaded_by: String,
    val upload_date: String
)

data class UploadedFile(
    val filename: String,
    val original_filename: String,
    val file_size: Long
)
