package com.example.bls.screens.empresa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcarpetasScreen(navController: NavController, unidadId: Int, subcarpetaId: Int) {
    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        topBar = {
            TopAppBar(
                title = { Text("Subcarpetas de la unidad", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1)) },
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
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Subcarpetas de la unidad", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), modifier = Modifier.padding(bottom = 24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { /* TODO: Elegir archivos */ }) {
                    Text("Elegir archivos")
                }
                Button(onClick = { /* TODO: Limpiar archivos */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Limpiar archivos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre del link") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* TODO: Adjuntar link */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Adjuntar link")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Archivos adjuntos:", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))

            Spacer(modifier = Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Present continuous - Reading1.pdf", "Present continuous - Reading1.pdf", "Present continuous - Reading1.pdf")) { file ->
                        Text(text = "• $file", color = Color.DarkGray)
                    }
                }
            }
        }
    }
}
