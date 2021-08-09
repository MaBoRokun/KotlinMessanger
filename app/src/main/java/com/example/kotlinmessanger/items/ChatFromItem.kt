package com.example.kotlinmessanger.items

import com.example.kotlinmessanger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(val text:String):Item<GroupieViewHolder>() {
    override fun getLayout():Int{
        return R.layout.chat_from_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    viewHolder.itemView.Text_chat_from.text=text
    }
}
