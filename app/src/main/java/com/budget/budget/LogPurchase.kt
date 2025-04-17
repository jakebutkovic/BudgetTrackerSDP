package com.example.budgetappalpha

import android.R.attr.data
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.Date
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.content.Intent

class LogPurchase : ComponentActivity() {
    fun logPurchase(m: String, d: String, y: String, name: String, price: Float, category: String, transaction: String) {
        val purchase = "[$m/$d/$y,$name,$price,$category,$transaction]\n"

        // Open Data File

        var file = File(filesDir.absolutePath + "/data.txt")

        // Write to Data File
        try {
            file.writeText("Test")
        } catch (e: Exception) {
            println("Error writing file: ${e.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_purchase)

        val returnButton = findViewById<Button>(R.id.btn_logReturn)
        val confirmButton = findViewById<Button>(R.id.btn_confirmPurchase)

        val monthText = findViewById<EditText>(R.id.monthText)
        val dayText = findViewById<EditText>(R.id.dayText)
        val yearText = findViewById<EditText>(R.id.yearText)

        val nameText = findViewById<EditText>(R.id.nameText)
        val priceText = findViewById<EditText>(R.id.priceText)

        val formatter = SimpleDateFormat("MM-dd-yyyy")
        var date = Date()
        var current = formatter.format(date)

        var firstDash = current.indexOf("-")
        var month = current.substring(0, firstDash).toInt()
        var day = current.substring(firstDash+1, current.indexOf("-", firstDash+1)).toInt()
        var year = current.substring(current.indexOf("-", firstDash+1)+1, current.length).toInt()
        monthText.setText(month.toString())
        dayText.setText(day.toString())
        yearText.setText(year.toString())

        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.PurchaseCategory,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = adapter
        }

        val transactionSpinner = findViewById<Spinner>(R.id.transactionSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.TransactionType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            transactionSpinner.adapter = adapter
        }

        confirmButton.setOnClickListener {
            var category = categorySpinner.selectedItem.toString()
            var transaction = transactionSpinner.selectedItem.toString()

            var m: String
            if (monthText.text.toString() == "") m = ""
            else m = monthText.text.toString()
            var d: String
            if (dayText.text.toString() == "") d = ""
            else d = dayText.text.toString()
            var y: String
            if (yearText.text.toString() == "") y = ""
            else y = yearText.text.toString()

            var name: String
            if (nameText.text.toString() == "") name = ""
            else name = nameText.text.toString()
            var price: Float
            if (priceText.text.toString() == "") price = 0.0f
            else price = priceText.text.toString().toFloat()

            logPurchase(m, d, y, name, price, category, transaction)
        }

        returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
