package com.example.kotlinmessanger.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid:String,val email:String,val username:String,val profileImageUrl:String):
    Parcelable {
    constructor():this("","","","")
}