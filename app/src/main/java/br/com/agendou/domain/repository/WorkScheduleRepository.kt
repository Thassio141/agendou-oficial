package br.com.agendou.domain.repository

import br.com.agendou.domain.model.WorkSchedule

interface WorkScheduleRepository {
    suspend fun getSchedules(professionalId: String): List<WorkSchedule>
    suspend fun saveSchedule(schedule: WorkSchedule): WorkSchedule
}