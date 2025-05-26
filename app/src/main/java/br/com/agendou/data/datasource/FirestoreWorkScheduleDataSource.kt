package br.com.agendou.data.datasource

import br.com.agendou.data.dto.WorkScheduleDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreWorkScheduleDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val schedules = firestore.collection("workSchedules")

    suspend fun getSchedulesForProfessional(professionalId: String): List<WorkScheduleDto> {
        return schedules
            .whereEqualTo("professionalId", professionalId)
            .get()
            .await()
            .toObjects(WorkScheduleDto::class.java)
    }

    suspend fun createOrUpdateSchedule(schedule: WorkScheduleDto): WorkScheduleDto {
        schedules.document(schedule.id).set(schedule).await()
        return schedule
    }

    suspend fun getScheduleById(id: String): WorkScheduleDto? =
        schedules.document(id)
            .get()
            .await()
            .toObject(WorkScheduleDto::class.java)
} 