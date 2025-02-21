package com.example.myapplication

import java.io.File

fun main() {
    print("Enter your budget: ")
    var input = readLine()?.trim() // read user input as a string

    var budget = input?.toIntOrNull()
    if (input != null && input.isNotBlank()) {
        var filename = "budget.txt"
        var file = File(filename)

        try {
            file.writeText(budget.toString()) // write the user's input to the file
            println("Number stored successfully in ${file.absolutePath}")
        } catch (e: Exception) {
            println("Error writing file: ${e.message}")
        }

    }  else {
        println("Invalid input. Please enter a valid number.")
    }
}