package br.com.agendou.domain.repository

import br.com.agendou.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
    suspend fun createUser(user: User, phoneNumber: String?, profilePictureUrl: String?): User
    suspend fun updatePhoneNumber(userId: String, phoneNumber: String?): Unit
    suspend fun getProfessionals(): List<User>
}