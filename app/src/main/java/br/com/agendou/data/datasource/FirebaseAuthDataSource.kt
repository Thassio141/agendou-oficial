package br.com.agendou.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Usuário não encontrado")
            Result.success(userId)
        } catch (e: FirebaseAuthException) {
            Result.failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Falha ao criar usuário")
            Result.success(userId)
        } catch (e: FirebaseAuthException) {
            Result.failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.failure(mapFirebaseException(e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAuthState(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    private fun mapFirebaseException(exception: FirebaseAuthException): Exception {
        return when (exception.errorCode) {
            "ERROR_INVALID_EMAIL" -> Exception("Email inválido")
            "ERROR_WRONG_PASSWORD" -> Exception("Senha incorreta")
            "ERROR_USER_NOT_FOUND" -> Exception("Usuário não encontrado")
            "ERROR_USER_DISABLED" -> Exception("Conta desabilitada")
            "ERROR_TOO_MANY_REQUESTS" -> Exception("Muitas tentativas. Tente novamente mais tarde")
            "ERROR_EMAIL_ALREADY_IN_USE" -> Exception("Este email já está em uso")
            "ERROR_WEAK_PASSWORD" -> Exception("Senha muito fraca. Use pelo menos 6 caracteres")
            "ERROR_NETWORK_REQUEST_FAILED" -> Exception("Erro de conexão. Verifique sua internet")
            else -> Exception("Erro de autenticação: ${exception.message}")
        }
    }
} 