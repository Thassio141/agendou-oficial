package br.com.agendou.domain.usecases.review

import br.com.agendou.domain.model.Review
import br.com.agendou.domain.repository.ReviewRepository
import javax.inject.Inject

class GetReviewsForProfessionalUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(professionalId: String): List<Review> {
        return repository.getReviewsForProfessional(professionalId)
    }
} 