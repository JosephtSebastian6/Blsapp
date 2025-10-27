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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bls.data.model.Subcarpeta
import com.example.bls.screens.empresa.viewModel.SubcarpetasState
import com.example.bls.screens.empresa.viewModel.SubcarpetasViewModel
import com.example.bls.ui.theme.BLSTheme
import kotlinx.coroutines.launch

class UnidadDetailActivity : ComponentActivity() {
    private val subcarpetasViewModel: SubcarpetasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val unitId = intent.getIntExtra("UNIT_ID", -1)
        val unitName = intent.getStringExtra("UNIT_NAME") ?: "Unidad"
        val token = intent.getStringExtra("AUTH_TOKEN")

        if (token != null && unitId != -1) {
            subcarpetasViewModel.loadSubcarpetas(unitId, token)
        }

        setContent {
            BLSTheme {
                SubcarpetasScreen(
                    viewModel = subcarpetasViewModel, 
                    unitName = unitName, 
                    unitId = unitId, 
                    token = token,
                    onBackClicked = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcarpetasScreen(
    viewModel: SubcarpetasViewModel, 
    unitName: String, 
    unitId: Int, 
    token: String?,
    onBackClicked: () -> Unit
) {
    val subcarpetasState by viewModel.subcarpetasState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Subcarpeta?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Subcarpeta?>(null) }
    var showToggleDialog by remember { mutableStateOf<Subcarpeta?>(null) }

    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(successMessage, errorMessage) {
        successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.clearMessages()
        }
        errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.clearMessages()
        }
    }

    if (showCreateDialog) {
        CreateOrEditSubcarpetaDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { nombre, descripcion ->
                if (token != null) {
                    viewModel.crearSubcarpeta(unitId, nombre, descripcion, token)
                }
                showCreateDialog = false
            }
        )
    }

    showEditDialog?.let { subcarpeta ->
        CreateOrEditSubcarpetaDialog(
            subcarpeta = subcarpeta,
            onDismiss = { showEditDialog = null },
            onConfirm = { nombre, descripcion ->
                if (token != null) {
                    viewModel.editarSubcarpeta(unitId, subcarpeta.id, nombre, descripcion, token)
                }
                showEditDialog = null
            }
        )
    }

    showDeleteDialog?.let { subcarpeta ->
        ConfirmDialog(
            title = "Eliminar Subcarpeta",
            text = "¿Estás seguro de que quieres eliminar '${subcarpeta.nombre}'?",
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                if (token != null) {
                    viewModel.eliminarSubcarpeta(unitId, subcarpeta.id, token)
                }
                showDeleteDialog = null
            }
        )
    }

    showToggleDialog?.let { subcarpeta ->
        ConfirmDialog(
            title = if (subcarpeta.habilitada) "Ocultar Subcarpeta" else "Mostrar Subcarpeta",
            text = "¿Estás seguro de que quieres ${if (subcarpeta.habilitada) "ocultar" else "mostrar"} '${subcarpeta.nombre}'?",
            onDismiss = { showToggleDialog = null },
            onConfirm = {
                if (token != null) {
                    viewModel.toggleSubcarpeta(unitId, subcarpeta.id, token)
                }
                showToggleDialog = null
            }
        )
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(unitName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF0D47A1))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Subcarpeta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp)
        ) {
            Text("Subcarpetas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), textDecoration = TextDecoration.Underline, modifier = Modifier.padding(vertical = 16.dp))

            when (val state = subcarpetasState) {
                is SubcarpetasState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is SubcarpetasState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        items(state.subcarpetas) { subcarpeta ->
                            SubcarpetaCard(
                                subcarpeta = subcarpeta, 
                                onCardClick = {
                                    val intent = Intent(context, SubcarpetaDetailActivity::class.java).apply {
                                        putExtra("UNIT_ID", unitId)
                                        putExtra("SUBFOLDER_ID", subcarpeta.id)
                                        putExtra("AUTH_TOKEN", token)
                                    }
                                    context.startActivity(intent)
                                },
                                onEditClick = { showEditDialog = subcarpeta },
                                onDeleteClick = { showDeleteDialog = subcarpeta },
                                onToggleClick = { showToggleDialog = subcarpeta }
                            )
                        }
                    }
                }
                is SubcarpetasState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message, color = Color.Red, textAlign = TextAlign.Center) }
            }
        }
    }
}

@Composable
fun SubcarpetaCard(subcarpeta: Subcarpeta, onCardClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit, onToggleClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(20.dp)).clickable(onClick = onCardClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.Folder, contentDescription = "Folder Icon", tint = Color(0xFF5C6BC0), modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(subcarpeta.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
            Spacer(modifier = Modifier.height(16.dp))
            
            Chip(text = if (subcarpeta.habilitada) "Visible" else "Oculto", backgroundColor = if (subcarpeta.habilitada) Color(0xFFE8F5E9) else Color(0xFFF5F5F5), textColor = if (subcarpeta.habilitada) Color(0xFF388E3C) else Color.DarkGray)

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionButton("Editar", onClick = onEditClick, backgroundColor = Color(0xFFE8EAF6), textColor = Color(0xFF3F51B5))
                ActionButton("Eliminar", onClick = onDeleteClick, backgroundColor = Color(0xFFFFEBEE), textColor = Color(0xFFD32F2F))
                ActionButton(if (subcarpeta.habilitada) "Ocultar" else "Mostrar", onClick = onToggleClick, backgroundColor = Color(0xFFF5F5F5), textColor = Color.DarkGray)
            }
        }
    }
}

@Composable
fun CreateOrEditSubcarpetaDialog(subcarpeta: Subcarpeta? = null, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var nombre by remember { mutableStateOf(subcarpeta?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(subcarpeta?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (subcarpeta == null) "Crear Subcarpeta" else "Editar Subcarpeta") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(nombre, descripcion) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ConfirmDialog(title: String, text: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = { Button(onClick = onConfirm) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun Chip(text: String, backgroundColor: Color, textColor: Color) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor).padding(horizontal = 12.dp, vertical = 4.dp)) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit, backgroundColor: Color, textColor: Color) {
    Button(onClick = onClick, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = backgroundColor), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp), elevation = ButtonDefaults.buttonElevation(0.dp)) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
