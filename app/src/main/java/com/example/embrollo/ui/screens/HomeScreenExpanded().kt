package com.example.embrollo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.embrollo.R
import com.example.embrollo.ui.theme.EmbrolloTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpanded() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Mi App Kotlin") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 64.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //texto + botón
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "¡Bienvenido a Mi App Kotlin!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "Explora las funcionalidades de la aplicación con una interfaz adaptada a tu pantalla.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Button(
                    onClick = { /* acción para dps */ },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Comenzar")
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(start = 32.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(name = "Expanded", widthDp = 840, heightDp = 800)
@Composable
fun PreviewExpanded() {
    EmbrolloTheme {
        HomeScreenExpanded()
    }
}