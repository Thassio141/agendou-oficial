package br.com.agendou.domain.usecases.schedule

import br.com.agendou.domain.model.WorkSchedule
import br.com.agendou.domain.repository.WorkScheduleRepository
import javax.inject.Inject

class SaveWorkScheduleUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(schedule: WorkSchedule): WorkSchedule {
        return repository.saveSchedule(schedule)
    }
} 