package br.com.agendou.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

object ReminderScheduler {
    fun schedule(context: Context, startDateTime: LocalDateTime, title: String, message: String) {
        val triggerTimeMillis = startDateTime.minusHours(1)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val delay = triggerTimeMillis - System.currentTimeMillis()
        if (delay <= 0) return

        val data = Data.Builder()
            .putString(ReminderWorker.KEY_TITLE, title)
            .putString(ReminderWorker.KEY_MESSAGE, message)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(Duration.ofMillis(delay))
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
} 