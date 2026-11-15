package com.arslandaim.playtube.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Every Premium Studio tool has its own logo: a glossy gradient badge with a
 * dedicated icon and colour pair, so each tool is recognisable at a glance in the
 * tab bar and on the dashboard.
 */
enum class StudioTool(
    val label: String,
    val tagline: String,
    val icon: ImageVector,
    val colorA: Color,
    val colorB: Color
) {
    DASHBOARD("Dashboard", "Everything at a glance", Icons.Filled.Dashboard, Color(0xFFFFB300), Color(0xFFFF6D00)),
    THEMES("Themes", "Mix & match colours", Icons.Filled.Palette, Color(0xFFFF4081), Color(0xFF7C4DFF)),
    BACKGROUND("Background", "Your photo on Home", Icons.Filled.Wallpaper, Color(0xFFD500F9), Color(0xFF304FFE)),
    EFFECTS("Effects", "Falling & floating emojis", Icons.Filled.AutoAwesome, Color(0xFF00E5FF), Color(0xFF2979FF)),
    SOUND("Sound", "Loud & clear boost", Icons.Filled.GraphicEq, Color(0xFF00E676), Color(0xFF00BFA5))
}

/** Glossy gradient logo badge for one [StudioTool]. */
@Composable
fun ToolLogo(
    tool: StudioTool,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    animated: Boolean = true
) {
    val shape = RoundedCornerShape(size * 0.30f)
    val transition = rememberInfiniteTransition(label = "toolLogo")
    val wave by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "toolLogoWave"
    )
    val motion = if (animated) wave else 0f
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                translationY = motion * 1.5.dp.toPx()
                rotationZ = motion * 3f
            }
            .clip(shape)
            .background(Brush.linearGradient(listOf(tool.colorA, tool.colorB)))
            .drawBehind {
                // Soft gloss highlight in the top-left corner.
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent),
                        center = Offset(this.size.width * 0.28f, this.size.height * 0.22f),
                        radius = this.size.width * 0.75f
                    ),
                    radius = this.size.width * 0.75f,
                    center = Offset(this.size.width * 0.28f, this.size.height * 0.22f)
                )
            }
            .border(1.2.dp, Color.White.copy(alpha = 0.45f), shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = tool.icon,
            contentDescription = tool.label,
            tint = Color.White,
            modifier = Modifier.size(size * 0.54f)
        )
    }
}

/** Tab-bar chip: tool logo + name. The selected tab gets a gradient fill. */
@Composable
fun ToolTabChip(
    tool: StudioTool,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sel by animateFloatAsState(if (selected) 1f else 0f, tween(280), label = "toolTabSel")
    Row(
        modifier = modifier
            .height(46.dp)
            .graphicsLayer {
                val sc = 1f + 0.04f * sel
                scaleX = sc
                scaleY = sc
            }
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.07f))
            .background(Brush.linearGradient(listOf(tool.colorA, tool.colorB)), alpha = 0.85f * sel)
            .border(1.dp, lerp(Color.White.copy(alpha = 0.14f), Color.White.copy(alpha = 0.55f), sel), CircleShape)
            .clickable(onClick = onClick)
            .padding(start = 6.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolLogo(tool = tool, size = 34.dp, animated = selected)
        Spacer(Modifier.width(8.dp))
        Text(
            text = tool.label,
            color = Color.White,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

/** Large dashboard card for one tool: logo, current value, live dot and an open arrow. */
@Composable
fun ToolDashboardCard(
    tool: StudioTool,
    value: String,
    subtitle: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    val transition = rememberInfiniteTransition(label = "toolCard")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "toolCardPulse"
    )
    Column(
        modifier = modifier
            .clip(shape)
            .background(Color(0xFF16161D))
            .background(
                Brush.linearGradient(
                    listOf(tool.colorA.copy(alpha = 0.26f), tool.colorB.copy(alpha = 0.08f), Color.Transparent)
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(tool.colorA.copy(alpha = 0.75f), tool.colorB.copy(alpha = 0.25f))),
                shape
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            ToolLogo(tool = tool, size = 50.dp)
            Spacer(Modifier.weight(1f))
            if (active) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .graphicsLayer {
                            val sc = 0.8f + 0.5f * pulse
                            scaleX = sc
                            scaleY = sc
                            alpha = 0.55f + 0.45f * (1f - pulse)
                        }
                        .clip(CircleShape)
                        .background(Color(0xFF3DDC84))
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = tool.label,
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1
        )
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.55f),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = tool.colorA,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
