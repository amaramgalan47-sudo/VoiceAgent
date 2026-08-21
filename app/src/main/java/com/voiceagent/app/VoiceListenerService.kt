package com.voiceagent.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class VoiceListenerService : Service() {

    companion object {
        private const val TAG = "VoiceListenerService"
        private const val CHANNEL_ID = "voice_agent_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, buildNotification())
        Log.d(TAG, "Voice Agent service started")
    }

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Voice Agent", NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(NotificationManager::class.java)).createNotificationChannel(channel)
        }
        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Voice Agent идэвхтэй")
            .setContentText("Таны дуу хоолойг сонсож байна")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
