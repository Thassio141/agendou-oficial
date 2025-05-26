package br.com.agendou.data.dto

import br.com.agendou.domain.model.Profession

data class ProfessionDto(
    val id: String = "",
    val name: String = ""
) {
    fun toDomain() = Profession(id = id, name = name)
}