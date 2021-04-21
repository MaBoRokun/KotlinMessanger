package com.example.kotlinmessanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessanger.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter

class NewMessageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        database=Firebase.database
        supportActionBar?.title="Select User"
        val Recycler:RecyclerView=findViewById(R.id.RecyclerView_NewMessage)

        val adapter = GroupieAdapter()

        Recycler.adapter =adapter

        fetchUsers()
    }
   private fun fetchUsers(){
       val ref =database.getReference("/users")
       ref.addListenerForSingleValueEvent(object: ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val adapter=GroupieAdapter()
               snapshot.children.forEach {
                   Log.d("Main",it.toString())
                   val user = it.getValue(User::class.java)
                   if(user!=null)
                   adapter.add(UserItem(user))
               }
               val Recycler:RecyclerView=findViewById(R.id.RecyclerView_NewMessage)
               Recycler.adapter =adapter
           }

           override fun onCancelled(error: DatabaseError) {
           }
       })
   }
}