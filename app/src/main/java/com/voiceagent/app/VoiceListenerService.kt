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
        // TODO: Дараагийн шатан
