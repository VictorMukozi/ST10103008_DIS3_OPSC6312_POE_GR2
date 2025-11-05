package com.example.birdzone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class WelcomePage : AppCompatActivity() {

    private  lateinit var kruger:ImageButton
    private lateinit var westCoast:ImageButton
    private  lateinit var Takwa: ImageButton
    private lateinit var Saint: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        kruger =findViewById(R.id.KrugerNationalPark)
        westCoast = findViewById(R.id.WestCoast)
        Takwa = findViewById(R.id.TakwaKaroo)
        Saint = findViewById(R.id.SaintLucia)


        kruger.setOnClickListener(){
            val Intent =Intent(Intent.ACTION_VIEW,Uri.parse("https://www.sanparks.org/parks/kruger/"))
            startActivity(Intent)
        }

        westCoast.setOnClickListener(){
            val Intent =Intent(Intent.ACTION_VIEW,Uri.parse("https://www.sanparks.org/parks/west_coast/"))
            startActivity(Intent)
        }

        Takwa.setOnClickListener(){
            val Intent =Intent(Intent.ACTION_VIEW,Uri.parse("https://www.sanparks.org/parks/tankwa/"))
            startActivity(Intent)
        }

        Saint.setOnClickListener(){
            val Intent =Intent(Intent.ACTION_VIEW,Uri.parse("https://stluciasouthafrica.com/birding-and-guided-walks/"))
            startActivity(Intent)
        }
        findViewById<Button>(R.id.MapBtn).setOnClickListener() {

                val intent = Intent(this, Mapping::class.java)
                startActivity(intent)
                finish()

            }

        findViewById<Button>(R.id.WelcomePageObservationButton).setOnClickListener() {

            val intent = Intent(this, Observation::class.java)
            startActivity(intent)
            finish()

        }

        findViewById<Button>(R.id.MetricSystemBtn).setOnClickListener() {

            val intent = Intent(this, MetricSystem::class.java)
            startActivity(intent)
            finish()

        }
    }
}