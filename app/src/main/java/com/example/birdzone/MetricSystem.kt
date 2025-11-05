package com.example.birdzone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.birdzone.R.id.MetricBtn

class MetricSystem : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metric_system)

        findViewById<Button>(R.id.MetricBtn).setOnClickListener() {
            val intent = Intent(this,WelcomePage::class.java)
            startActivity(intent)
            finish()

        }
    }
}