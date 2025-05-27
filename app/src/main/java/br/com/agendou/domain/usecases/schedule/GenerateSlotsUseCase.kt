package br.com.agendou.domain.usecases.schedule

import br.com.agendou.domain.enums.SlotState
import br.com.agendou.domain.model.WorkSchedule
import br.com.agendou.domain.repository.BookingRepository
import br.com.agendou.domain.repository.WorkScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.*

/**
 * Gera um mapa de LocalDateTime -> SlotState para o intervalo solicitado,
 * levando em conta agendas de trabalho e reservas já existentes.
 */
class GenerateSlotsUseCase(
    private val workScheduleRepository: WorkScheduleRepository,
    private val bookingRepository: BookingRepository
) {

    suspend operator fun invoke(
        professionalId: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): Map<LocalDateTime, SlotState> = withContext(Dispatchers.Default) {
        require(!end.isBefore(start)) { "O horário final deve ser após o início" }

        // 1) Buscar horários de trabalho do profissional
        val workSchedules = workScheduleRepository.getSchedules(professionalId)

        // 2) Buscar reservas existentes no período (dia a dia)
        val bookingsByDay = mutableMapOf<LocalDate, List<br.com.agendou.domain.model.Booking>>()
        var walkDate = start.toLocalDate()
        while (!walkDate.isAfter(end.toLocalDate())) {
            val bookings = bookingRepository.getBookingsForProfessionalOn(professionalId, walkDate.atStartOfDay())
            bookingsByDay[walkDate] = bookings
            walkDate = walkDate.plusDays(1)
        }

        // 3) Gerar slots de acordo com WorkSchedule + granularidade
        val result = mutableMapOf<LocalDateTime, SlotState>()

        // Index schedules by weekday for performance
        val schedulesByWeekday: Map<Int, List<WorkSchedule>> = workSchedules.groupBy { it.dayOfWeek }

        var current = start
        while (!current.isAfter(end)) {
            val weekday = current.dayOfWeek.value // 1 = Monday, 7 = Sunday
            val schedulesForDay = schedulesByWeekday[weekday] ?: emptyList()

            // Se não existe agenda, slot é DISABLED
            if (schedulesForDay.isEmpty()) {
                result[current] = SlotState.DISABLED
                current = current.plusMinutes(15) // passo mínimo
                continue
            }

            // Verificar se horário atual está dentro de alguma agenda
            val insideAnySchedule = schedulesForDay.any { schedule ->
                val slotTime = current.toLocalTime()
                val startTime = LocalTime.parse(schedule.startTime)
                val endTime = LocalTime.parse(schedule.endTime)
                !slotTime.isBefore(startTime) && slotTime.isBefore(endTime)
            }
            if (!insideAnySchedule) {
                result[current] = SlotState.DISABLED
                current = current.plusMinutes(15)
                continue
            }

            // Verificar conflito com reservas
            val bookings = bookingsByDay[current.toLocalDate()] ?: emptyList()
            val conflicts = bookings.any { booking ->
                val bookingStart = booking.startTime.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
                val bookingEnd = booking.endTime.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
                current.isBefore(bookingEnd) && current.plusMinutes(1).isAfter(bookingStart)
            }
            result[current] = if (conflicts) SlotState.BOOKED else SlotState.FREE

            // Avançar: usar menor granularidade configurada para o dia
            val minGranularity = schedulesForDay.minOf { (it.granularityMin ?: 30) }
            current = current.plusMinutes(minGranularity.toLong())
        }

        return@withContext result
    }
} 