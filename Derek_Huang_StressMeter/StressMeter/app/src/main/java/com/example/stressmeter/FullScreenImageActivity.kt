package com.example.stressmeter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.ui.gallery.GalleryViewModel
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FullScreenImageActivity : AppCompatActivity() {

    private val positionToScoreMap = mapOf(
        0 to 6,
        1 to 8,
        2 to 14,
        3 to 16,
        4 to 5,
        5 to 7,
        6 to 13,
        7 to 15,
        8 to 2,
        9 to 4,
        10 to 10,
        11 to 12,
        12 to 1,
        13 to 3,
        14 to 9,
        15 to 11
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val imageResource = intent.getIntExtra("imageResource", 0)
        val imageScore = positionToScoreMap[intent.getIntExtra("imageScore", 0)]
        val imageView = findViewById<ImageView>(R.id.fullScreenImageView)
        val cancel = findViewById<Button>(R.id.cancelButton)
        val submit = findViewById<Button>(R.id.submitButton)

//        val testTextView = findViewById<TextView>(R.id.test)
//        testTextView.text = "score is: $imageScore"

        submit.setOnClickListener {
            val timestamp = System.currentTimeMillis()
            saveToCsv(timestamp, imageScore)
            //save data to csv
            finishAffinity()
        }

        //close app instead of cancel activity
        cancel.setOnClickListener {
            finishAffinity()
        }

        //set the image resource to the ImageView
        imageView.setImageResource(imageResource)
    }

    private fun saveToCsv(timestamp: Long, score: Int?) {
        val csvFileName = "stress_timestamp.csv"
        val file = File(filesDir, csvFileName)

        val record = arrayOf(timestamp.toString(), score.toString())

        if (!file.exists()) {
            file.createNewFile()
        }

        try {
            val writer = CSVWriter(FileWriter(file, true))
            writer.writeNext(record)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}