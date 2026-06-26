package com.opencallshield.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opencallshield.auth.GitHubAuthManager
import com.opencallshield.data.AppDatabase
import com.opencallshield.data.BlockedCall
import com.opencallshield.data.SettingsStore
import com.opencallshield.data.SpamNumber
import com.opencallshield.data.SpamRepository
import com.opencallshield.data.TokenStore
import com.opencallshield.sync.GitHubContributor
import com.opencallshield.sync.GitHubSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SettingsUiState(
    val blockUnknown: Boolean = false,
    val blockPrefixes: Boolean = true,
    val silence: Boolean = false,
    val prefixes: String = SettingsStore.DEFAULT_PREFIXES,
    val syncUrl: String = SettingsStore.DEFAULT_SYNC_URL,
    val syncing: Boolean = false,
    val message: String? = null
)

data class AuthUiState(
    val loggedIn: Boolean = false,
    val login: String? = null,
    val method: String? = null,
    val clientId: String = "",
    val deviceCode: GitHubAuthManager.DeviceCode? = null,
    val busy: Boolean = false,
    val message: String? = null
)

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsStore(app)
    private val repo = SpamRepository(AppDatabase.get(app).spamDao(), settings)
    private val tokenStore = TokenStore(app)
    private val auth = GitHubAuthManager()

    private var pollJob: Job? = null

    val spamNumbers: StateFlow<List<SpamNumber>> =
        repo.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val blockedCalls: StateFlow<List<BlockedCall>> =
        repo.observeBlocked()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _state = MutableStateFlow(currentSettings())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val _auth = MutableStateFlow(currentAuth())
    val authState: StateFlow<AuthUiState> = _auth.asStateFlow()

    private fun currentSettings() = SettingsUiState(
        blockUnknown = settings.blockUnknown,
        blockPrefixes = settings.blockPrefixes,
        silence = settings.silenceInsteadOfReject,
        prefixes = settings.prefixList,
        syncUrl = settings.syncUrl
    )

    private fun currentAuth() = AuthUiState(
        loggedIn = tokenStore.isLoggedIn,
        login = tokenStore.login,
        method = tokenStore.method,
        clientId = settings.githubClientId
    )

    // ---- Reglas / base local ----

    fun report(number: String) = viewModelScope.launch {
        if (number.isNotBlank()) repo.report(number)
    }

    fun remove(item: SpamNumber) = viewModelScope.launch { repo.remove(item.number) }

    /** Anade un numero (desde el historial) a la lista de SPAM. */
    fun addToSpam(number: String) = viewModelScope.launch { repo.report(number) }

    /** Quita un numero de la lista de SPAM por su valor. */
    fun removeNumber(number: String) = viewModelScope.launch { repo.remove(number) }

    fun clearHistory() = viewModelScope.launch { repo.clearHistory() }

    fun setBlockUnknown(value: Boolean) {
        settings.blockUnknown = value
        _state.update { it.copy(blockUnknown = value) }
    }

    fun setBlockPrefixes(value: Boolean) {
        settings.blockPrefixes = value
        _state.update { it.copy(blockPrefixes = value) }
    }

    fun setSilence(value: Boolean) {
        settings.silenceInsteadOfReject = value
        _state.update { it.copy(silence = value) }
    }

    fun setPrefixes(value: String) {
        settings.prefixList = value
        _state.update { it.copy(prefixes = value) }
    }

    fun setSyncUrl(value: String) {
        settings.syncUrl = value
        _state.update { it.copy(syncUrl = value) }
    }

    fun syncNow() = viewModelScope.launch {
        _state.update { it.copy(syncing = true, message = null) }
        val message = withContext(Dispatchers.IO) {
            try {
                val remote = GitHubSync.fetch(settings.syncUrl)
                repo.mergeRemote(remote)
                "Sincronizado: ${remote.size} numeros recibidos"
            } catch (e: Exception) {
                "Error al sincronizar: ${e.message ?: "desconocido"}"
            }
        }
        _state.update { it.copy(syncing = false, message = message) }
    }

    fun consumeMessage() = _state.update { it.copy(message = null) }

    // ---- Autenticacion GitHub ----

    fun setClientId(value: String) {
        settings.githubClientId = value.trim()
        _auth.update { it.copy(clientId = value.trim()) }
    }

    fun startDeviceLogin() {
        val clientId = settings.githubClientId.trim()
        if (clientId.isEmpty()) {
            _auth.update { it.copy(message = "Configura el Client ID de la OAuth App primero.") }
            return
        }
        pollJob?.cancel()
        _auth.update { it.copy(busy = true, message = null, deviceCode = null) }
        pollJob = viewModelScope.launch {
            val device = withContext(Dispatchers.IO) {
                runCatching { auth.startDeviceFlow(clientId) }
            }.getOrElse { e ->
                _auth.update { it.copy(busy = false, message = friendlyError(e)) }
                return@launch
            }
            _auth.update { it.copy(deviceCode = device, busy = true) }
            pollDeviceFlow(clientId, device)
        }
    }

    private suspend fun pollDeviceFlow(clientId: String, device: GitHubAuthManager.DeviceCode) {
        var interval = device.interval
        val deadline = System.currentTimeMillis() + device.expiresIn * 1000L
        while (viewModelScope.isActive && System.currentTimeMillis() < deadline) {
            delay(interval * 1000L)
            // Un fallo de red transitorio NO debe abortar el flujo: seguimos
            // reintentando hasta que el usuario autorice o el codigo expire.
            val outcome = withContext(Dispatchers.IO) {
                runCatching { auth.pollForToken(clientId, device.deviceCode) }
            }
            val result = outcome.getOrNull() ?: continue
            when (result) {
                is GitHubAuthManager.PollResult.Success -> {
                    finishLogin(result.token, "device")
                    return
                }
                is GitHubAuthManager.PollResult.Pending -> { /* seguir esperando */ }
                is GitHubAuthManager.PollResult.SlowDown -> interval = result.interval
                is GitHubAuthManager.PollResult.Error -> {
                    // Error del protocolo OAuth (codigo expirado o acceso denegado): abortar.
                    _auth.update { it.copy(busy = false, deviceCode = null, message = result.message) }
                    return
                }
            }
        }
        _auth.update { it.copy(busy = false, deviceCode = null, message = "El codigo expiro, intenta de nuevo.") }
    }

    private fun friendlyError(e: Throwable): String = when (e) {
        is java.net.UnknownHostException ->
            "Sin conexion o el DNS no resuelve github.com. Revisa tu red e intenta de nuevo."
        is java.net.SocketTimeoutException ->
            "La conexion tardo demasiado. Intenta de nuevo."
        else -> e.message ?: "Error de red"
    }

    fun loginWithPat(rawToken: String) {
        val token = rawToken.trim()
        if (token.isEmpty()) {
            _auth.update { it.copy(message = "Pega un token valido.") }
            return
        }
        _auth.update { it.copy(busy = true, message = null) }
        viewModelScope.launch { finishLogin(token, "pat") }
    }

    private suspend fun finishLogin(token: String, method: String) {
        val login = withContext(Dispatchers.IO) {
            runCatching { auth.fetchLogin(token) }
        }.getOrElse { e ->
            _auth.update { it.copy(busy = false, deviceCode = null, message = friendlyError(e)) }
            return
        }
        tokenStore.save(token, login, method)
        _auth.update {
            it.copy(
                loggedIn = true,
                login = login,
                method = method,
                busy = false,
                deviceCode = null,
                message = "Sesion iniciada como @$login"
            )
        }
    }

    fun cancelDeviceLogin() {
        pollJob?.cancel()
        _auth.update { it.copy(busy = false, deviceCode = null) }
    }

    fun logout() {
        pollJob?.cancel()
        tokenStore.clear()
        _auth.update {
            it.copy(loggedIn = false, login = null, method = null, deviceCode = null, message = "Sesion cerrada")
        }
    }

    fun contribute() {
        if (!tokenStore.isLoggedIn) {
            _auth.update { it.copy(message = "Inicia sesion con GitHub primero.") }
            return
        }
        val locals = spamNumbers.value.filter { it.source == "local" }
        if (locals.isEmpty()) {
            _auth.update { it.copy(message = "No tienes numeros propios reportados para aportar.") }
            return
        }
        _auth.update { it.copy(busy = true, message = null) }
        viewModelScope.launch {
            val message = withContext(Dispatchers.IO) {
                try {
                    val url = GitHubContributor.proposeNumbers(
                        token = tokenStore.token!!,
                        owner = settings.contribOwner,
                        repo = settings.contribRepo,
                        numbers = locals
                    )
                    "Aporte enviado (Issue): $url"
                } catch (e: Exception) {
                    "Error al aportar: ${e.message ?: "desconocido"}"
                }
            }
            _auth.update { it.copy(busy = false, message = message) }
        }
    }

    fun consumeAuthMessage() = _auth.update { it.copy(message = null) }
}
