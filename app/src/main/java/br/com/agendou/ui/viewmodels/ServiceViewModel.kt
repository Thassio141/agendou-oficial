package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.Service
import br.com.agendou.domain.usecases.service.CreateServiceUseCase
import br.com.agendou.domain.usecases.service.DeleteServiceUseCase
import br.com.agendou.domain.usecases.service.GetServicesForProfessionalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesForProfessionalUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase
) : ViewModel() {

    private val _servicesState = MutableStateFlow<ServicesState>(ServicesState.Initial)
    val servicesState: StateFlow<ServicesState> = _servicesState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _serviceOperationState = MutableStateFlow<ServiceOperationState>(ServiceOperationState.Initial)
    val serviceOperationState: StateFlow<ServiceOperationState> = _serviceOperationState

    fun getServicesForProfessional(professionalId: String) {
        viewModelScope.launch {
            _servicesState.value = ServicesState.Loading
            try {
                val services = getServicesUseCase(professionalId)
                _servicesState.value = ServicesState.Success(services)
            } catch (e: Exception) {
                _servicesState.value = ServicesState.Error(e.message ?: "Erro ao buscar serviços")
            }
        }
    }

    fun loadServicesForProfessional(professionalId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val services = getServicesUseCase(professionalId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    services = services,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar serviços"
                )
            }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            _serviceOperationState.value = ServiceOperationState.Loading
            try {
                val createdService = createServiceUseCase(service)
                _serviceOperationState.value = ServiceOperationState.Created(createdService)
                // Atualizar a lista de serviços após a criação
                getServicesForProfessional(service.professionalId)
            } catch (e: Exception) {
                _serviceOperationState.value = 
                    ServiceOperationState.Error(e.message ?: "Erro ao criar serviço")
            }
        }
    }

    fun deleteService(serviceId: String, professionalId: String) {
        viewModelScope.launch {
            _serviceOperationState.value = ServiceOperationState.Loading
            try {
                deleteServiceUseCase(serviceId)
                _serviceOperationState.value = ServiceOperationState.Deleted
                // Atualizar a lista de serviços após a exclusão
                getServicesForProfessional(professionalId)
            } catch (e: Exception) {
                _serviceOperationState.value = 
                    ServiceOperationState.Error(e.message ?: "Erro ao excluir serviço")
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val services: List<Service> = emptyList(),
        val error: String? = null
    )

    sealed class ServicesState {
        object Initial : ServicesState()
        object Loading : ServicesState()
        data class Success(val services: List<Service>) : ServicesState()
        data class Error(val message: String) : ServicesState()
    }

    sealed class ServiceOperationState {
        object Initial : ServiceOperationState()
        object Loading : ServiceOperationState()
        data class Created(val service: Service) : ServiceOperationState()
        object Deleted : ServiceOperationState()
        data class Error(val message: String) : ServiceOperationState()
    }
} 