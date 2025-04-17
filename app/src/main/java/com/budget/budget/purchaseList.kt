package com.example.budgetappalpha

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
import com.example.budgetappalpha.ui.theme.BudgetAppAlphaTheme

import android.widget.Button
import android.view.View.OnClickListener
import android.content.Intent
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class PurchaseList : ComponentActivity() {
    data class Purchase(var dateM: Int, var dateD: Int, var dateY: Int, var name: String, var amount: Float, var category: String, var transaction: String)

    fun loadPurchases(): MutableList<Purchase> {
        val purchases = mutableListOf<Purchase>()
        try {
            val inputStream = assets.open("data.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines ->
                lines.forEach { line ->
                    val trimmedLine = line.trim().removePrefix("[").removeSuffix("]")
                    val tokens = trimmedLine.split(",")
                    if (tokens.size == 5) {
                        purchases.add(convertToPurchase(tokens))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Cannot Load 'data.txt': ${e.message}")
        }
        return purchases
    }

    fun convertToPurchase(data: List<String>): Purchase {
        var purchase = Purchase(0, 0, 0, "", ("0").toFloat(), "", "")

        // Date
        var firstSlash = data[0].indexOf('/')
        purchase.dateM = data[0].substring(0, firstSlash).toInt()
        val secondSlash = data[0].indexOf('/', firstSlash)
        purchase.dateD = data[0].substring(firstSlash + 1, secondSlash).toInt()
        purchase.dateY = data[0].substring(secondSlash, data[0].length).toInt()

        purchase.name = data[1]
        purchase.amount = data[2].toFloat()
        purchase.category = data[3]
        purchase.transaction = data[4]

        return purchase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_list)

        val purchaseTable = findViewById<TableLayout>(R.id.purchaseTable)
        val homeButton = findViewById<Button>(R.id.btn_listReturn)

        // Create Purchase List
        var purchaseList: MutableList<Purchase> = loadPurchases()
        var rowIndex = 2

        // Add Each Purchase to List
        for (purchase in purchaseList) {

            var row: TableRow = TableRow(this)
            row.setLayoutParams(
                TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )

            var dateText: TextView = TextView(this)
            dateText.id = (rowIndex * 10)
            dateText.text = purchase.dateM.toString() + "/" + purchase.dateD.toString() + "/" + purchase.dateY.toString()
            dateText.setPadding(3, 3, 3, 3)
            row.addView(dateText)

            var nameText: TextView = TextView(this)
            nameText.id = (rowIndex * 10) + 1
            nameText.text = purchase.name
            nameText.setPadding(3, 3, 3, 3)
            row.addView(nameText)

            purchaseTable.addView(row, TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            ))
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
