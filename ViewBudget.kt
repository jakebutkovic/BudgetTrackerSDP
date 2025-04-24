package com.budget.budget

import java.io.File
fun main() {
    val filePath = "C:\\Users\\jakeb\\AndroidStudioProjects\\BudgetTrackerSDP33\\app\\src\\main\\java\\com\\budget\\budget\\ui\\theme\\budget.txt"
    val file = File(filePath)

    try {
        val fileContent = file.readText()
        println(fileContent)
    } catch(e: Exception){
        println("Error reading file: ${e.message}")
    }
}