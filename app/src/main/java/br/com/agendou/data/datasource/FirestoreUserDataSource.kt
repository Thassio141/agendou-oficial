package br.com.agendou.data.datasource

import br.com.agendou.data.dto.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val users = firestore.collection("users")

    suspend fun getUserById(id: String): UserDto? =
        users.document(id)
            .get()
            .await()
            .toObject(UserDto::class.java)

    suspend fun createOrUpdateUser(dto: UserDto) {
        users.document(dto.id).set(dto).await()
    }
}
