package com.example.birdzone


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class Observation : AppCompatActivity() {
    private val cameraRequest = 1888
    lateinit var imageView: ImageView



    private lateinit var BirdName: EditText
    private lateinit var BirdNote: EditText

    private lateinit var  database: FirebaseDatabase
    private  lateinit var reference: DatabaseReference

    private lateinit var  Upload : Button
    private lateinit var  Add : Button
    private lateinit var  Save : Button
    private lateinit var imageURL: String
    private var uri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observation)


        //Initializingz565t5t5t656t656t
        database = FirebaseDatabase.getInstance()

        BirdName =findViewById(R.id.BirdName)
        BirdNote = findViewById(R.id.BirdNotes)
        Upload = findViewById(R.id.ObservationUpload)
        Add = findViewById(R.id.ObservationAddButton)

        reference = database.getReference().child("BirdZone Observation")


        findViewById<Button>(R.id.Back).setOnClickListener() {

            val intent = Intent(this, WelcomePage::class.java)
            startActivity(intent)
            finish()

        }

        title = "BirdZone"
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        imageView = findViewById(R.id.imageView3)
        val photoButton: Button = findViewById(R.id.observationPictureButton)
        photoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)

        }


        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data
                imageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        Upload.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        Add.setOnClickListener {
            saveData()
            val intent = Intent(this, Observation::class.java)
            startActivity(intent)
            finish()
            SaveFor()





        }

    }






    private fun saveData() {
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
            .child("BirdZone Observation")
            .child(uri?.lastPathSegment.toString())
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        //val view = LayoutInflater.from(this).inflate(R.layout.progress_layout, null, false)
      //  builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        uri?.let {
            storageReference.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    uriTask.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val urlImage = uriTask.result
                            imageURL = urlImage.toString()
                            uploadData()
                            dialog.dismiss()
                        } else {
                            // Handle the error
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the error
                    dialog.dismiss()
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun uploadData() {
        val birdName = BirdName.text.toString()
        val birdType = BirdNote.text.toString()


        val dataclass = BirdZone(birdName, birdType, imageURL)

        FirebaseDatabase.getInstance().getReference("BirdZone Observation").child(birdName)
            .setValue(dataclass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                }

    }
}





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
        }




    }
    private fun SaveFor() {
        val Bird_Name = BirdName.text.toString()
        val Bird_Notes = BirdNote.text.toString()


        if (Bird_Name.isEmpty()) {
            BirdName.error = "please enter the name of the Bird"
        }
        if (Bird_Notes.isEmpty()) {
            BirdNote.error = "please enter the Bird Notes"
        }


        val Obs_id = reference.get().isSuccessful.toString()

        val BirdZoneData = BirdZone(Obs_id, Bird_Name, Bird_Notes)

        reference.child(Obs_id).setValue(BirdZoneData).addOnCompleteListener()
        {
            Toast.makeText(this, "Data has been saved successfully", Toast.LENGTH_LONG).show()
        }.addOnFailureListener()
        {
            Toast.makeText(this, "error ", Toast.LENGTH_LONG).show()
        }

    }
}