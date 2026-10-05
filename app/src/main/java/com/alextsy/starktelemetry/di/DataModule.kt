package com.alextsy.starktelemetry.di

import com.alextsy.starktelemetry.data.repository.DefaultTelemetryRepository
import com.alextsy.starktelemetry.data.source.AssetTelemetryDataSource
import com.alextsy.starktelemetry.data.source.TelemetryDataSource
import com.alextsy.starktelemetry.domain.repository.TelemetryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindTelemetryDataSource(
        implementation: AssetTelemetryDataSource,
    ): TelemetryDataSource

    @Binds
    @Singleton
    abstract fun bindTelemetryRepository(
        implementation: DefaultTelemetryRepository,
    ): TelemetryRepository

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
}
