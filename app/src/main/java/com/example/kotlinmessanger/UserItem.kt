package com.example.kotlinmessanger


import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessanger.Entity.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class UserItem(val user: User): Item<GroupieViewHolder>() {

    override fun getLayout():Int{
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl)
            .rotate(90f)
            .into(viewHolder.itemView.profile_image_new_message)
    }
}