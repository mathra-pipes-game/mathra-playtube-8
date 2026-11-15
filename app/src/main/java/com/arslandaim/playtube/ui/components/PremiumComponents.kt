package com.arslandaim.playtube.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Mathra PlayTube premium design kit: gradient buttons, chips, cards and badges
 * shared by Home, Settings, About and the sound-boost sheet.
 */

val PremiumGold = Color(0xFFFFC857)
val PremiumGoldDeep = Color(0xFFFF9F1C)

/** Accent gradient built from the user's chosen theme colours. */
@Composable
fun premiumBrush(): Brush = Brush.linearGradient(
    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
)

private val GoldBrush = Brush.linearGradient(listOf(PremiumGold, PremiumGoldDeep))

/** Filled gradient button. */
@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(premiumBrush())
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/** Outlined, glassy button for secondary actions. */
@Composable
fun PremiumOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/** Pill chip; the selected one fades into a gradient with a soft border. */
@Composable
fun PremiumChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 36.dp
) {
    val sel by animateFloatAsState(if (selected) 1f else 0f, tween(280), label = "chipSel")
    val c = MaterialTheme.colorScheme
    val borderColor = lerp(c.outlineVariant.copy(alpha = 0.45f), Color.White.copy(alpha = 0.25f), sel)
    Box(
        modifier = modifier
            .height(height)
            .graphicsLayer {
                val sc = 1f + 0.04f * sel
                scaleX = sc
                scaleY = sc
            }
            .clip(CircleShape)
            .background(c.surfaceContainerHigh)
            .background(premiumBrush(), alpha = sel)
            .border(1.dp, borderColor, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = lerp(c.onSurface, Color.White, sel),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}

/** Gold "PREMIUM" style badge. */
@Composable
fun PremiumBadge(text: String = "PREMIUM", modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(GoldBrush)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFF3B2600), modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(3.dp))
        Text(
            text = text,
            color = Color(0xFF3B2600),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 9.sp,
            letterSpacing = 0.8.sp
        )
    }
}

/** Rounded gradient-tinted icon tile used in the settings rows. */
@Composable
fun PremiumIconTile(icon: ImageVector, modifier: Modifier = Modifier, tint: Color = MaterialTheme.colorScheme.primary) {
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(tint.copy(alpha = 0.14f))
            .border(1.dp, tint.copy(alpha = 0.28f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

/** Large hero card with a slowly drifting gradient (Settings header, Home banner). */
@Composable
fun PremiumHeroCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(26.dp)
    val c = MaterialTheme.colorScheme
    val c1 = c.primary.copy(alpha = 0.36f)
    val c2 = c.tertiary.copy(alpha = 0.18f)
    val c3 = c.surfaceContainer
    val transition = rememberInfiniteTransition(label = "hero")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse),
        label = "heroShift"
    )
    val clickMod = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .drawBehind {
                val w = size.width
                val h = size.height
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(c1, c2, c3),
                        start = Offset(-w * 0.4f + w * 0.6f * shift, 0f),
                        end = Offset(w * (0.8f + 0.7f * shift), h)
                    )
                )
            }
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(listOf(c.primary.copy(alpha = 0.7f), PremiumGold.copy(alpha = 0.35f)))
                ),
                shape
            )
            .then(clickMod)
            .padding(18.dp),
        content = content
    )
}

/** Fade + slide-up entrance, staggered by [index]. Plays once, not again on scroll. */
@Composable
fun Modifier.premiumEntrance(index: Int = 0): Modifier {
    var played by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (played) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!played) {
            delay(index * 90L)
            progress.animateTo(1f, tween(520, easing = FastOutSlowInEasing))
            played = true
        }
    }
    return this.graphicsLayer {
        val p = progress.value
        alpha = p
        translationY = (1f - p) * 44.dp.toPx()
        val sc = 0.96f + 0.04f * p
        scaleX = sc
        scaleY = sc
    }
}

/** Gentle up/down floating loop, used on logos. */
@Composable
fun Modifier.premiumFloat(amplitudeDp: Float = 3f): Modifier {
    val transition = rememberInfiniteTransition(label = "float")
    val v by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "floatV"
    )
    return this.graphicsLayer { translationY = v * amplitudeDp.dp.toPx() }
}

/** Dashboard stat tile with a pulsing "live" dot when [active]. */
@Composable
fun PremiumDashboardTile(
    icon: ImageVector,
    title: String,
    value: String,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val c = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(22.dp)
    val transition = rememberInfiniteTransition(label = "tile")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    val accent = if (active) c.primary else c.onSurfaceVariant
    Column(
        modifier = modifier
            .clip(shape)
            .background(c.surfaceContainerHigh)
            .background(
                Brush.linearGradient(listOf(accent.copy(alpha = if (active) 0.20f else 0.06f), Color.Transparent))
            )
            .border(1.dp, accent.copy(alpha = if (active) 0.5f else 0.25f), shape)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            PremiumIconTile(icon = icon, tint = accent)
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
        Spacer(Modifier.height(10.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = c.onSurface,
            maxLines = 1
        )
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = c.onSurfaceVariant,
            maxLines = 1
        )
    }
}

/** Small info pill, e.g. "200% Sound". */
@Composable
fun PremiumStatPill(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

/** Selectable row with a radio-like tick, used by the sound-boost sheet. */
@Composable
fun PremiumOptionRow(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    val c = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(shape)
            .background(if (selected) c.primary.copy(alpha = 0.14f) else c.surfaceContainerHigh.copy(alpha = 0.6f))
            .border(
                1.dp,
                if (selected) c.primary.copy(alpha = 0.7f) else c.outlineVariant.copy(alpha = 0.35f),
                shape
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                if (badge != null) {
                    Spacer(Modifier.width(8.dp))
                    PremiumBadge(badge)
                }
            }
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = c.onSurfaceVariant
                )
            }
        }
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(if (selected) premiumBrush() else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)))
                .border(1.5.dp, if (selected) Color.Transparent else c.outline.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

/** "Project by Denuwan Akash" signature footer. */
@Composable
fun PremiumProjectFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Brush.horizontalGradient(listOf(Color.Transparent, PremiumGold.copy(alpha = 0.7f))))
            )
            Icon(
                Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = PremiumGold,
                modifier = Modifier.padding(horizontal = 10.dp).size(20.dp)
            )
            Box(
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Brush.horizontalGradient(listOf(PremiumGold.copy(alpha = 0.7f), Color.Transparent)))
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Project by",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Denuwan Akash",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = PremiumGold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Mathra Quotes  •  Made with ♥ in Sri Lanka",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/** Round glassy icon button with a gradient ring and a press animation (top bar, studio). */
@Composable
fun PremiumIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    active: Boolean = false,
    size: androidx.compose.ui.unit.Dp = 40.dp
) {
    val c = MaterialTheme.colorScheme
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val press by animateFloatAsState(if (pressed) 0.88f else 1f, tween(120), label = "iconPress")
    val ring = Brush.linearGradient(
        listOf(c.primary.copy(alpha = if (active) 1f else 0.75f), c.tertiary.copy(alpha = if (active) 0.9f else 0.45f))
    )
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = press
                scaleY = press
            }
            .clip(CircleShape)
            .background(c.surfaceContainerHigh.copy(alpha = 0.75f))
            .background(tint.copy(alpha = if (active) 0.18f else 0f))
            .border(1.2.dp, ring, CircleShape)
            .clickable(interactionSource = source, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}
