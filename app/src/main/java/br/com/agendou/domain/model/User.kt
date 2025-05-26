package br.com.agendou.domain.model

import br.com.agendou.domain.enums.Role
import com.google.firebase.Timestamp

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role : Role = Role.CLIENT,
    val phoneNumber: String?,
    val profilePictureUrl: String?,
    val createdAt : Timestamp
)