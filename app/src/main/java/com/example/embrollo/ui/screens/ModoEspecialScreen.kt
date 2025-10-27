package com.example.embrollo.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.embrollo.viewmodels.ModoEspecialViewModel

@Composable
fun ModoEspecialScreen(viewModel: ModoEspecialViewModel = viewModel()) {
    val context = LocalContext.current
    val activado by viewModel.activado.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val mostrarMensaje by viewModel.mostrarMensaje.collectAsState()

    var contador by remember { mutableStateOf(0) }

    val textoDerivado by remember {
        derivedStateOf {
            if (activado) "Modo Activado" else "Modo Desactivado"
        }
    }

    val colorAnimado by animateColorAsState(
        targetValue = if (activado) Color(0xFF4CAF50) else Color(0xFFF44336),
        animationSpec = tween(durationMillis = 600),
        label = "ColorAnimation"
    )

    LaunchedEffect(Unit) {
        viewModel.cargarEstado(context)
    }

    if (cargando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = textoDerivado,
                style = MaterialTheme.typography.headlineMedium,
                color = colorAnimado
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.toggleActivado(context)
                    contador++
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorAnimado
                )
            ) {
                Text("Activar/Desactivar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Veces presionado: $contador")

            AnimatedVisibility(
                visible = mostrarMensaje,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "Estado guardado exitosamente",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            LaunchedEffect(mostrarMensaje) {
                if (mostrarMensaje) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.ocultarMensaje()
                }
            }
        }
    }
}