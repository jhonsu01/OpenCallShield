package com.opencallshield

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.opencallshield.ui.MainScreen
import com.opencallshield.ui.MainViewModel
import com.opencallshield.ui.theme.OpenCallShieldTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* el usuario decide; las reglas degradan con seguridad si falta permiso */ }

    private val roleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* resultado del rol gestionado por el sistema */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermissions()

        setContent {
            OpenCallShieldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        onRequestRole = ::requestScreeningRole
                    )
                }
            }
        }
    }

    private fun requestRuntimePermissions() {
        // Solo se necesita READ_CONTACTS (para no bloquear a contactos conocidos).
        // El filtrado de llamadas se concede aparte mediante ROLE_CALL_SCREENING.
        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS))
    }

    /**
     * Solicita el rol ROLE_CALL_SCREENING. Sin este rol el sistema no enlaza
     * el CallScreeningService y no se pueden filtrar llamadas.
     */
    private fun requestScreeningRole() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) &&
            !roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        ) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            roleLauncher.launch(intent)
        }
    }
}
