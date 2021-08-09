package com.example.kotlinmessanger.items

import com.example.kotlinmessanger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(val text:String):Item<GroupieViewHolder>() {
    override fun getLayout():Int{
        return R.layout.chat_to_row
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.Text_chat_to.text=text
    }
}
