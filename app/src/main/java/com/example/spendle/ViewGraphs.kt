package com.example.spendle

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.io.BufferedReader
import java.io.InputStreamReader

class ViewGraphs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_graphs)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val purchases = loadFilePurchases()

        // Set up Bar Chart (grouped by category)
        val barChart = findViewById<BarChart>(R.id.barChart)
        val (barEntries, barLabels) = prepareBarChartData(purchases)
        val barDataSet = BarDataSet(barEntries, "Spending by Category")
        barDataSet.colors = getColorList()
        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(barLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f
        barChart.invalidate()

        // Set up Pie Chart (grouped by category)
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val pieEntries = preparePieChartData(purchases)
        val pieDataSet = PieDataSet(pieEntries, "Spending by Category")
        pieDataSet.colors = getColorList()
        pieDataSet.setDrawValues(false) // Remove value labels from slices
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.setUsePercentValues(false)
        pieChart.setEntryLabelColor(Color.TRANSPARENT) // Hide labels on chart
        pieChart.description.isEnabled = false
        pieChart.legend.isWordWrapEnabled = true
        pieChart.invalidate()
    }

    private fun loadFilePurchases(): List<PurchaseData> {
        val purchases = mutableListOf<PurchaseData>()
        try {
            val inputStream = assets.open("data.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines ->
                lines.forEach { line ->
                    val trimmedLine = line.trim().removePrefix("[").removeSuffix("]")
                    val tokens = trimmedLine.split(",")
                    if (tokens.size == 5) {
                        val purchase = PurchaseData(
                            date = tokens[0].trim(),
                            vendor = tokens[1].trim(),
                            amount = tokens[2].trim().toDoubleOrNull() ?: 0.0,
                            category = tokens[3].trim(),
                            paymentType = tokens[4].trim()
                        )
                        purchases.add(purchase)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Error loading 'data.txt': ${e.message}")
        }
        return purchases
    }

    private fun prepareBarChartData(purchases: List<PurchaseData>): Pair<List<BarEntry>, List<String>> {
        val categoryTotals = purchases.groupBy { it.category }
            .mapValues { it.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        for ((index, pair) in categoryTotals.withIndex()) {
            entries.add(BarEntry(index.toFloat(), pair.second.toFloat()))
            labels.add(pair.first)
        }
        return Pair(entries, labels)
    }

    private fun preparePieChartData(purchases: List<PurchaseData>): List<PieEntry> {
        val categoryTotals = purchases.groupBy { it.category }
            .mapValues { it.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }

        return categoryTotals.map { PieEntry(it.second.toFloat(), it.first) }
    }

    private fun getColorList(): List<Int> {
        return listOf(
            Color.parseColor("#FF6384"), // red-pink
            Color.parseColor("#36A2EB"), // blue
            Color.parseColor("#FFCE56"), // yellow
            Color.parseColor("#4BC0C0"), // teal
            Color.parseColor("#9966FF"), // purple
            Color.parseColor("#FF9F40"), // orange
            Color.parseColor("#C9CBCF"), // grey
            Color.parseColor("#8B0000"), // dark red
            Color.parseColor("#00FA9A"), // medium spring green
            Color.parseColor("#DAA520")  // goldenrod
        )
    }
}