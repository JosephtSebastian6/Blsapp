package com.example.bls.screens.misprofes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bls.data.model.Clase
import com.example.bls.screens.misprofes.viewModel.GruposState
import com.example.bls.screens.misprofes.viewModel.MisProfesState
import com.example.bls.screens.misprofes.viewModel.MisProfesViewModel
import com.example.bls.screens.misprofes.viewModel.ProfeConResumen
import com.example.bls.ui.theme.BLSTheme
import androidx.compose.foundation.BorderStroke

// Sample data for the new dialog
data class StudentForManagement(val id: Int, val name: String, val username: String, val isAssigned: Boolean)

val sampleStudentsForManagement = listOf(
    StudentForManagement(1, "Sebastian gonzales", "@sebastianl", true),
    StudentForManagement(2, "Sebastian estudiante2", "@sebastian4", false)
)

@Composable
fun MisProfesScreen(viewModel: MisProfesViewModel, token: String?) {
    val profesState by viewModel.profesState.collectAsState()

    LaunchedEffect(token) {
        if (token != null) {
            viewModel.loadProfes(token)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Gestiona profesores, asigna estudiantes y crea grupos.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
        )

        Button(
            onClick = { /* Acción de crear grupo */ },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8E6C9)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF388E3C))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear grupo", color = Color(0xFF388E3C), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = profesState) {
            is MisProfesState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MisProfesState.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(state.profes) { profeConResumen ->
                        ProfeCard(profeConResumen, viewModel, token)
                    }
                }
            }
            is MisProfesState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun ProfeCard(profeConResumen: ProfeConResumen, viewModel: MisProfesViewModel, token: String?) {
    val profe = profeConResumen.profesor
    val resumen = profeConResumen.resumen
    var isExpanded by remember { mutableStateOf(false) }
    var showManageStudentsDialog by remember { mutableStateOf(false) }
    val gruposState by viewModel.gruposState.collectAsState()

    if (showManageStudentsDialog) {
        ManageStudentsDialog(profeName = profe.displayName, onDismiss = { showManageStudentsDialog = false })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFF0D47A1)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profe.displayName.first().toString(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(profe.displayName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    Text(profe.handle, fontSize = 14.sp, color = Color.Gray)
                }
                Button(onClick = { /* Ver tabla */ }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8EAF6)), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                    Text("Ver tabla", color = Color(0xFF3F51B5), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                StatItem(resumen?.gruposCreados?.toString() ?: "0", "Grupos")
                StatItem(resumen?.estudiantesAsignados?.toString() ?: "0", "Est. asignados")
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionButton(if (isExpanded) "Ocultar grupos" else "Ver grupos", true) { 
                    isExpanded = !isExpanded
                    if (isExpanded && token != null) {
                        viewModel.loadGrupos(profe.username, token)
                    } else {
                        viewModel.clearGruposState()
                    }
                }
                ActionButton("Gestionar estudiantes", true, onClick = { showManageStudentsDialog = true })
            }
            
            AnimatedVisibility(visible = isExpanded) {
                when(val state = gruposState) {
                    is GruposState.Loading -> Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                    is GruposState.Success -> {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Divider()
                            if (state.grupos.isEmpty()) {
                                Text("No hay grupos para este profesor.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                            } else {
                                state.grupos.forEach { clase ->
                                    GrupoItem(clase)
                                }
                            }
                        }
                    }
                    is GruposState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
                    is GruposState.Idle -> { /* No mostrar nada */ }
                }
            }
        }
    }
}

@Composable
fun ManageStudentsDialog(profeName: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF3F51B5))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gestionar estudiantes- $profeName",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }

                // Students List
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sampleStudentsForManagement) { student ->
                        StudentItem(student = student)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItem(student: StudentForManagement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(student.name, fontWeight = FontWeight.Bold)
                Text(student.username, fontSize = 12.sp, color = Color.Gray)
            }
            Button(
                onClick = { /* TODO: Assign/Unassign logic */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (student.isAssigned) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                )
            ) {
                Text(
                    text = if (student.isAssigned) "Desaignar" else "Asignar",
                    color = if (student.isAssigned) Color(0xFFD32F2F) else Color(0xFF388E3C),
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
fun GrupoItem(clase: Clase) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(clase.nombreGrupo, fontWeight = FontWeight.Bold)
        Text("Unidad ${clase.unidadId ?: "?"} - ${clase.estudiantes.size} estudiantes", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ActionButton(text: String, isPrimary: Boolean, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isPrimary) Color(0xFF0D47A1) else Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text, color = if (isPrimary) Color.White else Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun MisProfesScreenPreview() {
    BLSTheme {
        // MisProfesScreen() // Requires ViewModel
    }
}
