package br.com.agendou.domain.repository

import br.com.agendou.domain.model.Booking
import java.time.LocalDateTime

interface BookingRepository {
    suspend fun getBookingsForProfessionalOn(
        professionalId: String,
        date: LocalDateTime
    ): List<Booking>
    suspend fun scheduleBooking(request: ScheduleBookingRequest): Booking
    suspend fun cancelBooking(bookingId: String): Unit
}