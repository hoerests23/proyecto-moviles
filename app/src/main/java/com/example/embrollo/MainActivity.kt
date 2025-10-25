package com.example.embrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.embrollo.ui.theme.EmbrolloTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.embrollo.navigation.NavigationEvent
import com.example.embrollo.navigation.Screen
import com.example.embrollo.ui.screens.HomeScreen
import com.example.embrollo.ui.screens.ProfileScreen
import com.example.embrollo.ui.screens.RegistroScreen
import com.example.embrollo.ui.screens.ResumenScreen
import com.example.embrollo.ui.screens.SettingsScreen
import com.example.embrollo.viewmodels.MainViewModel
import com.example.embrollo.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmbrolloTheme {
                val mainViewModel: MainViewModel = viewModel()
                val usuarioViewModel: UsuarioViewModel = viewModel()
                val navController = rememberNavController()

                // escucha eventos emitidos por viewmodel
                LaunchedEffect(key1 = Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,  // O "registro" si quieres empezar ahí
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // principal
                        composable(route = Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(navController = navController, viewModel = mainViewModel)
                        }

                        // registro
                        composable(route = "registro") {
                            RegistroScreen(navController, usuarioViewModel)
                        }
                        composable(route = "resumen") {
                            ResumenScreen(usuarioViewModel)
                        }
                    }
                }
            }
        }
    }
}