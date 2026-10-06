package com.alextsy.starktelemetry.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.alextsy.starktelemetry.presentation.dashboard.BatteryUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DiagnosticsUiModel
import com.alextsy.starktelemetry.presentation.dashboard.WarningSeverityUi
import com.alextsy.starktelemetry.ui.theme.CockpitNavy
import com.alextsy.starktelemetry.ui.theme.StarkRed
import com.alextsy.starktelemetry.ui.theme.TelemetryCard
import com.alextsy.starktelemetry.ui.theme.TelemetryDivider
import com.alextsy.starktelemetry.ui.theme.TelemetryMuted
import com.alextsy.starktelemetry.ui.theme.WarningAmber

@Composable
fun BikeHero(
    model: String,
    variantAndFirmware: String,
    updatedAt: String,
    imageUrl: String,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val heroHeight = if (compact) 200.dp else 104.dp
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(heroHeight),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CockpitNavy),
    ) {
        if (compact) {
            Column(Modifier.fillMaxSize()) {
                HeroCopy(
                    model = model,
                    variantAndFirmware = variantAndFirmware,
                    updatedAt = updatedAt,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                )
                HeroImage(
                    model = model,
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }
        } else {
            Row(Modifier.fillMaxSize()) {
                HeroCopy(
                    model = model,
                    variantAndFirmware = variantAndFirmware,
                    updatedAt = updatedAt,
                    modifier = Modifier
                        .weight(0.46f)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                )
                HeroImage(
                    model = model,
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .weight(0.54f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun HeroCopy(
    model: String,
    variantAndFirmware: String,
    updatedAt: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "STARK FUTURE",
            style = MaterialTheme.typography.labelMedium,
            color = StarkRed,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = model,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(1.dp))
        Text(
            text = variantAndFirmware,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFFCBD5E1),
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = updatedAt,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HeroImage(
    model: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is coil3.compose.AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
            else -> HeroImageFallback(model = model)
        }
    }
}

@Composable
private fun HeroImageFallback(model: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF101D31), Color(0xFF281016), CockpitNavy),
                ),
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = model.substringAfter("Stark ").uppercase(),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White.copy(alpha = 0.18f),
        )
    }
}

@Composable
fun DiagnosticsPanel(
    diagnostics: DiagnosticsUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("diagnostics"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5E5)),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            diagnostics.warnings.forEachIndexed { index, warning ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = TelemetryDivider,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = warning.severity.color(),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = warning.message,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            if (diagnostics.faultCount > 0) {
                Text(
                    text = if (diagnostics.faultCount == 1) {
                        "Bike fault detected"
                    } else {
                        "${diagnostics.faultCount} bike faults detected"
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = StarkRed,
                )
            }
        }
    }
}

@Composable
fun BatteryCard(
    battery: BatteryUiModel,
    modifier: Modifier = Modifier,
) {
    TelemetryCardContainer(modifier) {
        CardHeader(Icons.Outlined.BatteryFull, "BATTERY")
        Spacer(Modifier.height(12.dp))
        Text(
            text = battery.percentage,
            style = MaterialTheme.typography.headlineLarge,
            color = CockpitNavy,
        )
        Text(
            text = battery.chargingState.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = TelemetryMuted,
        )
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { battery.percentageValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = StarkRed,
            trackColor = TelemetryDivider,
        )
        Spacer(Modifier.height(12.dp))
        MetricRows(
            metrics = listOf(
                "EST. RANGE" to battery.estimatedRange,
                "TEMPERATURE" to battery.temperature,
            ),
        )
    }
}

@Composable
fun MetricCard(
    icon: ImageVector,
    title: String,
    primaryValue: String,
    primaryLabel: String,
    metrics: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    TelemetryCardContainer(modifier) {
        CardHeader(icon, title)
        Spacer(Modifier.height(12.dp))
        Text(
            text = primaryValue,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = CockpitNavy,
            maxLines = 1,
        )
        Text(
            text = primaryLabel,
            style = MaterialTheme.typography.labelMedium,
            color = TelemetryMuted,
        )
        Spacer(Modifier.height(12.dp))
        MetricRows(metrics)
    }
}

@Composable
private fun TelemetryCardContainer(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 172.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = TelemetryCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content,
        )
    }
}

@Composable
private fun CardHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = StarkRed,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = CockpitNavy,
        )
    }
}

@Composable
private fun MetricRows(metrics: List<Pair<String, String>>) {
    metrics.forEach { (label, value) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TelemetryMuted,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = CockpitNavy,
            )
        }
    }
}

private fun WarningSeverityUi.color(): Color = when (this) {
    WarningSeverityUi.INFO -> Color(0xFF2563EB)
    WarningSeverityUi.WARNING -> WarningAmber
    WarningSeverityUi.CRITICAL -> StarkRed
    WarningSeverityUi.UNKNOWN -> TelemetryMuted
}
