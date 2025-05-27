package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreBookingDataSource
import br.com.agendou.data.dto.BookingDto
import br.com.agendou.domain.enums.BookingStatus
import br.com.agendou.domain.model.Booking
import br.com.agendou.domain.repository.BookingRepository
import br.com.agendou.domain.repository.ScheduleBookingRequest
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import java.util.UUID

class BookingRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreBookingDataSource
) : BookingRepository {

    override suspend fun getBookingsForProfessionalOn(
        professionalId: String,
        date: LocalDateTime
    ): List<Booking> {
        return dataSource.getBookingsForProfessionalByDate(professionalId, date)
            .map { it.toDomain() }
    }

    override suspend fun scheduleBooking(request: ScheduleBookingRequest): Booking {
        val now = Timestamp.now()
        val bookingId = UUID.randomUUID().toString()
        
        val startTimestamp = Timestamp(request.startTime.toEpochSecond(ZoneOffset.UTC), 0)
        val endTimestamp = Timestamp(request.endTime.toEpochSecond(ZoneOffset.UTC), 0)
        
        val dto = BookingDto(
            id = bookingId,
            clientId = request.clientId,
            professionalId = request.professionalId, 
            serviceId = request.serviceId,
            startTime = startTimestamp,
            endTime = endTimestamp,
            status = BookingStatus.PENDING.name,
            createdAt = now,
            deletedAt = null
        )
        
        dataSource.createBooking(dto)
        return dto.toDomain()
    }

    override suspend fun reserve(request: ScheduleBookingRequest): Booking {
        val dto = dataSource.reserveBooking(request)
        return dto.toDomain()
    }

    override suspend fun cancelBooking(bookingId: String) {
        val booking = dataSource.getBookingById(bookingId) ?: return
        val updated = booking.copy(
            status = BookingStatus.CANCELLED.name,
            deletedAt = Timestamp.now()
        )
        dataSource.updateBooking(updated)
    }
} 