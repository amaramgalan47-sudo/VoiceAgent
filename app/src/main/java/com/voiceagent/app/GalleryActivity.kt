package com.voiceagent.app

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.BitmapFactory
import java.io.File

class VaultActivity : AppCompatActivity() {

    companion object {
        private const val CORRECT_PIN = "1234"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPinScreen()
    }

    private fun showPinScreen() {
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        val title = TextView(this).apply {
            text = "PIN код оруулна уу"
            textSize = 20f
        }

        val pinInput = EditText(this).apply {
            hint = "4 оронтой PIN"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }

        val confirmButton = Button(this).apply {
            text = "Нээх"
            setOnClickListener {
                if (pinInput.text.toString() == CORRECT_PIN) {
                    showVaultImages()
                } else {
                    Toast.makeText(this@VaultActivity, "PIN буруу байна", Toast.LENGTH_SHORT).show()
                }
            }
        }

        rootLayout.addView(title)
        rootLayout.addView(pinInput)
        rootLayout.addView(confirmButton)
        setContentView(rootLayout)
    }

    private fun showVaultImages() {
        val scrollView = ScrollView(this)
        val imagesLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val vaultDir = File(filesDir, "vault")
        val files = vaultDir.listFiles()

        if (files == null || files.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "Vault хоосон байна"
            }
            imagesLayout.addView(emptyText)
        } else {
            for (file in files) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (bitmap != null) {
                    val itemLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(0, 0, 0, 32)
                    }

                    val imageView = ImageView(this).apply {
                        setImageBitmap(bitmap)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600
                        )
                    }

                    val buttonRow = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }

                    val restoreButton = Button(this).apply {
                        text = "↩️ Gallery руу буцаах"
                        setOnClickListener {
                            restoreToGallery(file)
                        }
                    }

                    val deleteButton = Button(this).apply {
                        text = "🗑️ Устгах"
                        setOnClickListener {
                            deleteFromVault(file)
                        }
                    }

                    buttonRow.addView(restoreButton)
                    buttonRow.addView(deleteButton)

                    itemLayout.addView(imageView)
                    itemLayout.addView(buttonRow)
                    imagesLayout.addView(itemLayout)
                }
            }
        }

        scrollView.addView(imagesLayout)
        setContentView(scrollView)
    }

    private fun restoreToGallery(file: File) {
        try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/VoiceAgentRestored")
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
                }
                Toast.makeText(this, "Gallery руу буцаагдлаа", Toast.LENGTH_SHORT).show()
                file.delete()
                showVaultImages()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Алдаа: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFromVault(file: File) {
        if (file.delete()) {
            Toast.makeText(this, "Устгагдлаа", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Устгаж чадсангүй", Toast.LENGTH_SHORT).show()
        }
        showVaultImages()
    }
}
