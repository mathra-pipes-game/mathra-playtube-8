/*
 * PlayTube Project Original (2026)
 * arslandaim-hub (GitHub.com/arslandaim-hub)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.platform.LocalDensity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.random.Random

/**
 * Emoji Effect styles the user can pick from in Settings.
 * Each style is a themed set of emojis that fall across the whole app.
 * All styles are free.
 */
enum class EffectMotion { FALL, RISE, DRIFT }

data class EmojiEffectStyle(
    val id: String,
    val displayName: String,
    val emojis: List<String>,
    val premium: Boolean = false,
    /** FALL = top to bottom, RISE = bottom to top (bubbles, balloons), DRIFT = falls while sweeping sideways. */
    val motion: EffectMotion = EffectMotion.FALL
)

val EmojiEffectStyles = listOf(
    EmojiEffectStyle("fire", "Fire", listOf("\uD83D\uDD25", "\u2728")),
    EmojiEffectStyle("hearts", "Hearts", listOf("\u2764\uFE0F", "\uD83D\uDC95", "\uD83D\uDC96")),
    EmojiEffectStyle("stars", "Stars", listOf("\u2B50", "\u2728", "\uD83C\uDF1F")),
    EmojiEffectStyle("notes", "Music Notes", listOf("\uD83C\uDFB5", "\uD83C\uDFB6", "\uD83C\uDFA7")),
    EmojiEffectStyle("rain", "Rain", listOf("\uD83D\uDCA7", "\uD83D\uDCA6")),
    EmojiEffectStyle("confetti", "Confetti", listOf("\uD83C\uDF89", "\uD83C\uDF8A", "\uD83C\uDF88")),
    EmojiEffectStyle("bubbles", "Bubbles", listOf("\uD83E\uDEE7", "\uD83D\uDD35")),
    EmojiEffectStyle("lightning", "Lightning", listOf("\u26A1", "\uD83D\uDCA5")),
    EmojiEffectStyle("snow", "Snow", listOf("\u2744\uFE0F", "\uD83C\uDF28\uFE0F")),
    EmojiEffectStyle("sparkle", "Sparkle", listOf("\uD83C\uDF1F", "\uD83D\uDCAB", "\u2728")),
    EmojiEffectStyle("funny", "Funny", listOf("\uD83D\uDE02", "\uD83E\uDD23", "\uD83D\uDE06", "\uD83D\uDE39", "\uD83D\uDE1D", "\uD83E\uDD2A", "\uD83D\uDE1C", "\uD83E\uDD73", "\uD83D\uDE01", "\uD83D\uDE0B")),
    EmojiEffectStyle("crown", "Royal Crown", listOf("\uD83D\uDC51", "\uD83D\uDC8E", "\u2728")),
    EmojiEffectStyle("diamond", "Diamond Rain", listOf("\uD83D\uDC8E", "\u2728", "\uD83D\uDCAB")),
    EmojiEffectStyle("sakura", "Sakura", listOf("\uD83C\uDF38", "\uD83C\uDF3A", "\uD83D\uDC9E")),
    EmojiEffectStyle("galaxy", "Galaxy", listOf("\uD83C\uDF19", "\u2B50", "\uD83D\uDCAB", "\uD83C\uDF1F")),
    EmojiEffectStyle("money", "Money Rain", listOf("💸", "💰", "🤑")),
    EmojiEffectStyle("party", "Party", listOf("🥳", "🎂", "🎁", "🎉")),
    EmojiEffectStyle("cats", "Cute Cats", listOf("🐱", "😺", "😻", "🐾")),
    EmojiEffectStyle("space", "Space", listOf("🚀", "👽", "🌍", "⭐")),
    EmojiEffectStyle("ocean", "Ocean", listOf("🐠", "🐬", "🐳", "🌊")),
    EmojiEffectStyle("halloween", "Halloween", listOf("🎃", "👻", "🦇")),
    EmojiEffectStyle("flowers", "Flowers", listOf("🌹", "🌷", "🌻", "🌼")),
    EmojiEffectStyle("gaming", "Gaming", listOf("🎮", "👾", "🕹️")),
    EmojiEffectStyle("rainbow", "Rainbow", listOf("🌈", "☀️", "☁️")),
    // ---- New effects ----
    EmojiEffectStyle("balloons", "Balloons", listOf("🎈", "🎈", "🎉", "✨"), motion = EffectMotion.RISE),
    EmojiEffectStyle("fireworks", "Fireworks", listOf("🎆", "🎇", "✨", "💥"), motion = EffectMotion.RISE),
    EmojiEffectStyle("butterflies", "Butterflies", listOf("🦋", "🌸", "🌼"), motion = EffectMotion.DRIFT),
    EmojiEffectStyle("autumn", "Autumn Leaves", listOf("🍂", "🍁", "🍃"), motion = EffectMotion.DRIFT),
    EmojiEffectStyle("magic", "Magic", listOf("🪄", "🔮", "✨", "🌟"), motion = EffectMotion.RISE),
    EmojiEffectStyle("love", "Love Letters", listOf("💌", "💘", "💝", "💗"), motion = EffectMotion.RISE),
    EmojiEffectStyle("candy", "Candy Shower", listOf("🍭", "🍬", "🍩", "🍪")),
    EmojiEffectStyle("icecream", "Ice Cream", listOf("🍦", "🍧", "🍨", "🍓")),
    EmojiEffectStyle("pizza", "Foodie", listOf("🍕", "🍔", "🍟", "🌮")),
    EmojiEffectStyle("sports", "Sports", listOf("⚽", "🏏", "🏀", "🏆")),
    EmojiEffectStyle("dogs", "Happy Dogs", listOf("🐶", "🐕", "🦴", "🐾")),
    EmojiEffectStyle("dragon", "Dragon Fire", listOf("🐉", "🔥", "⚔️", "🛡️")),
    EmojiEffectStyle("avurudu", "Avurudu", listOf("🪔", "🌸", "🎆", "🥁"), motion = EffectMotion.DRIFT),
    EmojiEffectStyle("lanterns", "Lanterns", listOf("🏮", "🪔", "✨"), motion = EffectMotion.RISE),
    EmojiEffectStyle("christmas", "Christmas", listOf("🎄", "🎅", "⛄", "🎁")),
    EmojiEffectStyle("tropical", "Tropical", listOf("🌴", "🥥", "🍍", "🌺"), motion = EffectMotion.DRIFT)
)

fun emojiStyleById(id: String): EmojiEffectStyle =
    EmojiEffectStyles.firstOrNull { it.id == id } ?: EmojiEffectStyles.first()

/** Emoji bitmaps are rendered once at this size and only scaled with a matrix afterwards. */
private const val EMOJI_BITMAP_DP = 56

/** Wait this long after app launch before the first effect starts, so Home can load smoothly. */
private const val STARTUP_DELAY_MS = 1400L

/** After the first run the startup delay is skipped (e.g. when the effect is toggled back on). */
@Volatile private var startupGateOpen = false

/** Skip vsyncs on 90/120 Hz screens: ~60 draws per second is plenty for falling emojis and halves the work. */
private const val MIN_FRAME_S = 0.0145f

private const val TWO_PI = 6.2831855f

private class EmojiParticle {
    /** 0 = just entering the screen edge, 1 = leaving at the opposite edge. */
    var journey = 0f
    var baseX = 0f
    var emojiIndex = 0
    /** 0 = far (small, slow, soft) .. 1 = near (big, quick, bright). Gives a natural sense of depth. */
    var depth = 0f
    var sizeDp = 0f
    var speed = 0f
    var swayAmp = 0f
    var swayFreq = 0f
    var phase = 0f
    var rockDeg = 0f
    var drift = 0f
}

private fun EmojiParticle.randomise(emojiCount: Int) {
    depth = Random.nextFloat()
    sizeDp = 20f + depth * 20f                                   // 20dp..40dp
    speed = 0.14f + depth * 0.14f + Random.nextFloat() * 0.03f   // screen heights per second at 1x
    swayAmp = 0.012f + Random.nextFloat() * 0.030f
    swayFreq = 0.6f + Random.nextFloat() * 0.9f
    phase = Random.nextFloat() * TWO_PI
    rockDeg = 8f + Random.nextFloat() * 18f
    drift = Random.nextFloat() * 2f - 1f
    baseX = Random.nextFloat()
    emojiIndex = Random.nextInt(emojiCount.coerceAtLeast(1))
    // Staggered start above the screen so particles never pop in or bunch up.
    journey = -0.10f - Random.nextFloat() * 0.35f
}

private class EffectSim(count: Int, emojiCount: Int) {
    val particles = Array(count) { i ->
        EmojiParticle().also {
            it.randomise(emojiCount)
            // Spread the first batch evenly over the whole screen.
            it.journey = (i + Random.nextFloat()) / count.coerceAtLeast(1) * 1.1f - 0.05f
        }
    }
    var elapsed = 0.0
}

private fun smooth(x: Float): Float {
    val c = x.coerceIn(0f, 1f)
    return c * c * (3f - 2f * c)
}

/** Draws one emoji into a square bitmap. Done once per emoji, off the main thread. */
private fun renderEmojiBitmap(emoji: String, px: Int): Bitmap {
    val bmp = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
    val c = android.graphics.Canvas(bmp)
    val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = px * 0.78f
    }
    val fm = p.fontMetrics
    c.drawText(emoji, px / 2f, px / 2f - (fm.ascent + fm.descent) / 2f, p)
    return bmp
}

private fun stepSim(
    sim: EffectSim,
    style: EmojiEffectStyle,
    dt: Float,
    speedScale: Float,
    beatReactive: Boolean
) {
    sim.elapsed += dt
    val e = (sim.elapsed % 3600.0).toFloat()
    // Gentle "breathing" of the fall speed (soft -> a little stronger -> soft), never jerky.
    val breathing = if (beatReactive) 1f + 0.12f * sin(e * 0.9f) + 0.06f * sin(e * 2.3f) else 1f
    val motionMul = when (style.motion) {
        EffectMotion.FALL -> 1f
        EffectMotion.RISE -> 0.85f
        EffectMotion.DRIFT -> 0.7f
    }
    for (p in sim.particles) {
        p.journey += p.speed * speedScale * breathing * motionMul * dt
        if (style.motion == EffectMotion.DRIFT) {
            p.baseX += p.drift * 0.05f * dt * speedScale
            if (p.baseX > 1.04f) p.baseX = -0.04f
            if (p.baseX < -0.04f) p.baseX = 1.04f
        }
        if (p.journey > 1.12f) p.randomise(style.emojis.size)
    }
}

/**
 * Emoji particles overlaid on top of a screen (home, library, player...).
 * Touches pass straight through - the canvas has no pointer handling.
 *
 * Why this is light on the CPU/GPU:
 *  - the canvas lives in its own graphics layer, so each frame only re-draws these few emojis
 *    and never the whole screen underneath
 *  - emojis are pre-rendered to bitmaps once; each frame only draws scaled bitmaps
 *  - frames are capped at ~60 per second even on 90/120 Hz displays
 *  - the simulation is plain fields (no per-frame object allocation, no state per particle)
 *  - the first start waits until Home has had time to load
 *  - the animation stops while the app is in the background
 *
 * Look: emojis have depth (far = small, slow, soft; near = big, quick, bright), sway side to side,
 * rock gently instead of spinning, fade in/out smoothly and the whole effect eases in when shown.
 * When [beatReactive] is true the fall speed gently breathes.
 * Effects are silent: [soundEnabled] is kept only for API compatibility and ignored.
 */
@Composable
fun EmojiEffectOverlay(
    enabled: Boolean,
    isPlaying: Boolean = true,
    style: EmojiEffectStyle,
    beatReactive: Boolean,
    modifier: Modifier = Modifier,
    soundEnabled: Boolean = false,
    particleCount: Int = 20
) {
    if (!enabled) return

    var ready by remember { mutableStateOf(startupGateOpen) }
    if (!ready) {
        LaunchedEffect(Unit) {
            delay(STARTUP_DELAY_MS)
            startupGateOpen = true
            ready = true
        }
        return
    }

    EmojiEffectContent(
        isPlaying = isPlaying,
        style = style,
        beatReactive = beatReactive,
        modifier = modifier,
        particleCount = particleCount
    )
}

@Composable
private fun EmojiEffectContent(
    isPlaying: Boolean,
    style: EmojiEffectStyle,
    beatReactive: Boolean,
    modifier: Modifier,
    particleCount: Int
) {
    // Fall speed chosen in Settings (Slow 0.6x / Normal 1x / Fast 1.6x / Super Fast 2.4x) and fade-out option.
    val mainViewModel = androidx.hilt.navigation.compose.hiltViewModel<com.arslandaim.playtube.MainViewModel>()
    val speedScale by mainViewModel.emojiEffectsSpeed.collectAsStateWithLifecycle()
    val fadeOut by mainViewModel.emojiEffectsFadeOut.collectAsStateWithLifecycle()

    val density = LocalDensity.current.density
    val lifecycleOwner = LocalLifecycleOwner.current

    val count by mainViewModel.emojiEffectsCount.collectAsStateWithLifecycle()
    val sim = remember(style.id, count) { EffectSim(count, style.emojis.size) }

    val bitmapPx = remember(density) { (EMOJI_BITMAP_DP * density).toInt().coerceIn(48, 192) }
    val bitmaps by produceState<List<Bitmap>>(emptyList(), style.id, bitmapPx) {
        value = emptyList()
        value = withContext(Dispatchers.Default) {
            style.emojis.map { renderEmojiBitmap(it, bitmapPx) }
        }
    }

    var frame by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isPlaying, sim, beatReactive, bitmaps.isNotEmpty()) {
        if (!isPlaying || bitmaps.isEmpty()) return@LaunchedEffect
        // Only animate while the app is actually visible.
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            var last = withFrameNanos { it }
            while (true) {
                withFrameNanos { now ->
                    val rawDt = (now - last) / 1_000_000_000f
                    if (rawDt < MIN_FRAME_S) return@withFrameNanos
                    last = now
                    stepSim(sim, style, rawDt.coerceIn(0f, 0.05f), speedScale, beatReactive)
                    frame = now // invalidates only the Canvas layer below
                }
            }
        }
    }

    val paint = remember { Paint().apply { isFilterBitmap = true } }
    val matrix = remember { Matrix() }

    // Own graphics layer: redrawing the emojis never re-records the screen underneath.
    Canvas(modifier = modifier.fillMaxSize().graphicsLayer { }) {
        if (frame < 0L) return@Canvas
        val list = bitmaps
        if (list.isEmpty()) return@Canvas

        val t = (sim.elapsed % 3600.0).toFloat()
        val appear = smooth((sim.elapsed / 0.9).toFloat())
        val rising = style.motion == EffectMotion.RISE
        val canvas = drawContext.canvas.nativeCanvas
        val w = size.width
        val h = size.height

        for (p in sim.particles) {
            val journey = p.journey
            val yNorm = if (rising) 1f - journey else journey
            if (yNorm < -0.12f || yNorm > 1.12f) continue

            var life = 1f
            if (fadeOut) {
                // Soft fade-in at the start, smooth fade-out and shrink over the last ~45% of the path.
                val fin = smooth(journey / 0.10f)
                val fout = smooth(1f - (journey - 0.55f) / 0.45f)
                life = fin * fout
            }
            if (life <= 0.01f) continue

            val bmp = list[p.emojiIndex % list.size]
            val depthAlpha = 0.55f + 0.45f * p.depth
            paint.alpha = (depthAlpha * life * appear * 255f).toInt().coerceIn(0, 255)

            val sway = sin(t * p.swayFreq + p.phase) * p.swayAmp
            val drawX = (p.baseX + sway).coerceIn(0.02f, 0.98f) * w
            val drawY = yNorm * h
            val shrink = if (fadeOut) 0.75f + 0.25f * life else 1f
            val s = (p.sizeDp * density * shrink) / bmp.width
            val rock = sin(t * p.swayFreq * 0.9f + p.phase + 1.3f) * p.rockDeg

            matrix.reset()
            matrix.postTranslate(-bmp.width / 2f, -bmp.height / 2f)
            matrix.postScale(s, s)
            matrix.postRotate(rock)
            matrix.postTranslate(drawX, drawY)
            canvas.drawBitmap(bmp, matrix, paint)
        }
    }
}
