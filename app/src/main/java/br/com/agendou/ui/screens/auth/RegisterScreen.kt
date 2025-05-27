// ui/screens/auth/RegisterScreen.kt
package br.com.agendou.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.R
import br.com.agendou.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFF0A2535), Color(0xFF13425A)))
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var vis1 by remember { mutableStateOf(false) }
    var vis2 by remember { mutableStateOf(false) }
    val ui by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
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
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 100.dp)
        ) {
            Text(
                "Criar Conta",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Preencha os dados para se cadastrar",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB0BEC5),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))

            @Composable
            fun Modifier.fieldModifier() = this
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                placeholder = { Text("Nome completo") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                singleLine = true,
                modifier = Modifier.fieldModifier(),
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
                value = email, onValueChange = { email = it },
                placeholder = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fieldModifier(),
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
                    IconButton (onClick = { vis1 = !vis1 }){
                        Icon(
                            if (vis1) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (vis1) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fieldModifier(),
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
                value = confirm, onValueChange = { confirm = it },
                placeholder = { Text("Confirmar senha") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton ( onClick = { vis2 = !vis2 }) {
                        Icon(
                            if (vis2) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (vis2) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = confirm.isNotBlank() && password != confirm,
                modifier = Modifier.fieldModifier(),
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
            if (confirm.isNotBlank() && password != confirm) {
                Text(
                    "Senhas não coincidem",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.signUp(email.trim(), password, name.trim(), confirm.trim())},
                enabled = !ui.isLoading && name.isNotBlank() && email.isNotBlank()
                        && password.isNotBlank() && confirm.isNotBlank() && password == confirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.large),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                if (ui.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Cadastrar", color = Color(0xFF0A2535))
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Já tem conta? Faça login", color = Color.White)
            }
        }
    }
}
