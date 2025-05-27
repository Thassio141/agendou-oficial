package br.com.agendou.ui.screens.service

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.domain.model.Service
import br.com.agendou.ui.viewmodels.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceManagementScreen(
    professionalId: String,
    onNavigateBack: () -> Unit,
    viewModel: ServiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showForm by remember { mutableStateOf(false) }
    var editingService by remember { mutableStateOf<Service?>(null) }

    LaunchedEffect(professionalId) {
        viewModel.loadServicesForProfessional(professionalId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meus Serviços") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { editingService = null; showForm = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Novo Serviço")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(uiState.error!!)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.services) { service ->
                        ElevatedCard(onClick = {
                            editingService = service
                            showForm = true
                        }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(service.name, style = MaterialTheme.typography.titleMedium)
                                Text("R$ %.2f".format(service.price))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showForm) {
        ServiceFormBottomSheet(
            professionalId = professionalId,
            initialService = editingService,
            onDismiss = { showForm = false },
            onSave = { svc ->
                if (editingService == null) viewModel.createService(svc) else viewModel.createService(svc)
                showForm = false
            }
        )
    }
} 