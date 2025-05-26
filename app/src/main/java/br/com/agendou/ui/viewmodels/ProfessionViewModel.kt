package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.Profession
import br.com.agendou.domain.usecases.profession.GetAllProfessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfessionViewModel @Inject constructor(
    private val getAllProfessionsUseCase: GetAllProfessionsUseCase
) : ViewModel() {

    private val _professionsState = MutableStateFlow<ProfessionsState>(ProfessionsState.Initial)
    val professionsState: StateFlow<ProfessionsState> = _professionsState

    fun getAllProfessions() {
        viewModelScope.launch {
            _professionsState.value = ProfessionsState.Loading
            try {
                val professions = getAllProfessionsUseCase()
                _professionsState.value = ProfessionsState.Success(professions)
            } catch (e: Exception) {
                _professionsState.value = ProfessionsState.Error(e.message ?: "Erro ao buscar profissões")
            }
        }
    }

    sealed class ProfessionsState {
        object Initial : ProfessionsState()
        object Loading : ProfessionsState()
        data class Success(val professions: List<Profession>) : ProfessionsState()
        data class Error(val message: String) : ProfessionsState()
    }
} 