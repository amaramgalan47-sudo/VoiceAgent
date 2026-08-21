package com.voiceagent.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            Toast.makeText(this, "Зураг сонгогдлоо. Одоо нуух боломжтой.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        val title = TextView(this).apply {
            text = "Gallery Vault"
            textSize = 20f
        }

        val pickButton = Button(this).apply {
            text = "📷 Зураг сонгох"
            setOnClickListener {
                pickImageLauncher.launch("image/*")
            }
        }

        val hideButton = Button(this).apply {
            text = "🔒 Сонгосон зургийг нуух"
            setOnClickListener {
                hideSelectedImage()
            }
        }

        val viewVaultButton = Button(this).apply {
            text = "🗂️ Нуусан зургууд харах"
            setOnClickListener {
                startActivity(Intent(this@GalleryActivity, VaultActivity::class.java))
            }
        }

        layout.addView(title)
        layout.addView(pickButton)
        layout.addView(hideButton)
        layout.addView(viewVaultButton)
        setContentView(layout)
    }

    private fun hideSelectedImage() {
        val uri = selectedImageUri
        if (uri == null) {
            Toast.makeText(this, "Эхлээд зураг сонгоно уу", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val inputStream = contentResolver.openInputStream(uri)
            val vaultDir = java.io.File(filesDir, "vault")
            if (!vaultDir.exists()) vaultDir.mkdirs()

            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val outFile = java.io.File(vaultDir, fileName)
            inputStream?.use { input ->
                outFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            Toast.makeText(this, "Зураг нуугдлаа! Vault-т хадгалагдлаа.", Toast.LENGTH_SHORT).show()
            selectedImageUri = null

        } catch (e: Exception) {
            Toast.makeText(this, "Алдаа гарлаа: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
