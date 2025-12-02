package com.example.bls.screens.profesor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bls.R
import com.example.bls.data.model.UsuarioResponse
import com.example.bls.screens.profesor.viewModel.ProfesorDashboardViewModel

@Composable
fun ProfesorDashboardScreen() {
    val viewModel: ProfesorDashboardViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Row(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Barra lateral azul
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(64.dp)
                .background(Color(0xFF0D47A1)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            IconButton(onClick = { /* TODO: Open drawer */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Información personal", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFBDBDBD))

            uiState?.let { user ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Reemplazar con el avatar del profesor
                        contentDescription = "Avatar",
                        modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(user.nombres ?: "", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                        Chip(text = user.tipoUsuario ?: "")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                InfoGrid(user)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { 
                        val updatedProfile = mutableMapOf<String, Any?>()
                        // TODO: Recoger los datos de los campos de texto
                        viewModel.updateProfile(updatedProfile)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoGrid(user: UsuarioResponse) {
    var email by remember { mutableStateOf(user.email ?: "") }
    var numeroIdentificacion by remember { mutableStateOf(user.numeroIdentificacion ?: "") }
    var nombres by remember { mutableStateOf(user.nombres ?: "") }
    var apellidos by remember { mutableStateOf(user.apellidos ?: "") }
    var ciudad by remember { mutableStateOf(user.ciudad ?: "") }
    var anoNacimiento by remember { mutableStateOf(user.anoNacimiento ?: "") }
    var direccion by remember { mutableStateOf(user.direccion ?: "") }
    var telefono by remember { mutableStateOf(user.telefono ?: "") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoField("Email", email, { email = it }, Modifier.weight(1f))
            InfoField("Número de identificación", numeroIdentificacion, { numeroIdentificacion = it }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoField("Nombres", nombres, { nombres = it }, Modifier.weight(1f))
            InfoField("Apellidos", apellidos, { apellidos = it }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoField("Ciudad", ciudad, { ciudad = it }, Modifier.weight(1f))
            InfoField("Año de nacimiento", anoNacimiento, { anoNacimiento = it }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoField("Dirección", direccion, { direccion = it }, Modifier.weight(1f))
            InfoField("Teléfono", telefono, { telefono = it }, Modifier.weight(1f))
        }
    }
}

@Composable
fun InfoField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0D47A1),
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color(0xFFF0F2F5),
                unfocusedContainerColor = Color(0xFFF0F2F5)
            )
        )
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
