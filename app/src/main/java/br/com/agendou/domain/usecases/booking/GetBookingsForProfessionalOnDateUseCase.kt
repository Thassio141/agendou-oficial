package br.com.agendou.domain.usecases.booking

import br.com.agendou.domain.model.Booking
import br.com.agendou.domain.repository.BookingRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetBookingsForProfessionalOnDateUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(professionalId: String, date: LocalDateTime): List<Booking> {
        return repository.getBookingsForProfessionalOn(professionalId, date)
    }
} 