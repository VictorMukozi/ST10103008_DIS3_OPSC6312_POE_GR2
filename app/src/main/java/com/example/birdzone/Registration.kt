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

class Registration : AppCompatActivity() {
    lateinit var pass:EditText;
    lateinit var cnfpass:EditText;
    lateinit var reg:Button
    lateinit var user:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        reg = findViewById(R.id.RegButton)
        user = findViewById(R.id.RegUsername)
        pass = findViewById(R.id.RegPassword);
        cnfpass = findViewById(R.id.RegCnfPassword);

        reg.setOnClickListener() {
            when {
                TextUtils.isEmpty(user.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@Registration, "Please enter username", Toast.LENGTH_LONG).show()
                    user.text.clear()
                }
                TextUtils.isEmpty(pass.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(this@Registration, "Please enter password", Toast.LENGTH_LONG).show()
                    pass.text.clear()
                }

                else ->{
                    val usernames:String =user.text.toString().trim{ it <= ' ' }
                    val passwords:String =pass.text.toString().trim{ it <= ' ' }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(usernames ,passwords)
                        .addOnCompleteListener(
                            {task ->
                                if(task.isSuccessful){
                                    val firebaseUser:FirebaseUser =task.result!!.user!!
                                    Toast.makeText(this@Registration ,"You have successfully registered",Toast.LENGTH_LONG).show()
                                    val intent =Intent(this@Registration ,MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id" ,firebaseUser.uid)
                                    intent.putExtra("username_id" ,usernames)
                                    intent.putExtra("password_id",passwords)
                                    startActivity(intent)
                                    finish()
                                }else
                                {Toast.makeText(this@Registration ,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()}

                            }

                        )
                }
            }
        }
    }
}