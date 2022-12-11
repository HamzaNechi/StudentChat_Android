package com.example.studentchat.entity

import java.util.*

data class Invitations(val _id:String,val expediteur:User,val destinataire:User,val status:String,val date: Date)
