package com.example.bls.screens.empresa

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bls.data.model.Subcarpeta
import com.example.bls.screens.empresa.viewModel.SubcarpetasState
import com.example.bls.screens.empresa.viewModel.SubcarpetasViewModel
import com.example.bls.ui.theme.BLSTheme

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
                SubcarpetasScreen(viewModel = subcarpetasViewModel, unitName = unitName)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcarpetasScreen(viewModel: SubcarpetasViewModel, unitName: String) {
    val subcarpetasState by viewModel.subcarpetasState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = unitName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Subcarpetas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when (val state = subcarpetasState) {
                is SubcarpetasState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is SubcarpetasState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        items(state.subcarpetas) { subcarpeta ->
                            SubcarpetaCard(subcarpeta)
                        }
                    }
                }
                is SubcarpetasState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color.Red, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun SubcarpetaCard(subcarpeta: Subcarpeta) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Folder,
                contentDescription = "Folder Icon",
                tint = Color(0xFF5C6BC0),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = subcarpeta.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Chip(text = "Presente", backgroundColor = Color(0xFFE8F5E9), textColor = Color(0xFF388E3C))

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(text = "Editar", backgroundColor = Color(0xFFE8EAF6), textColor = Color(0xFF3F51B5))
                ActionButton(text = "Eliminar", backgroundColor = Color(0xFFFFEBEE), textColor = Color(0xFFD32F2F))
                ActionButton(text = "Ocultar", backgroundColor = Color(0xFFF5F5F5), textColor = Color.DarkGray)
            }
        }
    }
}

@Composable
fun Chip(text: String, backgroundColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ActionButton(text: String, backgroundColor: Color, textColor: Color) {
    Button(
        onClick = { /* Acción del botón */ },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SubcarpetasScreenPreview() {
    BLSTheme {
        // SubcarpetasScreen(viewModel = SubcarpetasViewModel(), unitName = "Unidad de Prueba")
    }
}
