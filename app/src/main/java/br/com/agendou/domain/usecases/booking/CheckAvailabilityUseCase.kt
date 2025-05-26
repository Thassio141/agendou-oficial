package br.com.agendou.domain.usecases.booking

import br.com.agendou.domain.model.Booking
import br.com.agendou.domain.repository.BookingRepository
import br.com.agendou.domain.repository.WorkScheduleRepository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class CheckAvailabilityUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val scheduleRepository: WorkScheduleRepository
) {
    suspend operator fun invoke(
        professionalId: String,
        date: LocalDateTime,
        requestedStartTime: LocalTime,
        requestedEndTime: LocalTime
    ): Boolean {
        // Verificar se o profissional trabalha nesse dia da semana
        val dayOfWeek = date.dayOfWeek.value
        val workSchedules = scheduleRepository.getSchedules(professionalId)
        val scheduleForDay = workSchedules.find { it.dayOfWeek == dayOfWeek }
            ?: return false // Profissional não trabalha nesse dia

        // Verificar se o horário solicitado está dentro do horário de trabalho
        val scheduleStart = LocalTime.parse(scheduleForDay.startTime)
        val scheduleEnd = LocalTime.parse(scheduleForDay.endTime)

        if (requestedStartTime.isBefore(scheduleStart) || requestedEndTime.isAfter(scheduleEnd)) {
            return false // Horário fora do período de trabalho
        }

        // Obter agendamentos existentes nesse dia
        val existingBookings = bookingRepository.getBookingsForProfessionalOn(professionalId, date)
        val zoneId = ZoneId.systemDefault()

        // Verificar se há conflito com algum agendamento existente
        return existingBookings.none { booking ->
            val bookingStartDateTime = booking.startTime.toDate().toInstant().atZone(zoneId).toLocalTime()
            val bookingEndDateTime = booking.endTime.toDate().toInstant().atZone(zoneId).toLocalTime()

            // Verificar sobreposição
            !(requestedEndTime.isBefore(bookingStartDateTime) || requestedStartTime.isAfter(bookingEndDateTime))
        }
    }
} 