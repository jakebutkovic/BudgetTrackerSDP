package com.example.spendle

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Budget : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val buttonHome = findViewById<Button>(R.id.buttonHome)
        val editTextBudget = findViewById<EditText>(R.id.editTextBudget)
        val editTextType = findViewById<EditText>(R.id.editTextType)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)  // Add Delete button
        val budgetsTextView = findViewById<TextView>(R.id.budgetsTextView)

        // Save Budget. it retrieves the budget amt and type from the EditText fields,
        // checks if theyre not empty and the calls the saveBudget function to store the budget in a file
        buttonSave.setOnClickListener {
            val budgetText = editTextBudget.text.toString()
            val labelText = editTextType.text.toString()

            if (budgetText.isNotEmpty() && labelText.isNotEmpty()) {
                val amount = budgetText.toDouble()
                val filePath = filesDir.absolutePath + "/budget.txt"
                saveBudget(amount, labelText, filePath)
            }
        }

        // View Budgets. loads the saved budgets from the file and displays them in a TextView.
        //if not budgets are found, it shows "No budgets saved."
        val viewBudgetsButton = findViewById<Button>(R.id.viewBudgetsButton)
        val budgetFile = File(filesDir, "budget.txt")

        viewBudgetsButton.setOnClickListener {
            val savedBudgets = loadBudgets(budgetFile.absolutePath)

            budgetsTextView.text = if (savedBudgets.isEmpty()) {
                "No budgets saved."
            } else {
                val builder = StringBuilder()
                savedBudgets.forEach { (label, amount) ->
                    builder.append("$label: $amount\n")
                }
                builder.toString()
            }
        }

        // Delete Budget. Checks if the user has entered a valid budget type to delete.
        // if the type is valid, it calls the deleteBudget function. If not a toast message
        // saying "enter a valid budget type to delete" appears
        buttonDelete.setOnClickListener {
            val typeToDelete = editTextType.text.toString()

            if (typeToDelete.isNotEmpty()) {
                deleteBudget(typeToDelete, budgetFile.absolutePath)
            } else {
                Toast.makeText(this, "Enter a valid budget type to delete", Toast.LENGTH_SHORT).show()
            }
        }

        // Set-Up Home Button
        buttonHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // Function to save budget to file
    fun saveBudget(budget: Double, type: String, filePath: String) {
        val file = File(filePath)
        val budgets = mutableMapOf<String, Double>()

        if (file.exists()) {
            file.readLines().forEach { line ->
                val data = line.split(":")
                if (data.size >= 2) {
                    val amount = data[1].toDouble()
                    budgets[data[0]] = amount
                }
            }
        }

        // Add or update the budget
        budgets[type] = budget

        // Write all budgets back to the file
        file.writeText(budgets.entries.joinToString("\n") {
            "${it.key}:${it.value}"
        })

        Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_SHORT).show()
    }

    // Function to load budgets from the file
    fun loadBudgets(filePath: String): Map<String, Double> {
        val file = File(filePath)
        if (!file.exists()) return emptyMap()

        return file.readLines().mapNotNull { line ->
            val data = line.split(":")
            if (data.size >= 2) {
                val amount = data[1].toDouble()
                data[0] to amount
            } else null
        }.toMap()
    }

    // Function to delete a budget by type
    fun deleteBudget(typeToDelete: String, filePath: String) {
        val file = File(filePath)
        if (!file.exists()) return

        // Read existing budgets
        val budgets = mutableMapOf<String, Double>()
        file.readLines().forEach { line ->
            val data = line.split(":")
            if (data.size >= 2) {
                val amount = data[1].toDouble()
                budgets[data[0]] = amount
            }
        }

        // Remove the budget if it exists
        if (budgets.containsKey(typeToDelete)) {
            budgets.remove(typeToDelete)
            // Write updated budgets to the file
            file.writeText(budgets.entries.joinToString("\n") {
                "${it.key}:${it.value}"
            })
            Toast.makeText(this, "$typeToDelete budget deleted successfully!", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "No such budget type found to delete", Toast.LENGTH_SHORT).show()
        }
    }
}