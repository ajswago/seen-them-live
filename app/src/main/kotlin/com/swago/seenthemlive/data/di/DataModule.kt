package com.swago.seenthemlive.data.di

import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkFirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkSetlistFmRepository
import com.swago.seenthemlive.data.repository.NetworkSpotifyRepository
import com.swago.seenthemlive.data.repository.SetlistFmRepository
import com.swago.seenthemlive.data.repository.SpotifyRepository
import com.swago.seenthemlive.data.util.ConnectivityManagerNetworkMonitor
import com.swago.seenthemlive.data.util.NetworkMonitor
import com.swago.seenthemlive.network.FirebaseApiService
import com.swago.seenthemlive.network.NetworkFirebaseApiService
import com.swago.seenthemlive.network.NetworkSetlistFmApiService
import com.swago.seenthemlive.network.NetworkSpotifyApiService
import com.swago.seenthemlive.network.SetlistFmApiService
import com.swago.seenthemlive.network.SpotifyApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    @Singleton
    internal abstract fun bindsFirebaseRepository(
        firebaseRepository: NetworkFirebaseRepository
    ): FirebaseRepository

    @Binds
    @Singleton
    internal abstract fun bindsFirebaseApiService(
        firebaseApiService: NetworkFirebaseApiService
    ): FirebaseApiService

    @Binds
    internal abstract fun bindsSetlistFmRepository(
        setlistFmRepository: NetworkSetlistFmRepository
    ): SetlistFmRepository

    @Binds
    internal abstract fun bindsSetlistFmApiService(
        setlistFmApiService: NetworkSetlistFmApiService
    ): SetlistFmApiService

    @Binds
    internal abstract fun bindsSpotifyRepository(
        spotifyRepository: NetworkSpotifyRepository
    ): SpotifyRepository

    @Binds
    internal abstract fun bindsSpotifyApiService(
        spotifyApiService: NetworkSpotifyApiService
    ): SpotifyApiService

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
            return firestore
        }
    }
}
