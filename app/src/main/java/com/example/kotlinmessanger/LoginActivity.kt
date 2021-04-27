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
        val Password: EditText =findViewById(R.id.Password_login_activity)
        val Login: Button =findViewById(R.id.Button_login_activity)

        auth = Firebase.auth


        Login.setOnClickListener{
            val email = Email.text.toString()
            val password = Password.text.toString()

            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Main", "signInWithEmail:success")
                            val user = auth.currentUser
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Main", "signInWithEmail:failure", task.exception)
                        }
                    }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}