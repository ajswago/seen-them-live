package com.swago.seenthemlive.data.di

import com.swago.seenthemlive.data.repository.FirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkFirebaseRepository
import com.swago.seenthemlive.data.repository.NetworkSetlistFmRepository
import com.swago.seenthemlive.data.repository.SetlistFmRepository
import com.swago.seenthemlive.data.util.ConnectivityManagerNetworkMonitor
import com.swago.seenthemlive.data.util.NetworkMonitor
import com.swago.seenthemlive.network.FakeFirebaseApiService
import com.swago.seenthemlive.network.FakeSetlistFmApiService
import com.swago.seenthemlive.network.FirebaseApiService
import com.swago.seenthemlive.network.SetlistFmApiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun bindsFirebaseRepository(
        firebaseRepository: NetworkFirebaseRepository
    ): FirebaseRepository

    @Binds
    internal abstract fun bindsFirebaseApiService(
        firebaseApiService: FakeFirebaseApiService
    ): FirebaseApiService

    @Binds
    internal abstract fun bindsSetlistFmRepository(
        setlistFmRepository: NetworkSetlistFmRepository
    ): SetlistFmRepository

    @Binds
    internal abstract fun bindsSetlistFmApiService(
        setlistFmApiService: FakeSetlistFmApiService
    ): SetlistFmApiService
}
