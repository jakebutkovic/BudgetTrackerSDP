package com.budget.budget

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.io.File

class ViewBudgetActivity : ComponentActivity() {
    private val filename = "budget.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budget)

        val textViewBudget = findViewById<TextView>(R.id.textViewBudget)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Display budget file contents
        textViewBudget.text = readFileContents()

        // Go back to MainActivity when button is clicked
        buttonBack.setOnClickListener {
            finish()  // Closes ViewBudgetActivity and returns to MainActivity
        }
    }

    // Reads the contents of the budget file
    private fun readFileContents(): String {
        val file = File(filesDir, filename)
        return if (file.exists()) {
            file.readText()
        } else {
            "No budget data found."
        }
    }
}
