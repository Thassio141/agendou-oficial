package br.com.agendou.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(email: String, password: String, name: String): Result<String>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun getAuthState(): Flow<Boolean>
    fun getCurrentUserId(): String?
} 