package com.alextsy.starktelemetry.data.repository

import com.alextsy.starktelemetry.data.mapper.toDomain
import com.alextsy.starktelemetry.data.parser.TelemetryJsonParser
import com.alextsy.starktelemetry.data.source.TelemetryDataSource
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import com.alextsy.starktelemetry.domain.repository.TelemetryRepository
import java.io.IOException
import java.time.DateTimeException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

class DefaultTelemetryRepository @Inject constructor(
    private val dataSource: TelemetryDataSource,
    private val parser: TelemetryJsonParser,
) : TelemetryRepository {
    override suspend fun loadSnapshot(): TelemetryLoadResult {
        return try {
            val dto = parser.parse(dataSource.readSnapshot())
                ?: return TelemetryLoadResult.Empty
            TelemetryLoadResult.Success(dto.toDomain())
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: IOException) {
            TelemetryLoadResult.Failure(TelemetryFailure.SOURCE_UNAVAILABLE)
        } catch (exception: SerializationException) {
            TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA)
        } catch (exception: DateTimeException) {
            TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA)
        } catch (exception: Exception) {
            TelemetryLoadResult.Failure(TelemetryFailure.UNKNOWN)
        }
    }
}
