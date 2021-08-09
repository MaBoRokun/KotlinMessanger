package com.example.kotlinmessanger.items
import com.example.kotlinmessanger.entity.Messages
import com.example.kotlinmessanger.entity.User
import com.example.kotlinmessanger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.lates_message_row.view.*

class LatesMessageItemRow(val chatMessage: Messages) :Item<GroupieViewHolder>()  {
    var chatPartnetUser: User? = null

    override fun getLayout(): Int {
        return R.layout.lates_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.User_LatesMessage.text=chatMessage.text
        val chatPartnerid:String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerid =chatMessage.toId
        }else{
            chatPartnerid = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnetUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.Username_text.text=chatPartnetUser?.username
                val targetImageView = viewHolder.itemView.User_Image
                Picasso.get().load(chatPartnetUser?.profileImageUrl)
                        .rotate(90f)
                        .into(targetImageView)
            }

        })

    }
}