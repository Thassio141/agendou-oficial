package br.com.agendou.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.R
import br.com.agendou.ui.viewmodels.AuthViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFF0A2535), Color(0xFF13425A)))
    var email by remember { mutableStateOf("") }
    val ui by viewModel.uiState.collectAsState()

    // limpa ao sair
    DisposableEffect(Unit) {
        onDispose { viewModel.clearError(); viewModel.clearPasswordResetSent() }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        IconButton(onClick = onNavigateBack, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(Icons.Filled.ArrowBack, tint = Color.White, contentDescription = null)
        }
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
                "Esqueci minha senha",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Digite seu email para receber instruções",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB0BEC5),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
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
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.sendPasswordReset(email.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.large),
                enabled = !ui.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                if (ui.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Enviar", color = Color(0xFF0A2535))
            }

            Spacer(Modifier.height(16.dp))
            if (ui.passwordResetSent) {
                Text("Email enviado! Confira sua caixa de entrada.",
                    color = Color(0xFFB0BEC5), textAlign = TextAlign.Center)
            }
            ui.error?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            }
        }
    }
}
