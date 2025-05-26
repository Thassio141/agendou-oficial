package br.com.agendou.domain.usecases.review

import br.com.agendou.domain.model.Review
import br.com.agendou.domain.repository.ReviewRepository
import javax.inject.Inject

class SaveReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(review: Review): Review {
        return repository.saveReview(review)
    }
} 