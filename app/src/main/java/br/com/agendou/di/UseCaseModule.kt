package br.com.agendou.di

import br.com.agendou.domain.repository.*
import br.com.agendou.domain.usecases.auth.*
import br.com.agendou.domain.usecases.booking.*
import br.com.agendou.domain.usecases.profession.*
import br.com.agendou.domain.usecases.review.*
import br.com.agendou.domain.usecases.schedule.*
import br.com.agendou.domain.usecases.service.*
import br.com.agendou.domain.usecases.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    // Auth use cases
    @Provides @Singleton
    fun provideSignInUseCase(repository: AuthRepository) = SignInUseCase(repository)
    
    @Provides @Singleton
    fun provideSignUpUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ) = SignUpUseCase(authRepository, userRepository)
    
    @Provides @Singleton
    fun provideSendPasswordResetUseCase(repository: AuthRepository) = SendPasswordResetUseCase(repository)
    
    @Provides @Singleton
    fun provideSignOutUseCase(repository: AuthRepository) = SignOutUseCase(repository)
    
    @Provides @Singleton
    fun provideGetAuthStateUseCase(repository: AuthRepository) = GetAuthStateUseCase(repository)
    
    // User use cases
    @Provides @Singleton
    fun provideGetUserUseCase(repository: UserRepository) = GetUserUseCase(repository)
    
    @Provides @Singleton
    fun provideCreateUserUseCase(repository: UserRepository) = CreateUserUseCase(repository)
    
    @Provides @Singleton
    fun provideUpdatePhoneNumberUseCase(repository: UserRepository) = UpdatePhoneNumberUseCase(repository)
    
    @Provides @Singleton
    fun provideGetProfessionalsUseCase(repository: UserRepository) = GetProfessionalsUseCase(repository)
    
    // Booking use cases
    @Provides @Singleton
    fun provideGetBookingsForProfessionalOnDateUseCase(repository: BookingRepository) = 
        GetBookingsForProfessionalOnDateUseCase(repository)
    
    @Provides @Singleton
    fun provideScheduleBookingUseCase(repository: BookingRepository) = ScheduleBookingUseCase(repository)
    
    @Provides @Singleton
    fun provideCancelBookingUseCase(repository: BookingRepository) = CancelBookingUseCase(repository)
    
    @Provides @Singleton
    fun provideCheckAvailabilityUseCase(
        bookingRepository: BookingRepository,
        scheduleRepository: WorkScheduleRepository
    ) = CheckAvailabilityUseCase(bookingRepository, scheduleRepository)
    
    // Service use cases
    @Provides @Singleton
    fun provideGetServicesForProfessionalUseCase(repository: ServiceRepository) = 
        GetServicesForProfessionalUseCase(repository)
    
    @Provides @Singleton
    fun provideCreateServiceUseCase(repository: ServiceRepository) = CreateServiceUseCase(repository)
    
    @Provides @Singleton
    fun provideDeleteServiceUseCase(repository: ServiceRepository) = DeleteServiceUseCase(repository)
    
    // Work schedule use cases
    @Provides @Singleton
    fun provideGetWorkSchedulesUseCase(repository: WorkScheduleRepository) = 
        GetWorkSchedulesUseCase(repository)
    
    @Provides @Singleton
    fun provideSaveWorkScheduleUseCase(repository: WorkScheduleRepository) = 
        SaveWorkScheduleUseCase(repository)
    
    // Review use cases
    @Provides @Singleton
    fun provideGetReviewsForProfessionalUseCase(repository: ReviewRepository) = 
        GetReviewsForProfessionalUseCase(repository)
    
    @Provides @Singleton
    fun provideSaveReviewUseCase(repository: ReviewRepository) = SaveReviewUseCase(repository)
    
    // Profession use cases
    @Provides @Singleton
    fun provideGetAllProfessionsUseCase(repository: ProfessionRepository) = 
        GetAllProfessionsUseCase(repository)
} 