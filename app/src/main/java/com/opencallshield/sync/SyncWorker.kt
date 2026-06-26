package com.opencallshield.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.opencallshield.data.AppDatabase
import com.opencallshield.data.SettingsStore
import com.opencallshield.data.SpamRepository

/**
 * Tarea periodica (cada 24h) que descarga la base colaborativa y la mezcla
 * con la base local. Gestionada por WorkManager.
 */
class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val settings = SettingsStore(applicationContext)
            val remote = GitHubSync.fetch(settings.syncUrl)
            val repo = SpamRepository(
                AppDatabase.get(applicationContext).spamDao(),
                settings
            )
            repo.mergeRemote(remote)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
