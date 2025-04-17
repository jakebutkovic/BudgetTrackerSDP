package com.example.budgetappalpha

import android.os.Bundle
import androidx.activity.ComponentActivity

import android.widget.Button
import android.content.Intent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val logPurchaseButton = findViewById<Button>(R.id.btn_logPurchaseScreen)
        val purchaseListButton = findViewById<Button>(R.id.btn_purchaseListScreen)

        logPurchaseButton.setOnClickListener {
            val intent = Intent(this, LogPurchase::class.java)
            startActivity(intent)
        }

        purchaseListButton.setOnClickListener {
            val intent = Intent(this, PurchaseList::class.java)
            startActivity(intent)
        }
    }
}
