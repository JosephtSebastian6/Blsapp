package com.example.bls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bls.screens.empresa.UnidadesScreen
import com.example.bls.screens.empresa.viewModel.UnidadesViewModel
import com.example.bls.screens.misprofes.MisProfesScreen
import com.example.bls.screens.misprofes.viewModel.MisProfesViewModel
import com.example.bls.ui.theme.BLSTheme
import kotlinx.coroutines.launch

data class NavItem(val route: String, val label: String, val icon: ImageVector)

class MainScreenActivity : ComponentActivity() {
    private val unidadesViewModel: UnidadesViewModel by viewModels()
    private val misProfesViewModel: MisProfesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("AUTH_TOKEN")

        setContent {
            BLSTheme {
                MainScreenNavigation(
                    unidadesViewModel = unidadesViewModel, 
                    misProfesViewModel = misProfesViewModel, 
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
    token: String?
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Lista de ítems de navegación
    val navItems = listOf(
        NavItem("unidades", "Unidades", Icons.Default.Home),
        NavItem("mis_profes", "Mis profes", Icons.Default.SupervisorAccount),
        NavItem("perfil", "Perfil", Icons.Default.Person)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
                    navItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = navController.currentDestination?.route == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
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
            // Contenedor de navegación
            NavHost(navController = navController, startDestination = "unidades", modifier = Modifier.padding(paddingValues)) {
                composable("unidades") {
                    UnidadesScreen(viewModel = unidadesViewModel, token = token)
                }
                composable("mis_profes") {
                    MisProfesScreen(viewModel = misProfesViewModel, token = token)
                }
                composable("perfil") {
                    Text("Pantalla de Perfil")
                }
            }
        }
    }
}
