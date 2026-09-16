package com.voiceagent.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {

    private var selectedImageUris: List<Uri> = emptyList()
    private lateinit var statusText: TextView

    private val pickImagesLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImageUris = uris
            statusText.text = "${uris.size} зураг сонгогдлоо. Одоо нуух боломжтой."
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

        statusText = TextView(this).apply {
            text = "Одоогоор зураг сонгоогүй байна"
            textSize = 14f
        }

        val pickButton = Button(this).apply {
            text = "📷 Зураг сонгох (олноор)"
            setOnClickListener {
                pickImagesLauncher.launch("image/*")
            }
        }

        val hideButton = Button(this).apply {
            text = "🔒 Сонгосон зургуудыг нуух"
            setOnClickListener {
                hideSelectedImages()
            }
        }

        val viewVaultButton = Button(this).apply {
            text = "🗂️ Нуусан зургууд харах"
            setOnClickListener {
                startActivity(Intent(this@GalleryActivity, VaultActivity::class.java))
            }
        }

        layout.addView(title)
        layout.addView(statusText)
        layout.addView(pickButton)
        layout.addView(hideButton)
        layout.addView(viewVaultButton)
        setContentView(layout)
    }

    private fun hideSelectedImages() {
        if (selectedImageUris.isEmpty()) {
            Toast.makeText(this, "Эхлээд зураг сонгоно уу", Toast.LENGTH_SHORT).show()
            return
        }

        val vaultDir = java.io.File(filesDir, "vault")
        if (!vaultDir.exists()) vaultDir.mkdirs()

        var successCount = 0

        for (uri in selectedImageUris) {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val fileName = "img_${System.currentTimeMillis()}_${successCount}.jpg"
                val outFile = java.io.File(vaultDir, fileName)
                inputStream?.use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                successCount++
            } catch (e: Exception) {
                // Тухайн зурган дээр алдаа гарвал дараагийнхыг үргэлжлүүлнэ
            }
        }

        Toast.makeText(this, "$successCount зураг нуугдлаа!", Toast.LENGTH_SHORT).show()
        selectedImageUris = emptyList()
        statusText.text = "Одоогоор зураг сонгоогүй байна"
    }
}
