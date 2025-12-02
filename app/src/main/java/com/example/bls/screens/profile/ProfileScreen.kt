package com.example.bls.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
    username: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    // Si se pasa un username y el estado actual es error o no se ha cargado el usuario correcto,
    // podrías disparar la carga aquí.
    // Sin embargo, como ProfileViewModel usa SavedStateHandle, lo ideal es que el username ya esté ahí.
    // Si ProfileViewModel.init ya intentó cargar y falló (porque username era vacío en SavedStateHandle),
    // podemos intentar cargar de nuevo aquí si username != null.
    LaunchedEffect(username) {
        if (!username.isNullOrBlank()) {
            // Verificamos si es necesario recargar. 
            // Por simplicidad, llamamos a cargarPerfil si username es distinto.
            // Nota: ProfileViewModel debería exponer un método para esto o chequear si ya cargó ese usuario.
            viewModel.cargarPerfil(username)
        }
    }

    Scaffold(
        containerColor = Color.White,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(color = Color(0xFF1A237E))
                }
                is ProfileUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error al cargar el perfil", color = Color.Red)
                        Text(state.message, color = Color.Gray)
                        Button(onClick = { 
                            if (!username.isNullOrBlank()) {
                                viewModel.cargarPerfil(username) 
                            }
                        }) {
                            Text("Reintentar")
                        }
                    }
                }
                is ProfileUiState.Success -> {
                    val user = state.usuario
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Encabezado con botón atrás y título
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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
                            
                            Text(
                                text = "Información personal",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Avatar y Nombre
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = user.profileImageUrl ?: "https://placeholder.com/avatar",
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1A237E)),
                                contentScale = ContentScale.Crop
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column {
                                Text(
                                    text = "${user.nombres ?: ""} ${user.apellidos ?: ""}".trim(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A237E)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = Color(0xFFE8EAF6),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = user.tipoUsuario?.replaceFirstChar { it.uppercase() } ?: "Estudiante",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        color = Color(0xFF1A237E),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Campos del formulario
                        ProfileField(label = "Email", value = user.email ?: "")
                        ProfileField(label = "Numero de identificación", value = user.numeroIdentificacion ?: "")
                        ProfileField(label = "Ciudad", value = user.ciudad ?: "")
                        ProfileField(label = "Dirección", value = user.direccion ?: "")
                        ProfileField(label = "Año de nacimiento", value = user.anoNacimiento?.toString() ?: "")
                        ProfileField(label = "Telefono", value = user.telefono ?: "")
                        // ProfileField(label = "Url imagen de perfil", value = user.profileImageUrl ?: "")
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = { /* TODO: Guardar cambios */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFC5CAE9).copy(alpha = 0.5f)
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = Color(0xFF1A237E),
                fontSize = 14.sp
            )
        }
    }
}
