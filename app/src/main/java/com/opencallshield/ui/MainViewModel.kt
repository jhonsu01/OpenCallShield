package com.opencallshield.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opencallshield.data.AppDatabase
import com.opencallshield.data.BlockedCall
import com.opencallshield.data.SettingsStore
import com.opencallshield.data.SpamNumber
import com.opencallshield.data.SpamRepository
import com.opencallshield.sync.GitHubSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsStore(app)
    private val repo = SpamRepository(AppDatabase.get(app).spamDao(), settings)

    val spamNumbers: StateFlow<List<SpamNumber>> =
        repo.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val blockedCalls: StateFlow<List<BlockedCall>> =
        repo.observeBlocked()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _state = MutableStateFlow(currentSettings())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private fun currentSettings() = SettingsUiState(
        blockUnknown = settings.blockUnknown,
        blockPrefixes = settings.blockPrefixes,
        silence = settings.silenceInsteadOfReject,
        prefixes = settings.prefixList,
        syncUrl = settings.syncUrl
    )

    fun report(number: String) = viewModelScope.launch {
        if (number.isNotBlank()) repo.report(number)
    }

    fun remove(item: SpamNumber) = viewModelScope.launch {
        repo.remove(item.number)
    }

    fun clearHistory() = viewModelScope.launch {
        repo.clearHistory()
    }

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

    fun consumeMessage() {
        _state.update { it.copy(message = null) }
    }
}
