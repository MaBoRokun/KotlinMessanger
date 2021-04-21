package com.example.kotlinmessanger.Entity

data class User(val uid:String,val username:String,val profileImageUrl:String){
    constructor():this("","","")
}