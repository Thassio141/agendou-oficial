package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreProfessionDataSource
import br.com.agendou.domain.model.Profession
import br.com.agendou.domain.repository.ProfessionRepository
import javax.inject.Inject

class ProfessionRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreProfessionDataSource
) : ProfessionRepository {

    override suspend fun getAllProfessions(): List<Profession> {
        return dataSource.getAllProfessions()
            .map { it.toDomain() }
    }
} 