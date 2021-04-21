package com.example.kotlinmessanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val Email: EditText =findViewById(R.id.Email_login_activity)
        val Password: EditText =findViewById(R.id.Email_login_activity)
        val Login: Button =findViewById(R.id.Button_login_activity)

        auth = Firebase.auth
        val email = Email.text.toString()
        val password = Password.text.toString()
        Login.setOnClickListener{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Main", "signInWithEmail:success")
                    }
                }
                .addOnFailureListener {
                    Log.d("Main", "signInWithEmail:failed")
                }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}