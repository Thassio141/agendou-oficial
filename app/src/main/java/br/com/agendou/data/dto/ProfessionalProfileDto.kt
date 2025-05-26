package br.com.agendou.data.dto

import br.com.agendou.domain.model.ProfessionalProfile

data class ProfessionalProfileDto(
    val professionalId: String = "",
    val bio: String? = null
) {
    fun toDomain() = ProfessionalProfile(
        professionalId = professionalId,
        bio = bio
    )
}