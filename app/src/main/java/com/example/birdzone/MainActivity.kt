package com.example.birdzone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    lateinit var usernam:EditText;
    lateinit var passwor:EditText;
    lateinit var log:Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernam =findViewById(R.id.LogInname);
        passwor = findViewById(R.id.LogInPassword);
        log = findViewById(R.id.LogInButton)

        log.setOnClickListener() {
            when {
                TextUtils.isEmpty(usernam.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@MainActivity, "Please enter username", Toast.LENGTH_LONG).show()
                    usernam.text.clear()
                }
                TextUtils.isEmpty(passwor.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@MainActivity, "Please enter password", Toast.LENGTH_LONG).show()
                    passwor.text.clear()
                }

                else ->{
                    val usernames:String =usernam.text.toString().trim{ it <= ' ' }
                    val passwords:String =passwor.text.toString().trim{ it <= ' ' }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usernames ,passwords)
                        .addOnCompleteListener(
                            {task ->
                                if(task.isSuccessful){
                                    val firebaseUser:FirebaseUser =task.result!!.user!!
                                    Toast.makeText(this@MainActivity ,"You have successfully registered",Toast.LENGTH_LONG).show()
                                    val intent =Intent(this@MainActivity ,WelcomePage::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id" ,FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("username_id" ,usernames)
                                    intent.putExtra("password_id",passwords)
                                    startActivity(intent)
                                    finish()
                                }else
                                {Toast.makeText(this@MainActivity ,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()}

                            }

                        )
                }
            }
        }
        findViewById<Button>(R.id.LogInregister).setOnClickListener() {

            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()

        }
    }
}