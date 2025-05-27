package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirebaseAuthDataSource
import br.com.agendou.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<String> {
        return firebaseAuthDataSource.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<String> {
        return firebaseAuthDataSource.signUp(email, password)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return firebaseAuthDataSource.sendPasswordReset(email)
    }

    override suspend fun signOut(): Result<Unit> {
        return firebaseAuthDataSource.signOut()
    }

    override fun getAuthState(): Flow<Boolean> {
        return firebaseAuthDataSource.getAuthState()
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuthDataSource.getCurrentUserId()
    }
} 