package com.example.bls.data.model

import com.squareup.moshi.Json

// --- Modelos para el Dashboard del Estudiante ---

data class DashboardEstudiante(
    val estudiante: UsuarioResponse,
    @field:Json(name = "unidades_habilitadas") val unidadesHabilitadas: List<UnidadConProgreso>,
    val estadisticas: EstadisticasProgreso
)

data class UnidadConProgreso(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    @field:Json(name = "subcarpetas_count") val subcarpetasCount: Int,
    val progreso: ProgresoUnidad? = null
)

data class ProgresoUnidad(
    @field:Json(name = "unidad_id") val unidadId: Int,
    @field:Json(name = "progreso_completado") val progresoCompletado: Float,
    @field:Json(name = "subcarpetas_completadas") val subcarpetasCompletadas: Int,
    @field:Json(name = "subcarpetas_total") val subcarpetasTotal: Int,
    @field:Json(name = "fecha_ultimo_acceso") val fechaUltimoAcceso: String?
)

data class EstadisticasProgreso(
    @field:Json(name = "total_unidades") val totalUnidades: Int,
    @field:Json(name = "unidades_completadas") val unidadesCompletadas: Int,
    @field:Json(name = "progreso_general") val progresoGeneral: Float,
    @field:Json(name = "tiempo_estudio_total") val tiempoEstudioTotal: Int, // en minutos
    @field:Json(name = "racha_actual") val rachaActual: Int // días consecutivos
)

// Modelo de respuesta de usuario, similar a otros que tenemos
data class UsuarioResponse(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "email_verified") val emailVerified: Boolean,
    @field:Json(name = "numero_identificacion") val numeroIdentificacion: String?,
    val ciudad: String?,
    val rh: String?,
    @field:Json(name = "grupo_sanguineo") val grupoSanguineo: String?,
    @field:Json(name = "ano_nacimiento") val anoNacimiento: Int?,
    val direccion: String?,
    val telefono: String?,
    @field:Json(name = "profile_image_url") val profileImageUrl: String?,
    @field:Json(name = "tipo_usuario") val tipoUsuario: String,
    @field:Json(name = "matricula_activa") val matriculaActiva: Boolean
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
}
