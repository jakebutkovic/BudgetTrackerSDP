package com.example.spendle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val logPurchaseButton = findViewById<Button>(R.id.btnLogPurchase)
        val purchaseListButton = findViewById<Button>(R.id.btnPurchaseList)
        val budgetButton = findViewById<Button>(R.id.btnBudget)
        val graphsButton = findViewById<Button>(R.id.btnViewGraphs)

        logPurchaseButton.setOnClickListener {
            startActivity(Intent(this, LogPurchase::class.java))
        }

        purchaseListButton.setOnClickListener {
            startActivity(Intent(this, PurchaseList::class.java))
        }

        budgetButton.setOnClickListener {
            startActivity(Intent(this, Budget::class.java))
        }

        graphsButton.setOnClickListener {
            //startActivity(Intent(this, ViewGraphs::class.java))
        }
    }
}