package br.com.agendou.data.datasource

import br.com.agendou.data.dto.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreReviewDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val reviews = firestore.collection("reviews")

    suspend fun getReviewsForProfessional(professionalId: String): List<ReviewDto> {
        return reviews
            .whereEqualTo("professionalId", professionalId)
            .get()
            .await()
            .toObjects(ReviewDto::class.java)
    }

    suspend fun createReview(review: ReviewDto): ReviewDto {
        reviews.document(review.id).set(review).await()
        return review
    }

    suspend fun getReviewById(id: String): ReviewDto? =
        reviews.document(id)
            .get()
            .await()
            .toObject(ReviewDto::class.java)
} 