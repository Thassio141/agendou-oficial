package br.com.agendou.ui.screens.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.domain.enums.BookingStatus
import br.com.agendou.domain.model.Booking
import br.com.agendou.ui.viewmodels.BookingViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    clientId: String,
    onNavigateToProfile: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    
    val tabs = listOf("Próximos", "Histórico")

    LaunchedEffect(clientId) {
        viewModel.loadBookingsForClient(clientId)
    }

    val currentDate = Timestamp.now()
    val upcomingBookings = uiState.bookings.filter { booking ->
        booking.startTime.seconds >= currentDate.seconds && 
        booking.status != BookingStatus.CANCELLED
    }
    
    val pastBookings = uiState.bookings.filter { booking ->
        booking.startTime.seconds < currentDate.seconds ||
        booking.status == BookingStatus.CANCELLED
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meus Agendamentos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                IconButton(onClick = onNavigateToProfile) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White
                    )
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color(0xFF0A2535),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF0A2535)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0A2535))
            }
        } else {
            val bookingsToShow = if (selectedTab == 0) upcomingBookings else pastBookings
            
            if (bookingsToShow.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (selectedTab == 0) "Nenhum agendamento próximo" else "Nenhum histórico",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookingsToShow) { booking ->
                        BookingCard(
                            booking = booking,
                            onCancelClick = { 
                                if (selectedTab == 0) {
                                    viewModel.cancelBooking(booking.id)
                                }
                            },
                            showCancelButton = selectedTab == 0 && booking.status == BookingStatus.PENDING
                        )
                    }
                }
            }
        }

        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Mostrar snackbar ou toast com erro
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: Booking,
    onCancelClick: () -> Unit,
    showCancelButton: Boolean
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
    
    val startDate = booking.startTime.toDate()
    val endDate = booking.endTime.toDate()
    
    val statusColor = when (booking.status) {
        BookingStatus.PENDING -> Color(0xFFFFA726)
        BookingStatus.CONFIRMED -> Color(0xFF66BB6A)
        BookingStatus.CANCELLED -> Color(0xFFEF5350)
        BookingStatus.COMPLETED -> Color(0xFF42A5F5)
    }
    
    val statusText = when (booking.status) {
        BookingStatus.PENDING -> "Pendente"
        BookingStatus.CONFIRMED -> "Confirmado"
        BookingStatus.CANCELLED -> "Cancelado"
        BookingStatus.COMPLETED -> "Concluído"
    }

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
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agendamento #${booking.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A2535)
                )
                
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Service info (placeholder - você precisará buscar os dados do serviço)
            Text(
                text = "Serviço: ${booking.serviceId}", // TODO: Buscar nome do serviço
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Profissional: ${booking.professionalId}", // TODO: Buscar nome do profissional
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF0A2535)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateFormat.format(startDate),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0A2535)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF0A2535)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${timeFormat.format(startDate)} - ${timeFormat.format(endDate)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0A2535)
                    )
                }
            }

            // Cancel button
            if (showCancelButton) {
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEF5350)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFFEF5350), Color(0xFFEF5350)))
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancelar Agendamento",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
} 