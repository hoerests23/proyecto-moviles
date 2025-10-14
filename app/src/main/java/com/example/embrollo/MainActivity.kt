package com.example.embrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.embrollo.ui.screens.HomeScreen
import com.example.embrollo.ui.screens.HomeScreenCompact
import com.example.embrollo.ui.theme.EmbrolloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmbrolloTheme {
                HomeScreen();
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewCompact() {
    EmbrolloTheme {
        HomeScreenCompact()
    }
}
