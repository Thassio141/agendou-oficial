package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.usecases.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isAuthenticated: Flow<Boolean> = getAuthStateUseCase()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            signInUseCase(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Erro desconhecido"
                    )
                }
        }
    }

    fun signUp(email: String, password: String, name: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(error = "As senhas não coincidem")
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(error = "A senha deve ter pelo menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            signUpUseCase(email, password, name)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Erro desconhecido"
                    )
                }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            sendPasswordResetUseCase(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        passwordResetSent = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Erro desconhecido"
                    )
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearPasswordResetSent() {
        _uiState.value = _uiState.value.copy(passwordResetSent = false)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val passwordResetSent: Boolean = false
) 