package com.budget.budget

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.budget.budget.ui.theme.BudgetTheme
import java.io.File

val filename = "Data.txt"
val file = File(filename)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
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
}

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
