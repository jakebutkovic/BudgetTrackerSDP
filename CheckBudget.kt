package com.example.myapplication
import java.io.File

fun main() {
    val fileName = "budget.txt"
    val file = File(fileName)

    if (file.exists()) {
        val fileContents = file.readText()
        println(fileContents)
    } else {
        println("File not found.")
    }
}