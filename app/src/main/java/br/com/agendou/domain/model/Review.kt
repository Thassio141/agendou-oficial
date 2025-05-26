package br.com.agendou.domain.model

import com.google.firebase.Timestamp

data class Review(
    val id: String,
    val bookingId: String,
    val rating: Int,
    val comment: String? = null,
    val createdAt: Timestamp
)