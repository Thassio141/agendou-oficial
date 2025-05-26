package br.com.agendou.di

import br.com.agendou.data.datasource.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideUserDataSource(firestore: FirebaseFirestore) =
        FirestoreUserDataSource(firestore)
        
    @Provides @Singleton
    fun provideBookingDataSource(firestore: FirebaseFirestore) =
        FirestoreBookingDataSource(firestore)
        
    @Provides @Singleton
    fun provideServiceDataSource(firestore: FirebaseFirestore) =
        FirestoreServiceDataSource(firestore)
        
    @Provides @Singleton
    fun provideWorkScheduleDataSource(firestore: FirebaseFirestore) =
        FirestoreWorkScheduleDataSource(firestore)
        
    @Provides @Singleton
    fun provideReviewDataSource(firestore: FirebaseFirestore) =
        FirestoreReviewDataSource(firestore)
        
    @Provides @Singleton
    fun provideProfessionDataSource(firestore: FirebaseFirestore) =
        FirestoreProfessionDataSource(firestore)
}