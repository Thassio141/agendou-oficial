package br.com.agendou.data.dto

import br.com.agendou.domain.enums.Role
import br.com.agendou.domain.model.User
import com.google.firebase.Timestamp


data class UserDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val phoneNumber: String? = null,
    val profilePictureUrl: String? = null,
    val createdAt: Timestamp = Timestamp.now()
) {
    fun toDomain() = User(
        id = id,
        name = name,
        email = email,
        role = Role.valueOf(role),
        phoneNumber = phoneNumber,
        profilePictureUrl = profilePictureUrl,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(user: User) = UserDto(
            id = user.id,
            name = user.name,
            email = user.email,
            role = user.role.name,
            phoneNumber = user.phoneNumber,
            profilePictureUrl = user.profilePictureUrl,
            createdAt = user.createdAt
        )
    }
}