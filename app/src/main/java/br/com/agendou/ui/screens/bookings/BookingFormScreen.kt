package br.com.agendou.ui.screens.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.domain.model.Service
import br.com.agendou.domain.repository.ScheduleBookingRequest
import br.com.agendou.ui.viewmodels.BookingViewModel
import br.com.agendou.ui.viewmodels.ServiceViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFormScreen(
    professionalId: String,
    onNavigateBack: () -> Unit,
    onBookingSuccess: () -> Unit,
    bookingViewModel: BookingViewModel = hiltViewModel(),
    serviceViewModel: ServiceViewModel = hiltViewModel()
) {
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val serviceUiState by serviceViewModel.uiState.collectAsState()
    val bookingUiState by bookingViewModel.scheduleState.collectAsState()
    val availabilityState by bookingViewModel.availabilityState.collectAsState()

    LaunchedEffect(professionalId) {
        serviceViewModel.loadServicesForProfessional(professionalId)
    }

    // Observar sucesso do agendamento
    LaunchedEffect(bookingUiState) {
        if (bookingUiState is BookingViewModel.ScheduleState.Success) {
            onBookingSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header com botão de voltar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Novo Agendamento",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Seleção de Serviço
        Text(
            text = "Selecione o Serviço",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when {
            serviceUiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            serviceUiState.error != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = serviceUiState.error!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(serviceUiState.services) { service ->
                        ServiceCard(
                            service = service,
                            isSelected = selectedService?.id == service.id,
                            onSelect = { selectedService = service }
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Seleção de Data
        Text(
            text = "Selecione a Data",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
                    ?: "Escolher data"
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Seleção de Horário
        Text(
            text = "Selecione o Horário",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDate != null
        ) {
            Icon(Icons.Default.Schedule, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) 
                    ?: "Escolher horário"
            )
        }
        
        // Verificação de disponibilidade
        if (selectedDate != null && selectedTime != null && selectedService != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            LaunchedEffect(selectedDate, selectedTime, selectedService) {
                val endTime = selectedTime!!.plusMinutes(selectedService!!.duration.toLong())
                bookingViewModel.checkAvailability(
                    professionalId = professionalId,
                    date = LocalDateTime.of(selectedDate!!, selectedTime!!),
                    startTime = selectedTime!!,
                    endTime = endTime
                )
            }
            
            when (availabilityState) {
                is BookingViewModel.AvailabilityState.Loading -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verificando disponibilidade...")
                    }
                }
                
                is BookingViewModel.AvailabilityState.Result -> {
                    val result = availabilityState as BookingViewModel.AvailabilityState.Result
                    if (result.isAvailable) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "✓ Horário disponível",
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "✗ Horário indisponível",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                is BookingViewModel.AvailabilityState.Error -> {
                    val error = availabilityState as BookingViewModel.AvailabilityState.Error
                    Text(
                        text = "Erro ao verificar disponibilidade: ${error.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                else -> {}
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Botão de Agendar
        val canSchedule = selectedService != null && 
                         selectedDate != null && 
                         selectedTime != null &&
                         availabilityState is BookingViewModel.AvailabilityState.Result &&
                         (availabilityState as BookingViewModel.AvailabilityState.Result).isAvailable
        
        Button(
            onClick = {
                if (canSchedule) {
                    val request = ScheduleBookingRequest(
                        clientId = "current_user_id", // TODO: Obter do usuário logado
                        professionalId = professionalId,
                        serviceId = selectedService!!.id,
                        startTime = LocalDateTime.of(selectedDate!!, selectedTime!!),
                        endTime = LocalDateTime.of(selectedDate!!, selectedTime!!.plusMinutes(selectedService!!.duration.toLong()))
                    )
                    bookingViewModel.scheduleBooking(request)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canSchedule && bookingUiState !is BookingViewModel.ScheduleState.Loading
        ) {
            if (bookingUiState is BookingViewModel.ScheduleState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Confirmar Agendamento")
        }
        
        // Mostrar erro de agendamento
        if (bookingUiState is BookingViewModel.ScheduleState.Error) {
            val error = bookingUiState as BookingViewModel.ScheduleState.Error
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    
    // Date Picker Dialog (placeholder - implementar com DatePickerDialog)
    if (showDatePicker) {
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Selecionar Data") },
            text = { Text("Implementar DatePicker aqui") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = LocalDate.now().plusDays(1) // Placeholder
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Time Picker Dialog (placeholder - implementar com TimePickerDialog)
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Selecionar Horário") },
            text = { Text("Implementar TimePicker aqui") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedTime = LocalTime.of(14, 0) // Placeholder
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ServiceCard(
    service: Service,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) 
            CardDefaults.outlinedCardBorder().copy(width = 2.dp) 
        else 
            null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = service.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "R$ ${String.format("%.2f", service.price)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "${service.duration} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 