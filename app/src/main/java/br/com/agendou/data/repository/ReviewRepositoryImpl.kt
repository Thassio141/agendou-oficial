package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreReviewDataSource
import br.com.agendou.data.dto.ReviewDto
import br.com.agendou.domain.model.Review
import br.com.agendou.domain.repository.ReviewRepository
import com.google.firebase.Timestamp
import java.util.UUID
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreReviewDataSource
) : ReviewRepository {

    override suspend fun getReviewsForProfessional(professionalId: String): List<Review> {
        return dataSource.getReviewsForProfessional(professionalId)
            .map { it.toDomain() }
    }

    override suspend fun saveReview(review: Review): Review {
        val reviewId = review.id.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        val now = Timestamp.now()
        
        val dto = ReviewDto(
            id = reviewId,
            bookingId = review.bookingId,
            rating = review.rating,
            comment = review.comment,
            createdAt = now
        )
        
        dataSource.createReview(dto)
        return dto.toDomain()
    }
} 