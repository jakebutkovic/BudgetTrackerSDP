package com.budget.budget

data class PurchaseData(
    val date: String,
    val vendor: String,
    val amount: Double,
    val category: String,
    val paymentType: String
)
