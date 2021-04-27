package com.example.kotlinmessanger.Messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.kotlinmessanger.R
import com.example.kotlinmessanger.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class LatesMessagesActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lates_messages)
        auth = Firebase.auth
        verifyUserIsLogginIn()
    }
   private fun verifyUserIsLogginIn(){
       val uid = auth.uid
       if(uid ==null){
           val intent=Intent(this, RegistrationActivity::class.java)
           intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
           startActivity(intent)
       }
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.menu_new_message ->{
                val intent=Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sing_out ->{
                Firebase.auth.signOut()
                val intent=Intent(this, RegistrationActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}