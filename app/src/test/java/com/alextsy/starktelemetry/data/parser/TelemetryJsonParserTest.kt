package com.alextsy.starktelemetry.data.parser

import com.alextsy.starktelemetry.TestFixtures
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class TelemetryJsonParserTest {
    private val parser = TelemetryJsonParser(
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        },
    )

    @Test
    fun `parse maps exact snapshot structure`() {
        val result = requireNotNull(parser.parse(TestFixtures.validJson))

        assertEquals("Stark VARG MX 1.2", result.bike.model)
        assertEquals("3.4.1", result.bike.firmwareVersion)
        assertEquals(73, result.battery.stateOfChargePercent)
        assertEquals(52.4, result.motor.powerHp, 0.0)
        assertEquals("enduro", result.rideSettings.powerMap)
        assertEquals(3742, result.session.durationSeconds)
        assertEquals("W_MOT_TEMP_HIGH", result.diagnostics.warnings.single().code)
    }

    @Test
    fun `parse ignores unknown fields`() {
        val jsonWithUnknownField = TestFixtures.validJson.replaceFirst(
            "\"timestamp\"",
            "\"future_field\": true, \"timestamp\"",
        )

        val result = parser.parse(jsonWithUnknownField)

        assertEquals("Stark VARG MX 1.2", result?.bike?.model)
    }

    @Test
    fun `parse returns null for blank input`() {
        assertNull(parser.parse("  \n "))
    }

    @Test
    fun `parse rejects malformed json`() {
        assertThrows(SerializationException::class.java) {
            parser.parse("{not-json}")
        }
    }

    @Test
    fun `parse rejects array root`() {
        assertThrows(SerializationException::class.java) {
            parser.parse("[${TestFixtures.validJson}]")
        }
    }

    @Test
    fun `parse rejects missing required field`() {
        val missingBattery = TestFixtures.validJson.replace(
            Regex("\\s*\"battery\": \\{.*?},\\n", RegexOption.DOT_MATCHES_ALL),
            "\n",
        )

        assertThrows(SerializationException::class.java) {
            parser.parse(missingBattery)
        }
    }

    @Test
    fun `parse rejects incorrect field type`() {
        val invalidType = TestFixtures.validJson.replace(
            "\"state_of_charge_pct\": 73",
            "\"state_of_charge_pct\": \"high\"",
        )

        assertThrows(SerializationException::class.java) {
            parser.parse(invalidType)
        }
    }
}
