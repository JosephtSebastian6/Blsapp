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
