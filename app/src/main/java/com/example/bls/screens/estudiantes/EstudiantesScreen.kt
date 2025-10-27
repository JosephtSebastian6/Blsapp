package com.example.bls.screens.estudiantes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.bls.data.model.Unidad
import com.example.bls.screens.estudiantes.viewModel.GestionUnidadesState
import com.example.bls.screens.estudiantes.viewModel.GestionUnidadesViewModel

data class EstudianteDetailRowData(val ciudad: String, val telefono: String, val studentName: String, val username: String)

val sampleEstudiantesDetail = listOf(
    EstudianteDetailRowData("Bogota", "3115691632", "Sebastian Gonzalez", "sebastian"),
    EstudianteDetailRowData("Bogota", "3115691633", "Sebastian Estudiante2", "sebastian4")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudiantesScreen(navController: NavController, gestionUnidadesViewModel: GestionUnidadesViewModel) {
    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Bienvenido al dashboard de estudiantes. Aquí podrás gestionar y visualizar información de los estudiantes.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3F51B5))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Lista de matrículas", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        EstudiantesDetailedHeader()
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        sampleEstudiantesDetail.forEach { estudiante ->
                            EstudianteDetailedTableRow(estudiante, gestionUnidadesViewModel)
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstudiantesDetailedHeader() {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Ciudad", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
        Text("Telefono", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
        Text("Unidades", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
        Text("Acciones", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color.Gray)
    }
}

@Composable
fun EstudianteDetailedTableRow(estudiante: EstudianteDetailRowData, viewModel: GestionUnidadesViewModel) {
    var showManageUnitsDialog by remember { mutableStateOf(false) }

    if (showManageUnitsDialog) {
        ManageUnitsDialog(
            studentName = estudiante.studentName, 
            username = estudiante.username,
            viewModel = viewModel,
            onDismiss = { showManageUnitsDialog = false }
        )
    }

    Row(modifier = Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(estudiante.ciudad, modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(estudiante.telefono, modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        
        Box(modifier = Modifier.weight(1.5f)) {
            Button(
                onClick = { showManageUnitsDialog = true }, 
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Unidades", fontSize = 10.sp, color = Color.White)
            }
        }

        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Default.VisibilityOff, contentDescription = "Visibility", tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(18.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ManageUnitsDialog(studentName: String, username: String, viewModel: GestionUnidadesViewModel, onDismiss: () -> Unit) {
    val unidadesState by viewModel.unidadesState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadUnidades()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF3F51B5)).padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gestionar unidades - $studentName", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }

                when(val state = unidadesState) {
                    is GestionUnidadesState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is GestionUnidadesState.Success -> {
                        LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.unidades) { unit ->
                                UnitItem(unit = unit, onToggle = {
                                    viewModel.toggleUnitForStudent(unit.id, username)
                                })
                            }
                        }
                    }
                    is GestionUnidadesState.Error -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(state.message, color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnitItem(unit: Unidad, onToggle: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Cambio") },
            text = { Text("¿Estás seguro de que quieres ${if (unit.habilitada) "deshabilitar" else "habilitar"} esta unidad?") },
            confirmButton = {
                TextButton(onClick = {
                    onToggle()
                    showDialog = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(unit.nombre, fontWeight = FontWeight.Bold)
                Text(unit.descripcion, fontSize = 12.sp, color = Color.Gray)
            }
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (unit.habilitada) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
            ) {
                Text(
                    text = if (unit.habilitada) "Habilitado" else "Deshabilitado",
                    color = if (unit.habilitada) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    fontSize = 12.sp
                )
            }
        }
    }
}
