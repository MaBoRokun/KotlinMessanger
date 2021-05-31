package com.example.kotlinmessanger.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessanger.Entity.Messages
import com.example.kotlinmessanger.Entity.User
import com.example.kotlinmessanger.Items.ChatFromItem
import com.example.kotlinmessanger.Items.ChatToItem
import com.example.kotlinmessanger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    val adapter=GroupieAdapter()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        RecyclerView_ChatLog.adapter=adapter

        database=Firebase.database
        auth=Firebase.auth
        toUser= intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title=toUser?.username

        listenForMessages()
        Button_send_ChatLog.setOnClickListener {
            if(EditText_send_ChatLog.text.isEmpty()) return@setOnClickListener

            val message = EditText_send_ChatLog.text.toString()
            val fromId = auth.uid
            val toId=toUser?.uid

            if(fromId==null) return@setOnClickListener
            if(toId==null) return@setOnClickListener
            //  val reference = database.getReference("/messages").push()
            val reference = database.getReference("/user-messages/$fromId/$toId").push()
            val ToReference = database.getReference("/user-messages/$toId/$fromId").push()
            val id = reference.key
            val ChatMessage = Messages(id!!,message,fromId,toId,System.currentTimeMillis()/1000)
            reference.setValue(ChatMessage)
                    .addOnSuccessListener {
                        Log.d("Main","Message saved: ${reference.key}")
                        EditText_send_ChatLog.text.clear()
                        RecyclerView_ChatLog.scrollToPosition(adapter.itemCount-1)
                    }
            ToReference.setValue(ChatMessage)

            val LatesMessageRef = database.getReference("/latest-messages/$fromId/$toId")
            LatesMessageRef.setValue(ChatMessage)
            val LatesMessageToRef = database.getReference("/latest-messages/$toId/$fromId")
            LatesMessageRef.setValue(ChatMessage)
            LatesMessageToRef.setValue(ChatMessage)
        }
    }
    private fun listenForMessages(){
        val fromId = auth.uid
        val toId = toUser?.uid
        val ref = database.getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessages = snapshot.getValue(Messages::class.java)

                if(chatMessages!=null){
                    if(chatMessages.fromId==auth.uid){
                        adapter.add(ChatFromItem(chatMessages.text))
                    }else{
                        adapter.add(ChatToItem(chatMessages.text))
                    }
                }
                RecyclerView_ChatLog.scrollToPosition(adapter.itemCount-1)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }
}