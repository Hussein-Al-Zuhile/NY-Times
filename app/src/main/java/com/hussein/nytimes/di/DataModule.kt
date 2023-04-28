package com.hussein.nytimes.di

import com.hussein.nytimes.data.remote.RemoteDataSource
import com.hussein.nytimes.data.remote.RemoteService
import com.hussein.nytimes.data.repositories.TopicsRepository
import com.hussein.nytimes.data.repositories.TopicsRepositoryImpl
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinder {

    @Binds
    abstract fun bindTopicsRepository(topicsRepositoryImpl: TopicsRepositoryImpl): TopicsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataModuleProvider {

    @Provides
    fun providesRemoteService(remoteDataSource: RemoteDataSource): RemoteService =
        remoteDataSource.remoteService

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()
}