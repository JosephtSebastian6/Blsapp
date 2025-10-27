package com.example.bls.screens.matriculas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bls.data.model.Matricula
import com.example.bls.screens.matriculas.viewModel.GestionMatriculasViewModel
import com.example.bls.screens.matriculas.viewModel.MatriculasState
import com.example.bls.ui.theme.BLSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionMatriculasScreen(viewModel: GestionMatriculasViewModel) {
    var showDetailedView by remember { mutableStateOf(false) }
    val matriculasState by viewModel.matriculasState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMatriculas()
    }

    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        topBar = {
            TopAppBar(
                title = { Text("Gestión de matrículas", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (showDetailedView) {
                            showDetailedView = false
                        } else {
                            // TODO: Lógica para cerrar la actividad (si es necesario)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        when(val state = matriculasState) {
            is MatriculasState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is MatriculasState.Success -> {
                if (showDetailedView) {
                    DetailedMatriculasView(modifier = Modifier.padding(paddingValues), matriculas = state.matriculas, viewModel = viewModel)
                } else {
                    SimpleMatriculasView(modifier = Modifier.padding(paddingValues), matriculas = state.matriculas, onShowDetails = { showDetailedView = true })
                }
            }
            is MatriculasState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message, color = Color.Red) }
        }
    }
}

@Composable
fun SimpleMatriculasView(modifier: Modifier = Modifier, matriculas: List<Matricula>, onShowDetails: () -> Unit) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Administra y supervisa las matrículas de estudiantes en los diferentes cursos de inglés.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF3F51B5)).padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lista de matrículas", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Chip(text = "${matriculas.size} matriculas registradas", backgroundColor = Color(0x33FFFFFF), textColor = Color.White, fontSize = 10.sp)
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    TableHeader(isDetailed = false)
                    Divider(color = Color(0x333F51B5))
                    matriculas.forEach { matricula ->
                        TableRow(matricula, onMoreInfoClick = onShowDetails)
                        Divider(color = Color(0x333F51B5))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(value = matriculas.count { it.matriculaActiva }.toString(), label = "Activas")
                    StatItem(value = "0", label = "Pendientes") // TODO: Add logic for this
                    StatItem(value = matriculas.count { !it.matriculaActiva }.toString(), label = "Inactivas")
                }
            }
        }
    }
}

@Composable
fun DetailedMatriculasView(modifier: Modifier = Modifier, matriculas: List<Matricula>, viewModel: GestionMatriculasViewModel) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF3F51B5)).padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lista de matrículas", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    TableHeader(isDetailed = true)
                    Divider(color = Color(0x333F51B5))
                    matriculas.forEach { matricula ->
                        DetailedTableRow(matricula, viewModel)
                        Divider(color = Color(0x333F51B5))
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeader(isDetailed: Boolean) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("ID", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text("Estudiante", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        if (isDetailed) {
            Text("Profesor", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Fecha matrícula", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Acciones", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        } else {
            Text("Email", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Curso", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Estado", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Mas inf.", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TableRow(matricula: Matricula, onMoreInfoClick: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(matricula.identificador.toString(), modifier = Modifier.weight(0.5f), fontSize = 12.sp)
        Text(matricula.displayName, modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(matricula.email, modifier = Modifier.weight(1.5f), fontSize = 12.sp, color = Color.Gray)
        Chip(text = matricula.tipoUsuario, backgroundColor = Color(0xFFE0E0E0), textColor = Color.DarkGray, modifier = Modifier.weight(1f))
        Chip(text = matricula.estadoMatricula, backgroundColor = if(matricula.matriculaActiva) Color(0xFFC8E6C9) else Color(0xFFFFEBEE), textColor = if(matricula.matriculaActiva) Color(0xFF388E3C) else Color(0xFFD32F2F), modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(0.7f), contentAlignment = Alignment.Center) {
             IconButton(onClick = onMoreInfoClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Más info", tint = Color.White, modifier = Modifier.background(Color(0xFF3F51B5), CircleShape))
            }
        }
    }
}

@Composable
fun DetailedTableRow(matricula: Matricula, viewModel: GestionMatriculasViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Acción") },
            text = { Text("¿Estás seguro de que quieres ${if (matricula.matriculaActiva) "desactivar" else "activar"} la matrícula de ${matricula.displayName}?") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        viewModel.toggleMatricula(matricula.username)
                        showDialog = false 
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
        )
    }

    Row(modifier = Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(matricula.identificador.toString(), modifier = Modifier.weight(0.5f), fontSize = 12.sp)
        Text(matricula.displayName, modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text("Sistema", modifier = Modifier.weight(1.5f), fontSize = 12.sp, color = Color.Gray) // TODO: Get professor name
        Text("01/02/2025", modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color.Gray) // TODO: Get matricula date
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Lock, contentDescription = "Toggle Lock", tint = if (matricula.matriculaActiva) Color.Gray else Color(0xFF3F51B5), modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* TODO: Visibility action */ }) {
                Icon(Icons.Default.VisibilityOff, contentDescription = "VisibilityOff", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun Chip(text: String, backgroundColor: Color, textColor: Color, modifier: Modifier = Modifier, fontSize: TextUnit = 10.sp) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor).padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = fontSize, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun GestionMatriculasScreenPreview() {
    BLSTheme {
        GestionMatriculasScreen(viewModel = viewModel())
    }
}
