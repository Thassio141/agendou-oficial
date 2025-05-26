package br.com.agendou.data.dto

import br.com.agendou.domain.model.Review
import com.google.firebase.Timestamp

data class ReviewDto(
    val id: String = "",
    val bookingId: String = "",
    val rating: Int = 0,
    val comment: String? = null,
    val createdAt: Timestamp = Timestamp.now()
) {
    fun toDomain() = Review(
        id = id,
        bookingId = bookingId,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}