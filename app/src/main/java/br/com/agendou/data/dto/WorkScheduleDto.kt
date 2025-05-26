package br.com.agendou.data.dto

import br.com.agendou.domain.model.WorkSchedule

data class WorkScheduleDto(
    val id: String = "",
    val professionalId: String = "",
    val dayOfWeek: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
    val granularityMin: Int = 0
) {
    fun toDomain() = WorkSchedule(
        id = id,
        professionalId = professionalId,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime,
        granularityMin = granularityMin
    )
}