package com.example.spendle

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
    data class Purchase(var date: String, var name: String, var price: String, var category: String, var transaction: String)

    fun loadPurchases(): MutableList<Purchase> {
        val purchases = mutableListOf<Purchase>()
        var file = File(filesDir.absolutePath + "/purchaseData.txt")
        if (file.exists()) {
            file.forEachLine {
                var line = it.trim().removePrefix("[").removeSuffix("]")
                var purchase = convertToPurchase(line)
                purchases.add(purchase)
            }
        }
        else {
            Log.e("ERROR", "Cannot Load purchaseData.txt")
        }

        return purchases
    }

    fun convertToPurchase(data: String): Purchase {
        var purchase = Purchase("", "", "", "", "")

        // Conversion
        purchase.date = data.substring(0, data.indexOf(','))
        var comma = data.indexOf(',') + 1
        purchase.name = data.substring(comma, data.indexOf(',', comma))
        comma = data.indexOf(',', comma) + 1
        purchase.price = data.substring(comma, data.indexOf(',', comma))
        comma = data.indexOf(',', comma) + 1
        purchase.category = data.substring(comma, data.indexOf(',', comma))
        comma = data.indexOf(',', comma) + 1
        purchase.transaction = data.substring(comma, data.length)

        return purchase
    }

    fun constructTable(purchases: MutableList<Purchase>) {
        val purchaseTable = findViewById<TableLayout>(R.id.purchaseTable)
        Log.d("Task", "Load Table")

        // Parameters
        val rowParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
        val child1 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
        child1.weight = 0.5f
        val child2 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
        child2.weight = 0.4f
        val child3 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
        child3.weight = 0.3f
        val child4 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
        child4.weight = 0.2f
        val child5 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
        child5.weight = 0.1f
        Log.d("Task", "Parameters Set")

        var rowIndex = 0
        for (purchase in purchases) {
            var row = TableRow(this)
            row.layoutParams = rowParams

            var date = TextView(this)
            date.text = purchase.date
            date.setTextColor(resources.getColor(R.color.black))
            date.layoutParams = child1

            var name = TextView(this)
            name.text = purchase.name
            name.setTextColor(resources.getColor(R.color.black))
            name.layoutParams = child2

            var price = TextView(this)
            val translatedPrice = "$" + purchase.price
            price.text = translatedPrice
            price.setTextColor(resources.getColor(R.color.black))
            price.layoutParams = child3

            var category = TextView(this)
            category.text = purchase.category
            category.setTextColor(resources.getColor(R.color.black))
            category.layoutParams = child4

            var transaction = TextView(this)
            transaction.text = purchase.transaction
            transaction.setTextColor(resources.getColor(R.color.black))
            transaction.layoutParams = child5

            row.addView(date)
            row.addView(name)
            row.addView(price)
            row.addView(category)
            row.addView(transaction)
            row.weightSum = 1.5f

            purchaseTable.addView(row, rowIndex)
            rowIndex++
        }

        // Header Row
        var headerRow = TableRow(this)
        headerRow.layoutParams = rowParams
        Log.d("Task", "Header Row")

        var col1 = TextView(this)
        col1.text = "Date"
        col1.setTextColor(resources.getColor(R.color.black))
        col1.layoutParams = child1
        child1.setMargins(2, 20, 2, 0)

        var col2 = TextView(this)
        col2.text = "Name"
        col2.setTextColor(resources.getColor(R.color.black))
        col2.layoutParams = child2
        child2.setMargins(2, 20, 2, 0)

        var col3 = TextView(this)
        col3.text = "Price"
        col3.setTextColor(resources.getColor(R.color.black))
        col3.layoutParams = child3
        child3.setMargins(2, 20, 2, 0)

        var col4 = TextView(this)
        col4.text = "Category"
        col4.setTextColor(resources.getColor(R.color.black))
        col4.layoutParams = child4
        child4.setMargins(2, 20, 2, 0)

        var col5 = TextView(this)
        col5.text = "Transaction"
        col5.setTextColor(resources.getColor(R.color.black))
        col5.layoutParams = child5
        child5.setMargins(2, 20, 2, 0)
        Log.d("Task", "Columns Created")

        headerRow.addView(col1)
        headerRow.addView(col2)
        headerRow.addView(col3)
        headerRow.addView(col4)
        headerRow.addView(col5)
        headerRow.weightSum = 1.5f
        Log.d("Task", "Header Rows Filled")

        purchaseTable.addView(headerRow, 0)
        Log.d("Task", "Row 1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_list)

        val homeButton = findViewById<Button>(R.id.btn_listReturn)

        // Create Purchase List
        var purchaseList: MutableList<Purchase> = loadPurchases()

        constructTable(purchaseList)

        homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}