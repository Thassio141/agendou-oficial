package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.Review
import br.com.agendou.domain.usecases.review.GetReviewsForProfessionalUseCase
import br.com.agendou.domain.usecases.review.SaveReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsForProfessionalUseCase,
    private val saveReviewUseCase: SaveReviewUseCase
) : ViewModel() {

    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Initial)
    val reviewsState: StateFlow<ReviewsState> = _reviewsState

    private val _reviewOperationState = MutableStateFlow<ReviewOperationState>(ReviewOperationState.Initial)
    val reviewOperationState: StateFlow<ReviewOperationState> = _reviewOperationState

    fun getReviewsForProfessional(professionalId: String) {
        viewModelScope.launch {
            _reviewsState.value = ReviewsState.Loading
            try {
                val reviews = getReviewsUseCase(professionalId)
                _reviewsState.value = ReviewsState.Success(reviews)
            } catch (e: Exception) {
                _reviewsState.value = ReviewsState.Error(e.message ?: "Erro ao buscar avaliações")
            }
        }
    }

    fun saveReview(review: Review) {
        viewModelScope.launch {
            _reviewOperationState.value = ReviewOperationState.Loading
            try {
                val savedReview = saveReviewUseCase(review)
                _reviewOperationState.value = ReviewOperationState.Saved(savedReview)
                
                // Buscar as avaliações atualizadas se tivermos o ID do profissional
                val bookingId = review.bookingId
                if (bookingId.isNotBlank()) {
                    // Aqui seria ideal ter um caso de uso para obter o booking pelo ID
                    // e então obter o professionalId para atualizar as avaliações
                    // Por enquanto, deixamos essa funcionalidade pendente
                }
            } catch (e: Exception) {
                _reviewOperationState.value = 
                    ReviewOperationState.Error(e.message ?: "Erro ao salvar avaliação")
            }
        }
    }

    sealed class ReviewsState {
        object Initial : ReviewsState()
        object Loading : ReviewsState()
        data class Success(val reviews: List<Review>) : ReviewsState()
        data class Error(val message: String) : ReviewsState()
    }

    sealed class ReviewOperationState {
        object Initial : ReviewOperationState()
        object Loading : ReviewOperationState()
        data class Saved(val review: Review) : ReviewOperationState()
        data class Error(val message: String) : ReviewOperationState()
    }
} 