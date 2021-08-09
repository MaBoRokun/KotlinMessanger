package com.example.kotlinmessanger.activitys.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessanger.entity.User
import com.example.kotlinmessanger.R
import com.example.kotlinmessanger.items.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NewMessageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        database=Firebase.database
        auth=Firebase.auth
        supportActionBar?.title="Select User"
        fetchUsers()
    }

    companion object{
        val USER_KEY="USER_KEY"
    }
   private fun fetchUsers(){
       val CurrentUser = auth.currentUser
       val ref =database.getReference("/users")
       CoroutineScope(IO).launch {
           ref.addListenerForSingleValueEvent(object: ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   val adapter=GroupieAdapter()
                   snapshot.children.forEach {
                       Log.d("Main",it.toString())
                       val user = it.getValue(User::class.java)
                       if(user!=null) {
                           if (user.uid != CurrentUser?.uid){
                               adapter.add(UserItem(user))
                           }
                       }
                   }
                   adapter.setOnItemClickListener { item, view ->

                       val intent = Intent(view.context, ChatLogActivity::class.java)

                       val userItem=item as UserItem

                       intent.putExtra(USER_KEY,userItem.user)
                       startActivity(intent)
                       finish()
                   }

                   val Recycler:RecyclerView=findViewById(R.id.RecyclerView_NewMessage)
                   Recycler.adapter =adapter
               }

               override fun onCancelled(error: DatabaseError) {
               }
           })
       }
   }
}