package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreUserDataSource
import br.com.agendou.data.dto.UserDto
import br.com.agendou.domain.model.User
import br.com.agendou.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreUserDataSource
) : UserRepository {

    override suspend fun getUser(userId: String): User? {
        val dto = dataSource.getUserById(userId) ?: return null
        return dto.toDomain()
    }

    override suspend fun createUser(user: User, phoneNumber: String?, profilePictureUrl: String?): User {
        val dto = UserDto.fromDomain(user.copy(phoneNumber = phoneNumber, profilePictureUrl = profilePictureUrl))
        dataSource.createOrUpdateUser(dto)
        return dto.toDomain()
    }

    override suspend fun updatePhoneNumber(userId: String, phoneNumber: String?) {
        val existing = dataSource.getUserById(userId) ?: return
        val updated = existing.copy(phoneNumber = phoneNumber)
        dataSource.createOrUpdateUser(updated)
    }

    override suspend fun getProfessionals(): List<User> {
        val dtos = dataSource.getProfessionals()
        return dtos.map { it.toDomain() }
    }
}
