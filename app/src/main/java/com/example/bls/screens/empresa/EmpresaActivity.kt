package com.example.bls.screens.empresa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bls.data.model.Unidad
import com.example.bls.screens.auth.LoginActivity
import com.example.bls.screens.empresa.viewModel.UnidadesState
import com.example.bls.screens.empresa.viewModel.UnidadesViewModel
import com.example.bls.ui.theme.BLSTheme

class EmpresaActivity : ComponentActivity() {
    private val unidadesViewModel: UnidadesViewModel by viewModels()
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authToken = intent.getStringExtra("AUTH_TOKEN")

        if (authToken != null) {
            unidadesViewModel.loadUnidades(authToken!!)
        }

        setContent {
            BLSTheme {
                UnidadesScreen(viewModel = unidadesViewModel, token = authToken)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnidadesScreen(viewModel: UnidadesViewModel, token: String?) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val unidadesState by viewModel.unidadesState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Unidades",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1) // Azul oscuro
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0D47A1)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFF0D47A1)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Create Unit Button
            Button(
                onClick = { /* Acción de crear unidad */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)), // Verde muy claro
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF388E3C)) // Verde oscuro
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear unidad", color = Color(0xFF388E3C), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Content based on state
            when (val state = unidadesState) {
                is UnidadesState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UnidadesState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.unidades) { unidad ->
                            UnitCard(unidad, token)
                        }
                    }
                }
                is UnidadesState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color.Red, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirmar Cierre de Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun UnitCard(unit: Unidad, token: String?) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
            .clickable { 
                val intent = Intent(context, UnidadDetailActivity::class.java).apply {
                    putExtra("UNIT_ID", unit.id)
                    putExtra("UNIT_NAME", unit.nombre)
                    putExtra("AUTH_TOKEN", token)
                }
                context.startActivity(intent)
             },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Folder,
                contentDescription = "Unit Icon",
                tint = Color(0xFF0D47A1), // Azul oscuro
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = unit.nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1) // Azul oscuro
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = unit.descripcion,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Chip de Subcarpetas
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF5F5F5)) // Gris muy claro
            ) {
                Text(
                    text = "${unit.subcarpetasCount} Subcarpetas",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun UnidadesScreenPreview() {
    BLSTheme {
        // UnidadesScreen(viewModel = UnidadesViewModel(), token = "fake_token") // This would crash the preview
    }
}
