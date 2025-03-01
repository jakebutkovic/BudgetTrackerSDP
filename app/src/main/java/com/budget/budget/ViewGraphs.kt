package com.budget.budget

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ViewGraphs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_graphs)

        // Adjust for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load purchase data from file
        val purchases = loadPurchases()

        // Set up Bar Chart
        val barChart = findViewById<BarChart>(R.id.barChart)
        val (barEntries, dateLabels) = prepareBarChartData(purchases)
        val barDataSet = BarDataSet(barEntries, "Spending by Date")
        val barData = BarData(barDataSet)
        barChart.data = barData

        // Format X-axis with date labels
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dateLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        barChart.invalidate() // refresh the chart

        // Set up Pie Chart
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val pieEntries = preparePieChartData(purchases)
        val pieDataSet = PieDataSet(pieEntries, "Spending by Category")
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate() // refresh the chart
    }

    // Function to load and parse purchase log data from assets
    private fun loadPurchases(): List<PurchaseData> {
        val purchases = mutableListOf<PurchaseData>()
        val inputStream = assets.open("purchase_log.txt")
        inputStream.bufferedReader().forEachLine { line ->
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
        return purchases
    }

    // Prepare Bar Chart entries (grouping by date)
    private fun prepareBarChartData(purchases: List<PurchaseData>): Pair<List<BarEntry>, List<String>> {
        val dateTotals = purchases.groupBy { it.date }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }
        val sortedDates = dateTotals.keys.sorted()
        val entries = ArrayList<BarEntry>()
        for ((index, date) in sortedDates.withIndex()) {
            val total = dateTotals[date] ?: 0.0
            entries.add(BarEntry(index.toFloat(), total.toFloat()))
        }
        return Pair(entries, sortedDates)
    }

    // Prepare Pie Chart entries (grouping by category)
    private fun preparePieChartData(purchases: List<PurchaseData>): List<PieEntry> {
        val categoryTotals = purchases.groupBy { it.category }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }
        val entries = ArrayList<PieEntry>()
        for ((category, total) in categoryTotals) {
            entries.add(PieEntry(total.toFloat(), category))
        }
        return entries
    }
}
