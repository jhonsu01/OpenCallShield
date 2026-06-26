package com.opencallshield.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opencallshield.data.BlockedCall
import com.opencallshield.data.SpamNumber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestRole: () -> Unit
) {
    val tabs = listOf("Proteccion", "Lista SPAM", "Historial")
    var selectedTab by remember { mutableIntStateOf(0) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val spamNumbers by viewModel.spamNumbers.collectAsStateWithLifecycle()
    val blockedCalls by viewModel.blockedCalls.collectAsStateWithLifecycle()

    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHost.showSnackbar(it)
            viewModel.consumeMessage()
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
                        Text("OpenCallShield")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
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
                1 -> SpamListTab(spamNumbers, viewModel)
                else -> HistoryTab(blockedCalls, viewModel)
            }
        }
    }
}

@Composable
private fun ProtectionTab(
    state: SettingsUiState,
    viewModel: MainViewModel,
    onRequestRole: () -> Unit,
    spamCount: Int,
    blockedCount: Int
) {
    Column(
        Modifier
            .fillMaxSize()
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

        Divider()

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

        OutlinedTextField(
            value = state.prefixes,
            onValueChange = viewModel::setPrefixes,
            label = { Text("Prefijos en lista negra (separados por coma)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Divider()

        OutlinedTextField(
            value = state.syncUrl,
            onValueChange = viewModel::setSyncUrl,
            label = { Text("URL de base colaborativa (JSON)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
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
    viewModel: MainViewModel
) {
    var input by remember { mutableStateOf("") }

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
    viewModel: MainViewModel
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

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
        Spacer(Modifier.size(12.dp))
        if (calls.isEmpty()) {
            EmptyState("Todavia no se ha filtrado ninguna llamada.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calls, key = { it.id }) { call ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
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
                    }
                }
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
