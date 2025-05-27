package br.com.agendou.di

import br.com.agendou.data.datasource.*
import br.com.agendou.data.repository.*
import br.com.agendou.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): AuthRepository = AuthRepositoryImpl(firebaseAuthDataSource)

    @Provides @Singleton
    fun provideUserRepository(
        ds: FirestoreUserDataSource
    ): UserRepository = UserRepositoryImpl(ds)
    
    @Provides @Singleton
    fun provideBookingRepository(
        ds: FirestoreBookingDataSource
    ): BookingRepository = BookingRepositoryImpl(ds)
    
    @Provides @Singleton
    fun provideServiceRepository(
        ds: FirestoreServiceDataSource
    ): ServiceRepository = ServiceRepositoryImpl(ds)
    
    @Provides @Singleton
    fun provideWorkScheduleRepository(
        ds: FirestoreWorkScheduleDataSource
    ): WorkScheduleRepository = WorkScheduleRepositoryImpl(ds)
    
    @Provides @Singleton
    fun provideReviewRepository(
        ds: FirestoreReviewDataSource
    ): ReviewRepository = ReviewRepositoryImpl(ds)
    
    @Provides @Singleton
    fun provideProfessionRepository(
        ds: FirestoreProfessionDataSource
    ): ProfessionRepository = ProfessionRepositoryImpl(ds)
}