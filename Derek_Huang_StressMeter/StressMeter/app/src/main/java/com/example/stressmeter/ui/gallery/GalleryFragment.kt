package com.example.stressmeter.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.stressmeter.MainActivity
import com.example.stressmeter.databinding.FragmentGalleryBinding
import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //stop notification when opening this fragment
        (activity as MainActivity).stopNotification()

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ListAdapter(readFromCsv())
        displayLineChart()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readFromCsv(): List<Pair<Long, Int?>> {
        val csvFileName = "stress_timestamp.csv"
        val file = File(requireContext().filesDir, csvFileName)

        if (!file.exists()) {
            // File does not exist, return an empty list or handle it as appropriate.
            return emptyList()
        }

        val data: MutableList<Pair<Long, Int?>> = mutableListOf()

        try {
            val reader = CSVReader(FileReader(file))
            var nextLine: Array<String>?

            while (true) {
                nextLine = reader.readNext()
                if (nextLine == null) {
                    break
                }

                if (nextLine.size >= 2) {
                    val timestamp = nextLine[0].toLong()
                    val score = nextLine[1].toIntOrNull()
                    data.add(Pair(timestamp, score))

                    Log.d("CSV DATA", "Timestamp: $timestamp, Score: $score")
                }
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("CSV ERROR", "COULDN'T PARSE CSV")
        }
        return data
    }

    private fun displayLineChart() {
        val lineChart = AnyChart.line()
        val dataSet = ArrayList<DataEntry>()

        val data = readFromCsv()

        //Set values
        val stressScores = data.mapNotNull { it.second }
        val instances = (0 until stressScores.size).map { it.toDouble() }.toList()

        // Add data points to data set
        for (i in instances.indices) {
            dataSet.add(ValueDataEntry(instances[i], stressScores[i]))
        }
        if (dataSet.isEmpty()) {
            lineChart.noData("No data available")
        } else{
            // Set the data
            lineChart.data(dataSet)
        }


        lineChart.title("A graph showing your stress levels")
        lineChart.xAxis(0).title("Instances")
        lineChart.yAxis(0).title("Stress Score")

        _binding?.lineChart?.setChart(lineChart)
    }


}