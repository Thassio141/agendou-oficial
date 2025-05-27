package br.com.agendou.di

import br.com.agendou.domain.usecases.auth.*
import br.com.agendou.domain.usecases.booking.*
import br.com.agendou.domain.usecases.profession.*
import br.com.agendou.domain.usecases.review.*
import br.com.agendou.domain.usecases.schedule.*
import br.com.agendou.domain.usecases.service.*
import br.com.agendou.domain.usecases.user.*
import br.com.agendou.ui.viewmodels.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    
    @Provides
    @ViewModelScoped
    fun provideAuthViewModel(
        signInUseCase: SignInUseCase,
        signUpUseCase: SignUpUseCase,
        sendPasswordResetUseCase: SendPasswordResetUseCase,
        signOutUseCase: SignOutUseCase,
        getUserUseCase: GetUserUseCase,
        getAuthStateUseCase: GetAuthStateUseCase
    ): AuthViewModel = AuthViewModel(
        signInUseCase,
        signUpUseCase,
        sendPasswordResetUseCase,
        signOutUseCase,
        getUserUseCase,
        getAuthStateUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideUserViewModel(
        getUserUseCase: GetUserUseCase,
        createUserUseCase: CreateUserUseCase,
        updatePhoneNumberUseCase: UpdatePhoneNumberUseCase,
        getProfessionalsUseCase : GetProfessionalsUseCase
    ): UserViewModel = UserViewModel(
        getUserUseCase,
        createUserUseCase,
        updatePhoneNumberUseCase,
        getProfessionalsUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideBookingViewModel(
        getBookingsUseCase: GetBookingsForProfessionalOnDateUseCase,
        scheduleBookingUseCase: ScheduleBookingUseCase,
        cancelBookingUseCase: CancelBookingUseCase,
        checkAvailabilityUseCase: CheckAvailabilityUseCase
    ): BookingViewModel = BookingViewModel(
        getBookingsUseCase,
        scheduleBookingUseCase,
        cancelBookingUseCase,
        checkAvailabilityUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideServiceViewModel(
        getServicesUseCase: GetServicesForProfessionalUseCase,
        createServiceUseCase: CreateServiceUseCase,
        deleteServiceUseCase: DeleteServiceUseCase
    ): ServiceViewModel = ServiceViewModel(
        getServicesUseCase,
        createServiceUseCase,
        deleteServiceUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideScheduleViewModel(
        getWorkSchedulesUseCase: GetWorkSchedulesUseCase,
        saveWorkScheduleUseCase: SaveWorkScheduleUseCase
    ): ScheduleViewModel = ScheduleViewModel(
        getWorkSchedulesUseCase,
        saveWorkScheduleUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideReviewViewModel(
        getReviewsUseCase: GetReviewsForProfessionalUseCase,
        saveReviewUseCase: SaveReviewUseCase
    ): ReviewViewModel = ReviewViewModel(
        getReviewsUseCase,
        saveReviewUseCase
    )
    
    @Provides
    @ViewModelScoped
    fun provideProfessionViewModel(
        getAllProfessionsUseCase: GetAllProfessionsUseCase
    ): ProfessionViewModel = ProfessionViewModel(
        getAllProfessionsUseCase
    )
} 