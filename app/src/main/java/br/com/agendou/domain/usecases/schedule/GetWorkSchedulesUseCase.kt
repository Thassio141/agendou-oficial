package br.com.agendou.domain.usecases.schedule

import br.com.agendou.domain.model.WorkSchedule
import br.com.agendou.domain.repository.WorkScheduleRepository
import javax.inject.Inject

class GetWorkSchedulesUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(professionalId: String): List<WorkSchedule> {
        return repository.getSchedules(professionalId)
    }
} 