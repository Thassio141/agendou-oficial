package br.com.agendou.domain.usecases.profession

import br.com.agendou.domain.model.Profession
import br.com.agendou.domain.repository.ProfessionRepository
import javax.inject.Inject

class GetAllProfessionsUseCase @Inject constructor(
    private val repository: ProfessionRepository
) {
    suspend operator fun invoke(): List<Profession> {
        return repository.getAllProfessions()
    }
} 