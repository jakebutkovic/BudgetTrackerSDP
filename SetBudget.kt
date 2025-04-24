package com.budget.budget.ui.theme

import java.io.File

fun saveBudget(budget: Double, label: String, filePath: String) {
    val file = File(filePath)

    // Read existing budgets
    val budgets = mutableMapOf<String, Double>()
    if (file.exists()) {
        file.readLines().forEach { line ->
            val data = line.split(":")
            if (data.size == 2) {
                val amount = data[1].toDoubleOrNull()
                if (amount != null) {
                    budgets[data[0]] = amount
                }
            }
        }
    }

    // Update or insert
    budgets[label] = budget

    // Write all budgets back to the file
    file.writeText(budgets.entries.joinToString("\n") { "${it.key}:${it.value}" })

    println("Budget '$label' saved successfully at: $filePath")
}

fun loadBudgets(filePath: String): Map<String, Double> {
    val file = File(filePath)
    if (!file.exists()) return emptyMap()

    return file.readLines().mapNotNull { line ->
        val data = line.split(":")
        if (data.size == 2) {
            val amount = data[1].toDoubleOrNull()
            if (amount != null) data[0] to amount else null
        } else null
    }.toMap()
}
