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


val filename = "data.txt"
val file = File(filename)

class MainActivity : ComponentActivity() {
    data class Purchase(var dateM: Int, var dateD: Int, var dateY: Int, var name: String, var amount: Float, var category: String, var transaction: String)

    fun logPurchase(date: String, name: String, amount: Float, category: String, transaction: String, file: File) {
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
            pur.dateD = date.substring(dateSlash, date.indexOf('/',dateSlash)).toInt()
            dateSlash = date.indexOf('/', dateSlash) + 1
            pur.dateY = date.substring(dateSlash, date.length).toInt()
            var nextItem = comma + 1

            // Name
            comma = index.indexOf(',',nextItem)
            pur.name = index.substring(nextItem,comma)
            nextItem = comma + 1

            // Amount
            comma = index.indexOf(',',nextItem)
            pur.amount = index.substring(nextItem,comma).toFloat()
            nextItem = comma + 1

            // Category & Transaction
            comma = index.indexOf(',',nextItem)
            pur.category = index.substring(nextItem,comma)
            pur.transaction = index.substring(comma+1,index.indexOf(']',comma+1))

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
        setContentView(R.layout.activity_log_purchase)

        val monthText = findViewById<EditText>(R.id.monthText)
        val dayText = findViewById<EditText>(R.id.dayText)
        val yearText = findViewById<EditText>(R.id.yearText)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var date = Date()
        var current = formatter.format(date)

        var firstDash = current.indexOf("-")
        var month = current.substring(0, firstDash)
        var day = current.substring(firstDash+1, current.indexOf("-", firstDash+1))
        var year = current.substring(current.indexOf("-", firstDash+1), current.length)
        monthText.setText(month)
        dayText.setText(day)
        yearText.setText(year)
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
