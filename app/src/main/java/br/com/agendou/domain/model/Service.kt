package br.com.agendou.domain.model

import com.google.firebase.Timestamp


data class Service(
    val id: String,
    val professionalId: String,
    val name : String,
    val description: String,
    val price: Double,
    val duration: Int,
    val deletedAt : Timestamp?
)
