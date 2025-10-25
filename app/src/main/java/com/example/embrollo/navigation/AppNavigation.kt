package com.example.embrollo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.embrollo.ui.screens.RegistroScreen
import com.example.embrollo.ui.screens.ResumenScreen
import com.example.embrollo.viewmodels.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ) {
        composable( route = "registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable( route = "resumen") {
            ResumenScreen(usuarioViewModel)
        }
    }
}
