package com.example.bls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bls.screens.empresa.UnidadesScreen
import com.example.bls.screens.empresa.viewModel.UnidadesViewModel
import com.example.bls.screens.estudiantes.EstudiantesScreen
import com.example.bls.screens.estudiantes.viewModel.GestionUnidadesViewModel
import com.example.bls.screens.matriculas.GestionMatriculasScreen
import com.example.bls.screens.matriculas.viewModel.GestionMatriculasViewModel
import com.example.bls.screens.misprofes.MisProfesScreen
import com.example.bls.screens.misprofes.viewModel.MisProfesViewModel
import com.example.bls.ui.theme.BLSTheme
import kotlinx.coroutines.launch

data class NavItem(val route: String, val label: String, val icon: ImageVector)

class MainScreenActivity : ComponentActivity() {
    private val unidadesViewModel: UnidadesViewModel by viewModels()
    private val misProfesViewModel: MisProfesViewModel by viewModels()
    private val gestionMatriculasViewModel: GestionMatriculasViewModel by viewModels()
    private val gestionUnidadesViewModel: GestionUnidadesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("AUTH_TOKEN")

        setContent {
            BLSTheme {
                MainScreenNavigation(
                    unidadesViewModel = unidadesViewModel, 
                    misProfesViewModel = misProfesViewModel, 
                    gestionMatriculasViewModel = gestionMatriculasViewModel,
                    gestionUnidadesViewModel = gestionUnidadesViewModel,
                    token = token
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenNavigation(
    unidadesViewModel: UnidadesViewModel, 
    misProfesViewModel: MisProfesViewModel,
    gestionMatriculasViewModel: GestionMatriculasViewModel,
    gestionUnidadesViewModel: GestionUnidadesViewModel,
    token: String?
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItems = listOf(
        NavItem("unidades", "Unidades", Icons.Default.Home),
        NavItem("mis_profes", "Mis profes", Icons.Default.SupervisorAccount),
        NavItem("matriculas", "Matrículas", Icons.Outlined.Assignment),
        NavItem("estudiantes", "Estudiantes", Icons.Default.Face),
        NavItem("perfil", "Perfil", Icons.Default.Person)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF0D47A1) // Azul oscuro de fondo
            ) {
                DrawerHeader()
                Spacer(modifier = Modifier.height(16.dp))
                navItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label, tint = Color.White) },
                        label = { Text(item.label, color = Color.White, fontWeight = FontWeight.SemiBold) },
                        selected = navController.currentDestination?.route == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFF1976D2), // Azul más claro para el seleccionado
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).clip(RoundedCornerShape(50.dp))
                    )
                }
            }
        }
    ) { 
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("BLS App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(navController = navController, startDestination = "unidades", modifier = Modifier.padding(paddingValues)) {
                composable("unidades") {
                    UnidadesScreen(viewModel = unidadesViewModel, token = token)
                }
                composable("mis_profes") {
                    MisProfesScreen(viewModel = misProfesViewModel, token = token)
                }
                composable("matriculas") {
                    GestionMatriculasScreen(viewModel = gestionMatriculasViewModel, token = token)
                }
                composable("estudiantes") {
                    EstudiantesScreen(navController = navController, gestionUnidadesViewModel = gestionUnidadesViewModel, token = token)
                }
                composable("perfil") {
                    Text("Pantalla de Perfil")
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1976D2), Color(0xFF0D47A1))
                )
            )
            .padding(vertical = 32.dp, horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.inicio), // Asegúrate de tener un recurso de imagen en drawable
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Usuario", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("usuario@email.com", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}
