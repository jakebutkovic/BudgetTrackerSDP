package com.budget.budget

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
import com.budget.budget.ui.theme.BudgetTheme
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import android.widget.Button
import android.widget.TextView
import com.budget.budget.ui.theme.loadBudgets
import com.budget.budget.ui.theme.saveBudget
import android.widget.Toast


val filename = "data.txt"
val file = File(filename)

class MainActivity : ComponentActivity() {

    data class Purchase(
        var dateM: Int,
        var dateD: Int,
        var dateY: Int,
        var name: String,
        var amount: Float,
        var category: String,
        var transaction: String
    )

    fun logPurchase(
        date: String,
        name: String,
        amount: Float,
        category: String,
        transaction: String,
        file: File
    ) {
        var purchase = "[$date,$name,$amount,$category,$transaction]"
        file.appendText(purchase + "\n")
    }

    fun getPurchaseHistory(file: File): MutableList<Purchase> {
        var purchases = mutableListOf<Purchase>()
        var line = 0
        file.forEachLine { index ->
            var pur = Purchase(0, 0, 0, "", ("0").toFloat(), "", "")

            // Date
            var comma = index.indexOf(',')
            var date = index.substring(1, comma)
            pur.dateM = date.substring(0, date.indexOf('/')).toInt()
            var dateSlash = date.indexOf('/') + 1
            pur.dateD = date.substring(dateSlash, date.indexOf('/', dateSlash)).toInt()
            dateSlash = date.indexOf('/', dateSlash) + 1
            pur.dateY = date.substring(dateSlash, date.length).toInt()
            var nextItem = comma + 1

            // Name
            comma = index.indexOf(',', nextItem)
            pur.name = index.substring(nextItem, comma)
            nextItem = comma + 1

            // Amount
            comma = index.indexOf(',', nextItem)
            pur.amount = index.substring(nextItem, comma).toFloat()
            nextItem = comma + 1

            // Category & Transaction
            comma = index.indexOf(',', nextItem)
            pur.category = index.substring(nextItem, comma)
            pur.transaction = index.substring(comma + 1, index.indexOf(']', comma + 1))

            purchases.add(pur)
        }

        return purchases
    }

    fun readPurchases(purchases: MutableList<Purchase>) {
        for (purchase in purchases) {
            println(purchase)
        }
    }

    fun openLogSection(view: android.view.View) {
        //startActivity(Intent(this, LogPurchase::class.java))
    }

    fun openGraphs(view: android.view.View) {
        startActivity(Intent(this, ViewGraphs::class.java))
    }

    fun openTemplates(view: android.view.View) {
        //startActivity(Intent(this, TemplatesActivity::class.java))
    }

    // Running Code
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_purchases)

        val monthText = findViewById<EditText>(R.id.monthText)
        val dayText = findViewById<EditText>(R.id.dayText)
        val yearText = findViewById<EditText>(R.id.yearText)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)

        val firstDash = current.indexOf("-")
        val month = current.substring(0, firstDash)
        val day = current.substring(firstDash + 1, current.indexOf("-", firstDash + 1))
        val year = current.substring(current.indexOf("-", firstDash + 1) + 1, current.length)
        monthText.setText(month)
        dayText.setText(day)
        yearText.setText(year)

       // val buttonNext = findViewById<Button>(R.id.buttonNext) //sets up next button. when clicked starts ViewBudgetActivity
        //buttonNext.setOnClickListener {
         //   val intent = Intent(this, ViewBudgetActivity::class.java)
        //    startActivity(intent)
         //   overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
       // }

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

        // View Budgets. loads the saved bugets from the file and displays them in a TextView.
        //if not budgets are found, it sshows "No budgets saved."
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
            Toast.makeText(this, "$typeToDelete budget deleted successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No such budget type found to delete", Toast.LENGTH_SHORT).show()
        }
    }



    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        BudgetTheme {
            Greeting("Android")
        }
    }
}
