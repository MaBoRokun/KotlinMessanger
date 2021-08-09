package com.example.kotlinmessanger.activitys.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessanger.entity.Messages
import com.example.kotlinmessanger.entity.User
import com.example.kotlinmessanger.items.LatesMessageItemRow
import com.example.kotlinmessanger.R
import com.example.kotlinmessanger.activitys.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_lates_messages.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LatesMessagesActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    companion object{
        var currentUser: User? = null;
    }
    val adapter= GroupieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lates_messages)
        auth = Firebase.auth
        database=Firebase.database
        storage=Firebase.storage


        RecyclerView_Lates_Message.adapter=adapter
        RecyclerView_Lates_Message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        
        fetchUser()
       verifyUserIsLogginIn()
        listenForLatesMessages()
        
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this,ChatLogActivity::class.java)
            val row = item as LatesMessageItemRow
            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnetUser)
            startActivity(intent)
        }
        
    }

    val LatestMessageMap = HashMap<String, Messages>()

    private fun refreshRecycler(){
        adapter.clear()
        LatestMessageMap.values.forEach {
            adapter.add(LatesMessageItemRow(it))
        }
    }

    private fun listenForLatesMessages(){
        val fromId=auth.uid
        val ref = database.getReference("/latest-messages/$fromId")
        CoroutineScope(IO).launch {
            ref.addChildEventListener(object: ChildEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val ChatMessage = snapshot.getValue(Messages::class.java)?:return
                    LatestMessageMap[snapshot.key!!] = ChatMessage
                    refreshRecycler()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val ChatMessage = snapshot.getValue(Messages::class.java)?:return
                    LatestMessageMap[snapshot.key!!] = ChatMessage
                    refreshRecycler()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }
            })
        }
    }


    private fun fetchUser(){
        val uid = auth.uid
        val ref= database.getReference("/users/$uid")
        CoroutineScope(IO).launch {
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUser = snapshot.getValue(User::class.java)
                }
            })
        }
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