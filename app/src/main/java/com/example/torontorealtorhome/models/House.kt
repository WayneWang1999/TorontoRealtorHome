package com.example.torontorenthome.models

import com.google.firebase.firestore.DocumentId

data class House (
    @DocumentId
    val houseId:String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val description: String = "",
    val bedrooms: Int = 0,
    val bathrooms: Int = 2,
    val area: Int = 800,
    val price: Int = 0,
    val ownerId: String = "wwgtFtJ4LpeqsgECNtn6UTnHFUI3",
    val imageUrl:List<String> = listOf(
        "https://images.unsplash.com/photo-1480074568708-e7b720bb3f09?q=80&w=2074&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1480074568708-e7b720bb3f09?q=80&w=2074&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    val type: String = "House",
    val createTime: String = "2 weeks",
    val isAvailable:Boolean=true
)


