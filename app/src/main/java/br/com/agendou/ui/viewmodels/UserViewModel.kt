package br.com.agendou.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.User
import br.com.agendou.domain.usecases.user.CreateUserUseCase
import br.com.agendou.domain.usecases.user.GetUserUseCase
import br.com.agendou.domain.usecases.user.GetProfessionalsUseCase
import br.com.agendou.domain.usecases.user.UpdatePhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updatePhoneNumberUseCase: UpdatePhoneNumberUseCase,
    private val getProfessionalsUseCase: GetProfessionalsUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Initial)
    val userState: StateFlow<UserState> = _userState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun getUser(userId: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val user = getUserUseCase(userId)
                _currentUser.value = user
                _userState.value = if (user != null) {
                    UserState.Success(user)
                } else {
                    UserState.Error("Usuário não encontrado")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro desconhecido ao buscar usuário")
            }
        }
    }

    fun createUser(user: User, phoneNumber: String? = null, profilePictureUrl: String? = null) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val createdUser = createUserUseCase(user, phoneNumber, profilePictureUrl)
                _currentUser.value = createdUser
                _userState.value = UserState.Success(createdUser)
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro desconhecido ao criar usuário")
            }
        }
    }

    fun updatePhoneNumber(userId: String, phoneNumber: String?) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                updatePhoneNumberUseCase(userId, phoneNumber)
                // Recarregar o usuário para obter dados atualizados
                getUser(userId)
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao atualizar número de telefone")
            }
        }
    }

    fun loadProfessionals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val professionals = getProfessionalsUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    users = professionals,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar profissionais"
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val users: List<User> = emptyList(),
        val error: String? = null
    )

    sealed class UserState {
        object Initial : UserState()
        object Loading : UserState()
        data class Success(val user: User) : UserState()
        data class Error(val message: String) : UserState()
    }
} 