package com.example.bls.data.model

import com.google.gson.annotations.SerializedName

data class Clase(
    val id: Int,
    val dia: String,
    val hora: String,
    val tema: String,
    @SerializedName("meet_link") val meetLink: String?,
    @SerializedName("profesor_username") val profesorUsername: String,
    @SerializedName("profesor_nombres") val profesorNombres: String?,
    @SerializedName("profesor_apellidos") val profesorApellidos: String?,
    val estudiantes: List<EstudianteEnClase>
) {
    val esGrupoPorUnidad: Boolean
        get() = tema.startsWith("Grupo Unidad ") && hora == "00:00"

    val unidadId: Int?
        get() = if (esGrupoPorUnidad) {
            tema.removePrefix("Grupo Unidad ").toIntOrNull()
        } else null

    val nombreGrupo: String
        get() = if (esGrupoPorUnidad) {
            "Grupo Unidad ${unidadId ?: "?"}"
        } else {
            tema
        }
}

data class EstudianteEnClase(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
}
