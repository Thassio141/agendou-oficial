package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreWorkScheduleDataSource
import br.com.agendou.data.dto.WorkScheduleDto
import br.com.agendou.domain.model.WorkSchedule
import br.com.agendou.domain.repository.WorkScheduleRepository
import java.util.UUID
import javax.inject.Inject

class WorkScheduleRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreWorkScheduleDataSource
) : WorkScheduleRepository {

    override suspend fun getSchedules(professionalId: String): List<WorkSchedule> {
        return dataSource.getSchedulesForProfessional(professionalId)
            .map { it.toDomain() }
    }

    override suspend fun saveSchedule(schedule: WorkSchedule): WorkSchedule {
        val scheduleId = schedule.id.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        
        val dto = WorkScheduleDto(
            id = scheduleId,
            professionalId = schedule.professionalId,
            dayOfWeek = schedule.dayOfWeek,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            granularityMin = schedule.granularityMin ?: 0
        )
        
        dataSource.createOrUpdateSchedule(dto)
        return dto.toDomain()
    }
} 