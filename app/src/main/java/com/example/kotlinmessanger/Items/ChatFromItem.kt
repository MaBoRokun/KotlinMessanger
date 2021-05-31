package com.example.kotlinmessanger.Items

import com.example.kotlinmessanger.Entity.User
import com.example.kotlinmessanger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class ChatFromItem(val text:String):Item<GroupieViewHolder>() {
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.Text_chat_from.text=text
    }
}
