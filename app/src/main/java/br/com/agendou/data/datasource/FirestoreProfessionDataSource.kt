package br.com.agendou.data.datasource

import br.com.agendou.data.dto.ProfessionDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreProfessionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val professions = firestore.collection("professions")

    suspend fun getAllProfessions(): List<ProfessionDto> {
        return professions
            .get()
            .await()
            .toObjects(ProfessionDto::class.java)
    }

    suspend fun getProfessionById(id: String): ProfessionDto? =
        professions.document(id)
            .get()
            .await()
            .toObject(ProfessionDto::class.java)
} 