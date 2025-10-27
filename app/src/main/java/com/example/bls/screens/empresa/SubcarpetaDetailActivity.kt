package com.example.bls.screens.empresa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bls.ui.theme.BLSTheme

class SubcarpetaDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val unitId = intent.getIntExtra("UNIT_ID", -1)
        val subfolderId = intent.getIntExtra("SUBFOLDER_ID", -1)
        val token = intent.getStringExtra("AUTH_TOKEN")

        setContent {
            BLSTheme {
                SubcarpetaDetailScreen(
                    unidadId = unitId, 
                    subcarpetaId = subfolderId,
                    token = token,
                    onBackClicked = { finish() }
                )
            }
        }
    }
}
