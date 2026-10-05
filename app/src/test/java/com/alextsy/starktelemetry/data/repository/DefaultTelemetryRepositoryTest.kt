package com.alextsy.starktelemetry.data.repository

import com.alextsy.starktelemetry.TestFixtures
import com.alextsy.starktelemetry.data.parser.TelemetryJsonParser
import com.alextsy.starktelemetry.data.source.TelemetryDataSource
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import java.io.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultTelemetryRepositoryTest {
    private val parser = TelemetryJsonParser(Json { ignoreUnknownKeys = true })

    @Test
    fun `valid source returns domain snapshot`() = runTest {
        val repository = repositoryWith { TestFixtures.validJson }

        val result = repository.loadSnapshot()

        assertTrue(result is TelemetryLoadResult.Success)
        assertEquals(
            TestFixtures.validSnapshot,
            (result as TelemetryLoadResult.Success).snapshot,
        )
    }

    @Test
    fun `blank source returns empty`() = runTest {
        val result = repositoryWith { "  " }.loadSnapshot()

        assertEquals(TelemetryLoadResult.Empty, result)
    }

    @Test
    fun `unavailable source maps to source failure`() = runTest {
        val result = repositoryWith { throw IOException("missing") }.loadSnapshot()

        assertEquals(
            TelemetryLoadResult.Failure(TelemetryFailure.SOURCE_UNAVAILABLE),
            result,
        )
    }

    @Test
    fun `invalid json maps to invalid data failure`() = runTest {
        val result = repositoryWith { "[]" }.loadSnapshot()

        assertEquals(
            TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA),
            result,
        )
    }

    private fun repositoryWith(read: suspend () -> String): DefaultTelemetryRepository {
        val source = object : TelemetryDataSource {
            override suspend fun readSnapshot(): String = read()
        }
        return DefaultTelemetryRepository(source, parser)
    }
}
