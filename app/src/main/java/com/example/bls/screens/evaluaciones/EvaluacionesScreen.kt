package com.example.bls.screens.evaluaciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class EvaluacionItem(
    val id: String,
    val titulo: String,
    val unidad: String,
    val descripcion: String
)

// Datos dummy para la vista
val evaluacionesDummy = listOf(
    EvaluacionItem("1", "Imagen", "Unidad 94", "aaaa"),
    EvaluacionItem("2", "Audio", "Unidad 94", "aaaa"),
    EvaluacionItem("3", "Prueba", "Unidad 94", "aaaa")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionesScreen(
    onBackClick: () -> Unit,
    onVerCalificacionesClick: () -> Unit = {},
    onVerEvaluacionClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) // Fondo oscuro
    ) {
        // Header pequeño personalizado (reemplaza TopAppBar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Evaluaciones disponibles", 
                color = Color.Gray, 
                fontSize = 14.sp
            )
        }

        // Tarjeta blanca principal que ocupa el resto de la pantalla
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Ocupa todo el espacio vertical restante
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header con botón atrás y título grande
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF5F5F5), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = "Evaluaciones",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E) // Azul oscuro
                        )
                        Text(
                            text = "disponibles",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón "Ver mis calificaciones"
                OutlinedButton(
                    onClick = onVerCalificacionesClick,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.Gray),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Ver mis calificaciones",
                        color = Color(0xFF1A237E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contenedor de la lista con borde
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent) 
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(evaluacionesDummy) { evaluacion ->
                            EvaluacionItemCard(evaluacion, onVerEvaluacionClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EvaluacionItemCard(evaluacion: EvaluacionItem, onVerClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC5CAE9)) // Color azul claro lavanda
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = evaluacion.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = evaluacion.unidad,
                    fontSize = 12.sp,
                    color = Color(0xFF1A237E).copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = evaluacion.descripcion,
                fontSize = 12.sp,
                color = Color(0xFF1A237E).copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onVerClick(evaluacion.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text("Ver", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EvaluacionesScreenPreview() {
    EvaluacionesScreen(onBackClick = {})
}
