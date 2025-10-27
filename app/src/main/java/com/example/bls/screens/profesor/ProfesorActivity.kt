package com.example.bls.screens.profesor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bls.ui.theme.BLSTheme

class ProfesorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BLSTheme {
                ProfesorDashboardScreen()
            }
        }
    }
}
