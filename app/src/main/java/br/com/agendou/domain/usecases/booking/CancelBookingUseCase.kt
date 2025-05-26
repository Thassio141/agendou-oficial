package br.com.agendou.domain.usecases.booking

import br.com.agendou.domain.repository.BookingRepository
import javax.inject.Inject

class CancelBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String) {
        repository.cancelBooking(bookingId)
    }
} 