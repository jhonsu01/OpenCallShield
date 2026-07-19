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
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
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

    // --- Actualizaciones dentro de la app (Google Play In-App Updates) ---
    private lateinit var appUpdateManager: AppUpdateManager

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { /* resultado del flujo de actualizacion gestionado por Play */ }

    private val installListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showUpdateReadyPrompt()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermissions()

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdate()

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

    /**
     * Comprueba en Google Play si hay una nueva version y, si la hay, inicia la
     * descarga flexible en segundo plano. Al terminar se avisa al usuario para reiniciar.
     * (Solo funciona en apps instaladas desde Play Store.)
     */
    private fun checkForAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val available = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            if (available && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                appUpdateManager.registerListener(installListener)
                runCatching {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Si una actualizacion quedo descargada (p. ej. al volver a abrir), ofrecer instalarla.
        if (::appUpdateManager.isInitialized) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.installStatus() == InstallStatus.DOWNLOADED) showUpdateReadyPrompt()
            }
        }
    }

    private fun showUpdateReadyPrompt() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Actualizacion disponible")
            .setMessage(
                "Se descargo una nueva version de OpenCallShield. " +
                    "Reinicia la app para aplicarla y ver las novedades."
            )
            .setPositiveButton("Reiniciar ahora") { _, _ -> appUpdateManager.completeUpdate() }
            .setNegativeButton("Mas tarde", null)
            .show()
    }

    override fun onDestroy() {
        if (::appUpdateManager.isInitialized) appUpdateManager.unregisterListener(installListener)
        super.onDestroy()
    }
}
