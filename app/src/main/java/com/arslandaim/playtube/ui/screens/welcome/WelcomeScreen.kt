package com.arslandaim.playtube.ui.screens.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arslandaim.playtube.R
import com.arslandaim.playtube.ui.components.PremiumButton
import com.arslandaim.playtube.ui.components.PremiumGold
import com.arslandaim.playtube.ui.components.premiumBrush
import com.arslandaim.playtube.ui.components.premiumFloat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

private class WelcomeStar(val x: Float, val y: Float, val radiusDp: Float, val phase: Float)

/** Fade + slide-up used for the staggered reveal of each block. */
@Composable
private fun Modifier.reveal(show: Boolean): Modifier {
    val p by animateFloatAsState(if (show) 1f else 0f, tween(550, easing = FastOutSlowInEasing), label = "reveal")
    return this.graphicsLayer {
        alpha = p
        translationY = (1f - p) * 28.dp.toPx()
    }
}

/**
 * Animated welcome screen with the Mathra logo.
 *
 * - First launch ([isFirstTime] = true): waits for the "Get Started" button.
 * - Later launches: a short splash with a progress bar that closes itself (tap to skip).
 */
@Composable
fun WelcomeScreen(isFirstTime: Boolean, onFinished: () -> Unit) {
    val c = MaterialTheme.colorScheme
    val primary = c.primary
    val secondary = c.secondary
    val tertiary = c.tertiary

    BackHandler(onBack = onFinished)

    val transition = rememberInfiniteTransition(label = "welcome")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "drift"
    )
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing)),
        label = "spin"
    )
    val glow by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val twinkle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing)),
        label = "twinkle"
    )

    val stars = remember {
        List(48) {
            WelcomeStar(Random.nextFloat(), Random.nextFloat(), Random.nextFloat() * 1.6f + 0.6f, Random.nextFloat())
        }
    }

    val logoScale = remember { Animatable(0.25f) }
    val logoAlpha = remember { Animatable(0f) }
    var stage by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        launch { logoAlpha.animateTo(1f, tween(500)) }
        launch {
            logoScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
        delay(450); stage = 1
        delay(250); stage = 2
        delay(250); stage = 3
        delay(250); stage = 4
    }

    val progress = remember { Animatable(0f) }
    LaunchedEffect(isFirstTime) {
        if (!isFirstTime) {
            progress.animateTo(1f, tween(2800, easing = LinearEasing))
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07070C))
            .drawBehind {
                val w = this.size.width
                val h = this.size.height
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.42f), Color.Transparent),
                        center = Offset(w * (0.15f + 0.6f * drift), h * 0.18f),
                        radius = w * 1.0f
                    )
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(tertiary.copy(alpha = 0.34f), Color.Transparent),
                        center = Offset(w * (0.95f - 0.6f * drift), h * 0.82f),
                        radius = w * 0.95f
                    )
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(secondary.copy(alpha = 0.18f), Color.Transparent),
                        center = Offset(w * 0.5f, h * (0.45f + 0.1f * drift)),
                        radius = w * 0.8f
                    )
                )
                for (s in stars) {
                    val a = 0.2f + 0.8f * abs(sin((twinkle + s.phase) * 2f * PI.toFloat()))
                    drawCircle(
                        color = Color.White.copy(alpha = a * 0.85f),
                        radius = s.radiusDp * density,
                        center = Offset(s.x * w, s.y * h)
                    )
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { if (!isFirstTime) onFinished() }
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with a glowing, rotating gradient ring
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                        alpha = logoAlpha.value
                    }
                    .premiumFloat(5f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(210.dp)) {
                    val cx = this.size.width / 2f
                    val cy = this.size.height / 2f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primary.copy(alpha = 0.55f * glow), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = this.size.width / 2f
                        ),
                        radius = this.size.width / 2f,
                        center = Offset(cx, cy)
                    )
                    rotate(degrees = spin, pivot = Offset(cx, cy)) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(primary, tertiary, secondary, Color.Transparent, primary),
                                center = Offset(cx, cy)
                            ),
                            radius = this.size.width * 0.40f,
                            center = Offset(cx, cy),
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.mathra_logo),
                    contentDescription = "Mathra logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(124.dp)
                        .clip(RoundedCornerShape(36.dp))
                        .border(2.dp, premiumBrush(), RoundedCornerShape(36.dp))
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                "ආයුබෝවන්!  👋",
                modifier = Modifier.reveal(stage >= 1),
                color = PremiumGold,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Welcome to",
                modifier = Modifier.reveal(stage >= 1),
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Mathra PlayTube",
                modifier = Modifier.reveal(stage >= 2),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = Brush.linearGradient(listOf(Color.White, tertiary, primary)),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp
                )
            )
            Spacer(Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .reveal(stage >= 4),
                contentAlignment = Alignment.Center
            ) {
                if (isFirstTime) {
                    PremiumButton(
                        text = "Get Started  →",
                        onClick = onFinished,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = progress.value
                                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                                    }
                                    .clip(CircleShape)
                                    .background(premiumBrush())
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Tap to continue",
                            color = Color.White.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .reveal(stage >= 4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Project by Denuwan Akash",
                color = PremiumGold,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                "Mathra Quotes  •  Made with ♥ in Sri Lanka",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
