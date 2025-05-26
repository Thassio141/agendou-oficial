package br.com.agendou.domain.repository

import br.com.agendou.domain.model.Profession

interface ProfessionRepository {
    suspend fun getAllProfessions(): List<Profession>
}