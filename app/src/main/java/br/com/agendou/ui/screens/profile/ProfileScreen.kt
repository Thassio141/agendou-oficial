package br.com.agendou.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.domain.model.User
import br.com.agendou.ui.viewmodels.AuthViewModel
import br.com.agendou.ui.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: User?,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(currentUser?.name ?: "") }
    var editedPhone by remember { mutableStateOf(currentUser?.phoneNumber ?: "") }
    
    val userState by userViewModel.userState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            editedName = it.name
            editedPhone = it.phoneNumber ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A2535), Color(0xFF13425A))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Meu Perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { isEditing = !isEditing }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = if (isEditing) "Cancelar" else "Editar",
                        tint = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Picture Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFF0A2535), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentUser?.profilePictureUrl != null) {
                            // TODO: Implementar carregamento de imagem
                            Text(
                                text = currentUser.name.first().uppercase(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = currentUser?.name?.first()?.uppercase() ?: "U",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentUser?.name ?: "Usuário",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A2535)
                    )

                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            // User Information Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Informações Pessoais",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A2535)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name Field
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Nome") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Field (read-only)
                    OutlinedTextField(
                        value = currentUser?.email ?: "",
                        onValueChange = { },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Field
                    OutlinedTextField(
                        value = editedPhone,
                        onValueChange = { editedPhone = it },
                        label = { Text("Telefone") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null)
                        },
                        enabled = isEditing,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray
                        )
                    )

                    // Save Button
                    if (isEditing) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                currentUser?.let { user ->
                                    userViewModel.updatePhoneNumber(user.id, editedPhone.ifBlank { null })
                                    isEditing = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A2535)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !userState.let { it is UserViewModel.UserState.Loading }
                        ) {
                            if (userState is UserViewModel.UserState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "Salvar Alterações",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Account Actions Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Conta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A2535)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Logout Button
                    OutlinedButton(
                        onClick = {
                            authViewModel.signOut()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF5350)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFEF5350), Color(0xFFEF5350)))
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !authState.isLoading
                    ) {
                        if (authState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFFEF5350)
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sair da Conta",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Handle state changes
        LaunchedEffect(userState) {
            when (userState) {
                is UserViewModel.UserState.Success -> {
                    // Atualizar dados locais se necessário
                }
                is UserViewModel.UserState.Error -> {
                    // Mostrar erro
                }
                else -> {}
            }
        }
    }
} 