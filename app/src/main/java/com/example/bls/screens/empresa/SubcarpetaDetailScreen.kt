package com.example.bls.screens.empresa

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bls.data.network.ApiConfig
import com.example.bls.data.network.ArchivoEmpresa
import com.example.bls.data.network.ApiService
import com.example.bls.data.repository.ArchivosSubcarpetaRepository
import com.example.bls.screens.empresa.viewModel.ArchivosSubcarpetaViewModel
import com.example.bls.screens.empresa.viewModel.ArchivosSubcarpetaViewModelFactory
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcarpetaDetailScreen(
    unidadId: Int, 
    subcarpetaId: Int, 
    token: String?,
    onBackClicked: () -> Unit
) {
    val apiService = ApiConfig.client { token }.create(ApiService::class.java)
    val viewModel: ArchivosSubcarpetaViewModel = viewModel(
        factory = ArchivosSubcarpetaViewModelFactory(
            ArchivosSubcarpetaRepository(apiService), 
            unidadId, 
            subcarpetaId
        )
    )

    val context = LocalContext.current
    val archivos by viewModel.archivos.observeAsState(emptyList())
    val isLoading by viewModel.loading.observeAsState(false)

    var showLinkDialog by remember { mutableStateOf(false) }
    var showCleanDialog by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val files = uris.mapNotNull { uri ->
                context.contentResolver.openInputStream(uri)?.readBytes()?.let {
                    val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
                    file.writeBytes(it)
                    file
                }
            }
            if (token != null) {
                viewModel.subirArchivos(files, token)
            }
        }
    )

    LaunchedEffect(token) {
        if (token != null) {
            viewModel.cargarArchivos(token)
        }
    }

    if (showLinkDialog) {
        AdjuntarLinkDialog(
            onDismiss = { showLinkDialog = false },
            onConfirm = { nombre, url ->
                if (token != null) {
                    viewModel.adjuntarLink(nombre, url, token)
                }
                showLinkDialog = false
            }
        )
    }

    if (showCleanDialog) {
        ConfirmDialog(
            title = "Limpiar Archivos",
            text = "¿Estás seguro de que quieres eliminar todos los archivos de esta subcarpeta?",
            onDismiss = { showCleanDialog = false },
            onConfirm = {
                if (token != null) {
                    viewModel.limpiarArchivos(token)
                }
                showCleanDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contenido de la Subcarpeta") },
                navigationIcon = { IconButton(onClick = onBackClicked) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás") } }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { filePickerLauncher.launch("*/*") }) {
                    Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Elegir archivos")
                }
                Button(onClick = { showCleanDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Limpiar archivos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showLinkDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.AddLink, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Adjuntar link")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(archivos) { archivo ->
                    ArchivoItem(archivo = archivo, onDelete = {
                        if (token != null) {
                            viewModel.eliminarArchivo(archivo.id, token)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun ArchivoItem(archivo: ArchivoEmpresa, onDelete: () -> Unit) {
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth().clickable {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(archivo.url))
        context.startActivity(intent)
    }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(archivo.nombre_original, fontWeight = FontWeight.Bold)
                Text("${archivo.tamano / 1024} KB", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun AdjuntarLinkDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adjuntar Link") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del link") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(nombre, url) }) { Text("Adjuntar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

