package com.opencallshield.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opencallshield.R
import com.opencallshield.data.BlockedCall
import com.opencallshield.data.Countries
import com.opencallshield.data.SpamNumber
import com.opencallshield.data.SpamRepository
import com.opencallshield.engine.SpamDetector
import java.text.DateFormat
import java.util.Date

private const val REPO_URL = "https://github.com/jhonsu01/OpenCallShield"
private const val KOFI_URL = "https://ko-fi.com/V7V81LV7GX"
private const val GUIDE_URL =
    "https://github.com/jhonsu01/OpenCallShield/blob/main/docs/CREAR_BASE_COLABORATIVA.md"

/**
 * Idiomas de la interfaz: etiqueta BCP 47 + nombre en su propio idioma (endonimo), que
 * es como cada usuario reconoce el suyo aunque la app este en otro idioma.
 */
private val LANGUAGES = listOf(
    "en" to "English",
    "es" to "Español",
    "pt" to "Português",
    "fr" to "Français",
    "de" to "Deutsch",
    "ru" to "Русский",
    "zh-CN" to "中文 (简体)",
    "ja" to "日本語",
    "ko" to "한국어"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestRole: () -> Unit
) {
    val tabs = listOf(
        stringResource(R.string.tab_protection),
        stringResource(R.string.tab_spam_list),
        stringResource(R.string.tab_history)
    )
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAccount by remember { mutableStateOf(false) }
    var showLanguage by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val spamNumbers by viewModel.spamNumbers.collectAsStateWithLifecycle()
    val blockedCalls by viewModel.blockedCalls.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHost.showSnackbar(it.resolve(context))
            viewModel.consumeMessage()
        }
    }
    LaunchedEffect(authState.message) {
        authState.message?.let {
            snackbarHost.showSnackbar(it.resolve(context))
            viewModel.consumeAuthMessage()
        }
    }

    if (showLanguage) {
        LanguageDialog(onDismiss = { showLanguage = false })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Shield, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (showAccount) stringResource(R.string.title_collaborate)
                            else stringResource(R.string.app_name)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLanguage = true }) {
                        Icon(
                            Icons.Filled.Language,
                            contentDescription = stringResource(R.string.cd_language)
                        )
                    }
                    IconButton(onClick = { showAccount = !showAccount }) {
                        Icon(
                            if (showAccount) Icons.Filled.Close else Icons.Filled.MoreVert,
                            contentDescription = stringResource(
                                if (showAccount) R.string.cd_close else R.string.cd_more_options
                            )
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

/**
 * Selector de idioma de la app. AppCompat guarda la eleccion y recrea la Activity;
 * en Android 13+ queda ademas sincronizado con Ajustes > Apps > Idioma.
 */
@Composable
private fun LanguageDialog(onDismiss: () -> Unit) {
    val current = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    // "zh-Hans-CN" o "es-419" deben marcar su opcion: se compara por idioma base.
    fun isSelected(tag: String) = when {
        current.isEmpty() -> tag.isEmpty()
        tag.isEmpty() -> false
        else -> current.substringBefore(',').substringBefore('-') == tag.substringBefore('-')
    }
    fun choose(tag: String) {
        AppCompatDelegate.setApplicationLocales(
            if (tag.isEmpty()) LocaleListCompat.getEmptyLocaleList()
            else LocaleListCompat.forLanguageTags(tag)
        )
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language_title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                (listOf("" to stringResource(R.string.language_system)) + LANGUAGES).forEach { (tag, name) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { choose(tag) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSelected(tag), onClick = { choose(tag) })
                        Spacer(Modifier.width(8.dp))
                        Text(name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
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
    val locale = LocalConfiguration.current.locales[0]
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
                    stringResource(R.string.protection_active),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    stringResource(R.string.protection_stats, spamCount, blockedCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Button(onClick = onRequestRole, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Shield, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.activate_screening))
        }
        Text(
            stringResource(R.string.activate_screening_hint),
            style = MaterialTheme.typography.bodySmall
        )

        HorizontalDivider()

        SettingRow(
            title = stringResource(R.string.block_unknown_title),
            subtitle = stringResource(R.string.block_unknown_sub),
            checked = state.blockUnknown,
            onCheckedChange = viewModel::setBlockUnknown
        )
        SettingRow(
            title = stringResource(R.string.block_prefixes_title),
            subtitle = stringResource(R.string.block_prefixes_sub),
            checked = state.blockPrefixes,
            onCheckedChange = viewModel::setBlockPrefixes
        )
        SettingRow(
            title = stringResource(R.string.silence_title),
            subtitle = stringResource(R.string.silence_sub),
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
                if (countriesCount > 0) stringResource(R.string.block_by_country_count, countriesCount)
                else stringResource(R.string.block_by_country)
            )
        }
        if (countriesOpen) {
            Text(
                stringResource(R.string.countries_hint),
                style = MaterialTheme.typography.bodySmall
            )
            val premium = stringResource(R.string.country_premium)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Countries.ALL.forEach { c ->
                    FilterChip(
                        selected = c.dialCode in activePrefixes,
                        onClick = { viewModel.toggleCountryPrefix(c.dialCode) },
                        label = { Text("${c.flag} ${c.displayName(locale) ?: premium}") }
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
                Text(stringResource(R.string.syncing))
            } else {
                Text(stringResource(R.string.sync_now))
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
            Text(stringResource(R.string.advanced_settings))
        }
        if (advancedOpen) {
            OutlinedTextField(
                value = state.syncUrl,
                onValueChange = viewModel::setSyncUrl,
                label = { Text(stringResource(R.string.sync_url_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.prefixes,
                onValueChange = viewModel::setPrefixes,
                label = { Text(stringResource(R.string.manual_prefixes_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            TextButton(
                onClick = { uriHandler.openUri(GUIDE_URL) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.guide_link))
            }
        }

        HorizontalDivider()

        // Donaciones (Ko-fi)
        Button(
            onClick = { uriHandler.openUri(KOFI_URL) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29ABE0))
        ) {
            Text(stringResource(R.string.kofi_button))
        }
        Text(
            stringResource(R.string.kofi_hint),
            style = MaterialTheme.typography.bodySmall
        )

        // Pie: enlace al repositorio
        TextButton(
            onClick = { uriHandler.openUri(REPO_URL) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.repo_link))
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
                label = { Text(stringResource(R.string.number_to_report)) },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                viewModel.report(input)
                input = ""
            }) {
                Icon(Icons.Filled.Block, contentDescription = stringResource(R.string.cd_report))
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
                if (authState.loggedIn) stringResource(R.string.contribute_n, localCount)
                else stringResource(R.string.contribute_optional)
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
                Text(stringResource(R.string.syncing))
            } else {
                Icon(Icons.Filled.Sync, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.sync_public_db))
            }
        }
        Text(
            stringResource(R.string.sync_first_hint),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.size(12.dp))
        if (numbers.isEmpty()) {
            EmptyState(stringResource(R.string.empty_spam_list))
        } else {
            val sourceLocal = stringResource(R.string.source_local)
            val sourceGithub = stringResource(R.string.source_github)
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
                                    stringResource(
                                        R.string.spam_item_meta,
                                        item.tag,
                                        item.reports,
                                        if (item.source == "local") sourceLocal else sourceGithub
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = { viewModel.remove(item) }) {
                                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.cd_delete))
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Numero oculto: vacio desde v1.5.0, "(oculto)" en registros de versiones anteriores. */
private fun isHiddenNumber(number: String) = number.isBlank() || number == "(oculto)"

/**
 * Traduce el motivo guardado. Desde v1.5.0 es un codigo (SpamDetector.REASON_*); los
 * registros anteriores guardaban la frase en espanol, que tambien se reconoce aqui.
 */
@Composable
private fun reasonText(reason: String): String {
    val legacyPrefix = Regex("""Prefijo sospechoso \((.*)\)""").matchEntire(reason)
    return when {
        reason == SpamDetector.REASON_SPAM || reason == "Reportado como SPAM" ->
            stringResource(R.string.reason_spam)
        reason.startsWith(SpamDetector.REASON_PREFIX) ->
            stringResource(R.string.reason_prefix, reason.removePrefix(SpamDetector.REASON_PREFIX))
        legacyPrefix != null ->
            stringResource(R.string.reason_prefix, legacyPrefix.groupValues[1])
        reason == SpamDetector.REASON_HIDDEN || reason == "Numero oculto" ->
            stringResource(R.string.reason_hidden)
        reason == SpamDetector.REASON_UNKNOWN || reason == "Numero fuera de contactos" ->
            stringResource(R.string.reason_unknown)
        else -> reason
    }
}

@Composable
private fun HistoryTab(
    calls: List<BlockedCall>,
    spamNumbers: List<SpamNumber>,
    viewModel: MainViewModel
) {
    val locale = LocalConfiguration.current.locales[0]
    val formatter = remember(locale) {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
    }
    val spamSet = remember(spamNumbers) { spamNumbers.map { it.number }.toHashSet() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.filtered_calls), style = MaterialTheme.typography.titleMedium)
            if (calls.isNotEmpty()) {
                OutlinedButton(onClick = { viewModel.clearHistory() }) {
                    Icon(Icons.Filled.History, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.clear))
                }
            }
        }
        Text(
            stringResource(R.string.history_hint),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.size(12.dp))
        if (calls.isEmpty()) {
            EmptyState(stringResource(R.string.empty_history))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calls, key = { it.id }) { call ->
                    val hidden = isHiddenNumber(call.number)
                    val normalized = SpamRepository.normalize(call.number)
                    val inList = normalized in spamSet
                    val action = stringResource(
                        if (call.silenced) R.string.call_silenced else R.string.call_rejected
                    )
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    if (hidden) stringResource(R.string.hidden_number) else call.number,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "$action · ${reasonText(call.reason)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    formatter.format(Date(call.timestamp)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            // Un numero oculto no se puede anadir a la lista de SPAM.
                            if (!hidden) {
                                if (inList) {
                                    IconButton(onClick = { viewModel.removeNumber(call.number) }) {
                                        Icon(
                                            Icons.Filled.CheckCircle,
                                            contentDescription = stringResource(R.string.cd_remove_from_spam),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { viewModel.addToSpam(call.number) }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription = stringResource(R.string.cd_add_to_spam)
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
            val method = when (authState.method) {
                "device" -> stringResource(R.string.method_device)
                "pat" -> stringResource(R.string.method_pat)
                else -> authState.method ?: "-"
            }
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.connected_as, authState.login.orEmpty()),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(Modifier.size(4.dp))
                    Text(
                        stringResource(R.string.connected_method, method),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            OutlinedButton(onClick = { viewModel.logout() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.logout))
            }
            return@Column
        }

        // Estado del Device Flow en curso
        val device = authState.deviceCode
        if (device != null) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.device_step1), style = MaterialTheme.typography.bodyMedium)
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
                        Text(stringResource(R.string.open_url, device.verificationUri))
                    }
                    Spacer(Modifier.size(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.waiting_auth), style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.size(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.cancelDeviceLogin() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.cancel)) }
                }
            }
            return@Column
        }

        Text(
            stringResource(R.string.collaborate_intro),
            style = MaterialTheme.typography.bodyMedium
        )

        // --- Device Flow ---
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.option_a), style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = authState.clientId,
                    onValueChange = viewModel::setClientId,
                    label = { Text(stringResource(R.string.client_id_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = { viewModel.startDeviceLogin() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.busy
                ) {
                    Text(stringResource(R.string.connect))
                }
                Text(
                    stringResource(R.string.device_hint),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // --- PAT ---
        var pat by remember { mutableStateOf("") }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.option_b), style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = pat,
                    onValueChange = { pat = it },
                    label = { Text(stringResource(R.string.token_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedButton(
                    onClick = { viewModel.loginWithPat(pat) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.busy
                ) {
                    Text(stringResource(R.string.connect_with_token))
                }
                Text(
                    stringResource(R.string.token_hint),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (authState.busy) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.processing), style = MaterialTheme.typography.bodySmall)
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
