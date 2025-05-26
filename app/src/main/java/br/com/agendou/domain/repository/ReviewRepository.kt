package br.com.agendou.domain.repository

import br.com.agendou.domain.model.Review

interface ReviewRepository {
    suspend fun getReviewsForProfessional(professionalId: String): List<Review>
    suspend fun saveReview(review: Review): Review
}