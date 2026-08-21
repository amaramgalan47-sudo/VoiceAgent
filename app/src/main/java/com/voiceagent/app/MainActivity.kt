package com.voiceagent.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = mutableListOf(Manifest.permission.RECORD_AUDIO).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        val galleryButton = Button(this).apply {
            text = "🖼️ Gallery Vault"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
            }
        }

        layout.addView(galleryButton)
        setContentView(layout)

        if (hasAllPermissions()) {
            startVoiceListenerService()
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions, 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasAllPermissions()) {
            startVoiceListenerService()
        }
    }

    private fun hasAllPermissions(): Boolean = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startVoiceListenerService() {
        val intent = Intent(this, VoiceListenerService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
}
