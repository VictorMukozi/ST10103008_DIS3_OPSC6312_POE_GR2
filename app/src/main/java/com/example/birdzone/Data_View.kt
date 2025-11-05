package com.example.birdzone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Data_View : AppCompatActivity() {



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        val BirdName :EditText
        val BirdNotes:EditText

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        BirdName = findViewById(R.id.BirdName)
        BirdNotes = findViewById(R.id.BirdNotes)

       var deta = findViewById<TextView>(R.id.DataView)


        val detail ="Bird Name: "+ BirdName +"\nBird Notes" +BirdNotes

        deta.text = detail


        findViewById<Button>(R.id.Back).setOnClickListener() {

            val intent = Intent(this, Observation::class.java)
            startActivity(intent)
            finish()

        }

    }
}