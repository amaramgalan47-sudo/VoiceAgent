package com.voiceagent.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.BitmapFactory

class VaultActivity : AppCompatActivity() {

    companion object {
        private const val CORRECT_PIN = "1234" // TODO: дараа нь хэрэглэгч өөрөө PIN тохируулдаг болгоно
    }

    private lateinit var rootLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPinScreen()
    }

    private fun showPinScreen() {
        rootLayout = LinearLayout(this).apply {
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

        val vaultDir = java.io.File(filesDir, "vault")
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
                    val imageView = ImageView(this).apply {
                        setImageBitmap(bitmap)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600
                        )
                        setPadding(0, 0, 0, 24)
                    }
                    imagesLayout.addView(imageView)
                }
            }
        }

        scrollView.addView(imagesLayout)
        setContentView(scrollView)
    }
}
