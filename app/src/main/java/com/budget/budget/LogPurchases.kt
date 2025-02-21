package com.budget.budget

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class LogPurchases : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_purchases)

        val editText = findViewById<EditText>(R.id.editTextBudget)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        saveButton.setOnClickListener {
            val input = editText.text.toString()

            if (input.isNotBlank()) {
                val filename = "budget.txt"
                val file = File(filesDir, filename) // Save to internal storage

                try {
                    file.writeText(input)
                    Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error saving file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
