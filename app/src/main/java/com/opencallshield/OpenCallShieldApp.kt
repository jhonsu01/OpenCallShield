package com.opencallshield

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.opencallshield.sync.SyncWorker
import java.util.concurrent.TimeUnit

class OpenCallShieldApp : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleDailySync()
    }

    private fun scheduleDailySync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ocs_daily_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
