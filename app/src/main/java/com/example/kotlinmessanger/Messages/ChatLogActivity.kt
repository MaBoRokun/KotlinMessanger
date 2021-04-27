package com.example.kotlinmessanger.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessanger.Entity.User
import com.example.kotlinmessanger.Items.ChatFromItem
import com.example.kotlinmessanger.Items.ChatToItem
import com.example.kotlinmessanger.R
import com.xwray.groupie.GroupieAdapter

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)



       val user= intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title=user?.username

        val adapter= GroupieAdapter()

        val recycle_chatlog:RecyclerView=findViewById(R.id.RecyclerView_ChatLog)

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        recycle_chatlog.adapter=adapter

    }
}