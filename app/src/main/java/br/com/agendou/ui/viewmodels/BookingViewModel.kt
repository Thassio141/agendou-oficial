package br.com.agendou.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.model.Booking
import br.com.agendou.domain.repository.ScheduleBookingRequest
import br.com.agendou.domain.usecases.booking.CancelBookingUseCase
import br.com.agendou.domain.usecases.booking.CheckAvailabilityUseCase
import br.com.agendou.domain.usecases.booking.GetBookingsForProfessionalOnDateUseCase
import br.com.agendou.domain.usecases.booking.ScheduleBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsForProfessionalOnDateUseCase,
    private val scheduleBookingUseCase: ScheduleBookingUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val checkAvailabilityUseCase: CheckAvailabilityUseCase
) : ViewModel() {

    private val _bookingsState = MutableStateFlow<BookingsState>(BookingsState.Initial)
    val bookingsState: StateFlow<BookingsState> = _bookingsState

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Initial)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState

    private val _availabilityState = MutableStateFlow<AvailabilityState>(AvailabilityState.Initial)
    val availabilityState: StateFlow<AvailabilityState> = _availabilityState

    fun getBookingsForProfessional(professionalId: String, date: LocalDateTime) {
        viewModelScope.launch {
            _bookingsState.value = BookingsState.Loading
            try {
                val bookings = getBookingsUseCase(professionalId, date)
                _bookingsState.value = BookingsState.Success(bookings)
            } catch (e: Exception) {
                _bookingsState.value = BookingsState.Error(e.message ?: "Erro ao buscar agendamentos")
            }
        }
    }

    fun scheduleBooking(request: ScheduleBookingRequest) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            try {
                val booking = scheduleBookingUseCase(request)
                _scheduleState.value = ScheduleState.Success(booking)
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error(e.message ?: "Erro ao agendar serviço")
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            try {
                cancelBookingUseCase(bookingId)
                _scheduleState.value = ScheduleState.Canceled
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error(e.message ?: "Erro ao cancelar agendamento")
            }
        }
    }

    fun checkAvailability(
        professionalId: String,
        date: LocalDateTime,
        startTime: LocalTime,
        endTime: LocalTime
    ) {
        viewModelScope.launch {
            _availabilityState.value = AvailabilityState.Loading
            try {
                val isAvailable = checkAvailabilityUseCase(professionalId, date, startTime, endTime)
                _availabilityState.value = AvailabilityState.Result(isAvailable)
            } catch (e: Exception) {
                _availabilityState.value = AvailabilityState.Error(e.message ?: "Erro ao verificar disponibilidade")
            }
        }
    }

    sealed class BookingsState {
        object Initial : BookingsState()
        object Loading : BookingsState()
        data class Success(val bookings: List<Booking>) : BookingsState()
        data class Error(val message: String) : BookingsState()
    }

    sealed class ScheduleState {
        object Initial : ScheduleState()
        object Loading : ScheduleState()
        data class Success(val booking: Booking) : ScheduleState()
        object Canceled : ScheduleState()
        data class Error(val message: String) : ScheduleState()
    }

    sealed class AvailabilityState {
        object Initial : AvailabilityState()
        object Loading : AvailabilityState()
        data class Result(val isAvailable: Boolean) : AvailabilityState()
        data class Error(val message: String) : AvailabilityState()
    }
} 