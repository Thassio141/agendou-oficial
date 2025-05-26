package br.com.agendou.data.datasource

import br.com.agendou.data.dto.BookingDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import java.time.LocalDateTime
import java.time.ZoneOffset

class FirestoreBookingDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val bookings = firestore.collection("bookings")

    suspend fun getBookingsForProfessionalByDate(
        professionalId: String,
        date: LocalDateTime
    ): List<BookingDto> {
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().atTime(23, 59, 59)
        
        val startTimestamp = Timestamp(startOfDay.toEpochSecond(ZoneOffset.UTC), 0)
        val endTimestamp = Timestamp(endOfDay.toEpochSecond(ZoneOffset.UTC), 0)
        
        return bookings
            .whereEqualTo("professionalId", professionalId)
            .whereGreaterThanOrEqualTo("startTime", startTimestamp)
            .whereLessThanOrEqualTo("startTime", endTimestamp)
            .whereEqualTo("deletedAt", null)
            .get()
            .await()
            .toObjects(BookingDto::class.java)
    }

    suspend fun createBooking(booking: BookingDto): BookingDto {
        val docRef = bookings.document(booking.id)
        docRef.set(booking).await()
        return booking
    }

    suspend fun getBookingById(id: String): BookingDto? =
        bookings.document(id)
            .get()
            .await()
            .toObject(BookingDto::class.java)

    suspend fun updateBooking(booking: BookingDto) {
        bookings.document(booking.id).set(booking).await()
    }
} 