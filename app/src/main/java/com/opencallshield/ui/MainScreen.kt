package com.opencallshield.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opencallshield.data.BlockedCall
import com.opencallshield.data.Countries
import com.opencallshield.data.SpamNumber
import com.opencallshield.data.SpamRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REPO_URL = "https://github.com/jhonsu01/OpenCallShield"
private const val KOFI_URL = "https://ko-fi.com/V7V81LV7GX"
private const val GUIDE_URL =
    "https://github.com/jhonsu01/OpenCallShield/blob/main/docs/CREAR_BASE_COLABORATIVA.md"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestRole: () -> Unit
) {
    val tabs = listOf("Proteccion", "Lista SPAM", "Historial")
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAccount by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val spamNumbers by viewModel.spamNumbers.collectAsStateWithLifecycle()
    val blockedCalls by viewModel.blockedCalls.collectAsStateWithLifecycle()

    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHost.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }
    LaunchedEffect(authState.message) {
        authState.message?.let {
            snackbarHost.showSnackbar(it)
            viewModel.consumeAuthMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Shield, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (showAccount) "Colaborar" else "OpenCallShield")
                    }
                },
                actions = {
                    IconButton(onClick = { showAccount = !showAccount }) {
                        Icon(
                            if (showAccount) Icons.Filled.Close else Icons.Filled.MoreVert,
                            contentDescription = if (showAccount) "Cerrar" else "Mas opciones"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            if (showAccount) {
                AccountTab(authState, viewModel)
            } else {
                ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 0.dp) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
                when (selectedTab) {
                    0 -> ProtectionTab(state, viewModel, onRequestRole, spamNumbers.size, blockedCalls.size)
                    1 -> SpamListTab(spamNumbers, authState, state.syncing, viewModel) { showAccount = true }
                    else -> HistoryTab(blockedCalls, spamNumbers, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProtectionTab(
    state: SettingsUiState,
    viewModel: MainViewModel,
    onRequestRole: () -> Unit,
    spamCount: Int,
    blockedCount: Int
) {
    val uriHandler = LocalUriHandler.current
    var advancedOpen by remember { mutableStateOf(false) }
    var countriesOpen by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Proteccion activa",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    "$spamCount numeros en lista negra  -  $blockedCount llamadas filtradas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Button(onClick = onRequestRole, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Shield, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Activar como app de filtrado de llamadas")
        }
        Text(
            "Android pedira permiso para que OpenCallShield filtre tus llamadas. " +
                "Es necesario para bloquear el SPAM.",
            style = MaterialTheme.typography.bodySmall
        )

        HorizontalDivider()

        SettingRow(
            title = "Bloquear numeros desconocidos",
            subtitle = "Rechaza llamadas que no esten en tus contactos",
            checked = state.blockUnknown,
            onCheckedChange = viewModel::setBlockUnknown
        )
        SettingRow(
            title = "Bloquear prefijos sospechosos",
            subtitle = "Usa la lista negra de prefijos internacionales",
            checked = state.blockPrefixes,
            onCheckedChange = viewModel::setBlockPrefixes
        )
        SettingRow(
            title = "Silenciar en vez de rechazar",
            subtitle = "La llamada no suena pero queda como perdida",
            checked = state.silence,
            onCheckedChange = viewModel::setSilence
        )

        // --- Selector de paises por bandera (colapsable) ---
        val activePrefixes = remember(state.prefixes) {
            state.prefixes.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        }
        val countriesCount = Countries.ALL.count { it.dialCode in activePrefixes }
        TextButton(
            onClick = { countriesOpen = !countriesOpen },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                if (countriesOpen) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (countriesCount > 0) "Bloquear llamadas por pais  ($countriesCount)"
                else "Bloquear llamadas por pais"
            )
        }
        if (countriesOpen) {
            Text(
                "Toca las banderas de los paises cuyas llamadas NO quieres recibir. " +
                    "No necesitas escribir prefijos.",
                style = MaterialTheme.typography.bodySmall
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Countries.ALL.forEach { c ->
                    FilterChip(
                        selected = c.dialCode in activePrefixes,
                        onClick = { viewModel.toggleCountryPrefix(c.dialCode) },
                        label = { Text("${c.flag} ${c.name}") }
                    )
                }
            }
        }

        HorizontalDivider()

        OutlinedButton(
            onClick = { viewModel.syncNow() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.syncing
        ) {
            if (state.syncing) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Sincronizando...")
            } else {
                Text("Sincronizar ahora")
            }
        }

        // --- Ajustes avanzados (colapsable): URL de la base y prefijos manuales ---
        TextButton(
            onClick = { advancedOpen = !advancedOpen },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                if (advancedOpen) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Ajustes avanzados")
        }
        if (advancedOpen) {
            OutlinedTextField(
                value = state.syncUrl,
                onValueChange = viewModel::setSyncUrl,
                label = { Text("URL de la base colaborativa (JSON)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.prefixes,
                onValueChange = viewModel::setPrefixes,
                label = { Text("Prefijos manuales (avanzado, separados por coma)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            TextButton(
                onClick = { uriHandler.openUri(GUIDE_URL) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Como crear tu propia base colaborativa? (guia)")
            }
        }

        HorizontalDivider()

        // Donaciones (Ko-fi)
        Button(
            onClick = { uriHandler.openUri(KOFI_URL) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29ABE0))
        ) {
            Text("☕  Apoyame en Ko-fi")
        }
        Text(
            "OpenCallShield es gratuito y open source. Si te resulta util, considera apoyarlo.",
            style = MaterialTheme.typography.bodySmall
        )

        // Pie: enlace al repositorio
        TextButton(
            onClick = { uriHandler.openUri(REPO_URL) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OpenCallShield  -  ver el proyecto en GitHub")
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SpamListTab(
    numbers: List<SpamNumber>,
    authState: AuthUiState,
    syncing: Boolean,
    viewModel: MainViewModel,
    onOpenCollaborate: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val localCount = numbers.count { it.source == "local" }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Numero a reportar") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                viewModel.report(input)
                input = ""
            }) {
                Icon(Icons.Filled.Block, contentDescription = "Reportar")
            }
        }

        Spacer(Modifier.size(8.dp))
        OutlinedButton(
            onClick = { if (authState.loggedIn) viewModel.contribute() else onOpenCollaborate() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !authState.busy
        ) {
            Icon(Icons.Filled.CloudUpload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                if (authState.loggedIn) "Aportar $localCount numero(s) a la base publica"
                else "Aportar a la base publica (opcional)"
            )
        }

        Spacer(Modifier.size(8.dp))
        OutlinedButton(
            onClick = { viewModel.syncNow() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !syncing
        ) {
            if (syncing) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Sincronizando...")
            } else {
                Icon(Icons.Filled.Sync, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Sincronizar base publica")
            }
        }
        Text(
            "Sincroniza primero la base publica: los numeros que ya esten en ella no se " +
                "vuelven a reportar. Asi solo aportas numeros nuevos y evitas duplicados.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.size(12.dp))
        if (numbers.isEmpty()) {
            EmptyState("Aun no hay numeros reportados.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(numbers, key = { it.number }) { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.number, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    "${item.tag} - ${item.reports} reportes - ${item.source}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = { viewModel.remove(item) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTab(
    calls: List<BlockedCall>,
    spamNumbers: List<SpamNumber>,
    viewModel: MainViewModel
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val spamSet = remember(spamNumbers) { spamNumbers.map { it.number }.toHashSet() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Llamadas filtradas", style = MaterialTheme.typography.titleMedium)
            if (calls.isNotEmpty()) {
                OutlinedButton(onClick = { viewModel.clearHistory() }) {
                    Icon(Icons.Filled.History, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }
            }
        }
        Text(
            "Toca + para anadir el numero a la lista de SPAM, o el check para quitarlo.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.size(12.dp))
        if (calls.isEmpty()) {
            EmptyState("Todavia no se ha filtrado ninguna llamada.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calls, key = { it.id }) { call ->
                    val normalized = SpamRepository.normalize(call.number)
                    val inList = normalized in spamSet
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    call.number,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${if (call.silenced) "Silenciada" else "Rechazada"} - ${call.reason}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    formatter.format(Date(call.timestamp)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            if (inList) {
                                IconButton(onClick = { viewModel.removeNumber(call.number) }) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = "Quitar de SPAM",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                IconButton(onClick = { viewModel.addToSpam(call.number) }) {
                                    Icon(
                                        Icons.Filled.AddCircle,
                                        contentDescription = "Anadir a SPAM"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountTab(
    authState: AuthUiState,
    viewModel: MainViewModel
) {
    val uriHandler = LocalUriHandler.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (authState.loggedIn) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Conectado como @${authState.login}", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.size(4.dp))
                    Text(
                        "Metodo: ${authState.method ?: "-"}. Tus aportes se envian como Issue al repositorio publico.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            OutlinedButton(onClick = { viewModel.logout() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesion")
            }
            return@Column
        }

        // Estado del Device Flow en curso
        val device = authState.deviceCode
        if (device != null) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Paso 1 - Escribe este codigo en GitHub:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.size(8.dp))
                    Text(
                        device.userCode,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(Modifier.size(12.dp))
                    Button(
                        onClick = { uriHandler.openUri(device.verificationUri) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Abrir ${device.verificationUri}")
                    }
                    Spacer(Modifier.size(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Esperando autorizacion...", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.size(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.cancelDeviceLogin() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Cancelar") }
                }
            }
            return@Column
        }

        Text(
            "Colaborar es OPCIONAL. La app funciona completa sin esto. Conecta una " +
                "cuenta solo si quieres aportar numeros a la base publica colaborativa.",
            style = MaterialTheme.typography.bodyMedium
        )

        // --- Device Flow ---
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Opcion A - Codigo de dispositivo", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = authState.clientId,
                    onValueChange = viewModel::setClientId,
                    label = { Text("Client ID (publico)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = { viewModel.startDeviceLogin() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.busy
                ) {
                    Text("Conectar")
                }
                Text(
                    "Se abrira github.com/login/device para autorizar. El Client ID es publico.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // --- PAT ---
        var pat by remember { mutableStateOf("") }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Opcion B - Token personal (PAT)", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = pat,
                    onValueChange = { pat = it },
                    label = { Text("Token (scope public_repo)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedButton(
                    onClick = { viewModel.loginWithPat(pat) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.busy
                ) {
                    Text("Conectar con token")
                }
                Text(
                    "Crea el token en github.com/settings/tokens con permiso public_repo.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (authState.busy) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Procesando...", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Shield,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.size(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium)
    }
}
