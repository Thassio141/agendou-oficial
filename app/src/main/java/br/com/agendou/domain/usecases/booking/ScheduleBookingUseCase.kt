package br.com.agendou.domain.usecases.booking

import br.com.agendou.domain.model.Booking
import br.com.agendou.domain.repository.BookingRepository
import br.com.agendou.domain.repository.ScheduleBookingRequest
import javax.inject.Inject

class ScheduleBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(request: ScheduleBookingRequest): Booking {
        return repository.scheduleBooking(request)
    }
} 