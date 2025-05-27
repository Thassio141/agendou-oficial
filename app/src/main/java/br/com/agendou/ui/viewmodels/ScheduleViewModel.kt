package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.WorkSchedule
import br.com.agendou.domain.usecases.schedule.GetWorkSchedulesUseCase
import br.com.agendou.domain.usecases.schedule.SaveWorkScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getWorkSchedulesUseCase: GetWorkSchedulesUseCase,
    private val saveWorkScheduleUseCase: SaveWorkScheduleUseCase
) : ViewModel() {

    private val _schedulesState = MutableStateFlow<SchedulesState>(SchedulesState.Initial)
    val schedulesState: StateFlow<SchedulesState> = _schedulesState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _scheduleOperationState = MutableStateFlow<ScheduleOperationState>(ScheduleOperationState.Initial)
    val scheduleOperationState: StateFlow<ScheduleOperationState> = _scheduleOperationState

    fun getWorkSchedules(professionalId: String) {
        viewModelScope.launch {
            _schedulesState.value = SchedulesState.Loading
            try {
                val schedules = getWorkSchedulesUseCase(professionalId)
                _schedulesState.value = SchedulesState.Success(schedules)
            } catch (e: Exception) {
                _schedulesState.value = SchedulesState.Error(e.message ?: "Erro ao buscar horários de trabalho")
            }
        }
    }

    fun saveWorkSchedule(schedule: WorkSchedule) {
        viewModelScope.launch {
            _scheduleOperationState.value = ScheduleOperationState.Loading
            try {
                val savedSchedule = saveWorkScheduleUseCase(schedule)
                _scheduleOperationState.value = ScheduleOperationState.Saved(savedSchedule)
                // Atualizar a lista de horários após a criação/atualização
                getWorkSchedules(schedule.professionalId)
            } catch (e: Exception) {
                _scheduleOperationState.value = 
                    ScheduleOperationState.Error(e.message ?: "Erro ao salvar horário de trabalho")
            }
        }
    }

    fun loadAvailability(serviceId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // TODO: Implementar lógica de verificação de disponibilidade
                // Por enquanto, apenas simula carregamento
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar disponibilidade"
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class SchedulesState {
        object Initial : SchedulesState()
        object Loading : SchedulesState()
        data class Success(val schedules: List<WorkSchedule>) : SchedulesState()
        data class Error(val message: String) : SchedulesState()
    }

    sealed class ScheduleOperationState {
        object Initial : ScheduleOperationState()
        object Loading : ScheduleOperationState()
        data class Saved(val schedule: WorkSchedule) : ScheduleOperationState()
        data class Error(val message: String) : ScheduleOperationState()
    }
} 