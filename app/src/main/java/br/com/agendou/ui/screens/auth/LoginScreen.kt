// ui/screens/auth/LoginScreen.kt
package br.com.agendou.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.R
import br.com.agendou.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFF0A2535), Color(0xFF13425A)))
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var vis by remember { mutableStateOf(false) }
    val ui by viewModel.uiState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Spacer(Modifier.height(40.dp))
            Image(
                painter = painterResource(R.drawable.logotipo_agendou_branco_png),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                "Bem-vindo de volta!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Faça login para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB0BEC5)
            )
            Spacer(Modifier.height(32.dp))

            @Composable
            fun Modifier.field() = this
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                placeholder = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.field(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33FFFFFF),
                    unfocusedContainerColor = Color(0x33FFFFFF),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color(0x99FFFFFF),
                    unfocusedPlaceholderColor = Color(0x99FFFFFF),
                    cursorColor = Color.White
                )
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                placeholder = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton ( onClick = { vis = !vis }){
                        Icon(
                            if (vis) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (vis) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.field(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x33FFFFFF),
                    unfocusedContainerColor = Color(0x33FFFFFF),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color(0x99FFFFFF),
                    unfocusedPlaceholderColor = Color(0x99FFFFFF),
                    cursorColor = Color.White
                )
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onNavigateToForgotPassword) { Text("Esqueceu a senha?", color = Color.White) }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signIn(email.trim(), password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.large),
                enabled = !ui.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                if (ui.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Entrar", color = Color(0xFF0A2535))
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onNavigateToRegister) { Text("Cadastre-se", color = Color.White) }
        }
    }
}
