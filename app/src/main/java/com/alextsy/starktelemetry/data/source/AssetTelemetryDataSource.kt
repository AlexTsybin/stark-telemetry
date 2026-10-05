package com.alextsy.starktelemetry.data.source

import android.content.Context
import com.alextsy.starktelemetry.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AssetTelemetryDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TelemetryDataSource {
    override suspend fun readSnapshot(): String = withContext(ioDispatcher) {
        context.assets.open(ASSET_NAME).bufferedReader().use { it.readText() }
    }

    private companion object {
        const val ASSET_NAME = "telemetry_snapshot.json"
    }
}
