package com.example.kotlinmessanger.Items

import com.example.kotlinmessanger.Entity.User
import com.example.kotlinmessanger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class ChatFromItem:Item<GroupieViewHolder>() {
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.itemView.username_new_message.text = user.username
//        Picasso.get().load(user.profileImageUrl)
//            .rotate(90f)
//            .into(viewHolder.itemView.profile_image_new_message)
    }
}
