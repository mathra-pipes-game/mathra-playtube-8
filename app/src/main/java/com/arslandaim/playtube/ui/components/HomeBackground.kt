package com.arslandaim.playtube.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.arslandaim.playtube.R
import com.arslandaim.playtube.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

const val HOME_BG_LOGO = "LOGO"
const val HOME_BG_PHOTO = "PHOTO"
const val HOME_BG_OFF = "OFF"

@HiltViewModel
class HomeBackgroundViewModel @Inject constructor(
    preferencesManager: PreferencesManager
) : ViewModel() {
    val style: StateFlow<String> = preferencesManager.homeBgStyle
        .stateIn(viewModelScope, SharingStarted.Eagerly, HOME_BG_LOGO)
    val path: StateFlow<String> = preferencesManager.homeBgPath
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val dim: StateFlow<Float> = preferencesManager.homeBgDim
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.45f)
}

/** Decodes a saved photo, scaled down so its longest side is at most [maxSide] px. */
private fun decodeSampled(path: String, maxSide: Int): ImageBitmap? = try {
    val file = File(path)
    if (!file.exists()) {
        null
    } else {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        var sample = 1
        while (bounds.outWidth / sample > maxSide * 2 || bounds.outHeight / sample > maxSide * 2) sample *= 2
        val opts = BitmapFactory.Options().apply {
            inSampleSize = sample
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        BitmapFactory.decodeFile(path, opts)?.asImageBitmap()
    }
} catch (e: Throwable) {
    null
}

/** Background layer for the Home screen. Put it as the first child of a Box. */
@Composable
fun HomeBackground(
    modifier: Modifier = Modifier,
    viewModel: HomeBackgroundViewModel = hiltViewModel()
) {
    val style by viewModel.style.collectAsStateWithLifecycle()
    val path by viewModel.path.collectAsStateWithLifecycle()
    val dim by viewModel.dim.collectAsStateWithLifecycle()
    HomeBackgroundContent(style = style, photoPath = path, dim = dim, modifier = modifier)
}

/** Stateless renderer, also used for the live preview inside Premium Studio. */
@Composable
fun HomeBackgroundContent(
    style: String,
    photoPath: String,
    dim: Float,
    modifier: Modifier = Modifier
) {
    when {
        style == HOME_BG_PHOTO && photoPath.isNotBlank() -> PhotoBackground(photoPath, dim, modifier)
        style == HOME_BG_OFF -> Box(modifier)
        else -> LogoBackground(modifier)
    }
}

@Composable
private fun PhotoBackground(path: String, dim: Float, modifier: Modifier) {
    val c = MaterialTheme.colorScheme
    val bitmap by produceState<ImageBitmap?>(null, path) {
        value = withContext(Dispatchers.IO) { decodeSampled(path, 1100) }
    }
    val appear by animateFloatAsState(if (bitmap != null) 1f else 0f, tween(700), label = "bgAppear")
    val transition = rememberInfiniteTransition(label = "kenBurns")
    val zoom by transition.animateFloat(
        initialValue = 1.02f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Reverse),
        label = "zoom"
    )
    Box(modifier.fillMaxSize()) {
        bitmap?.let { bmp ->
            Image(
                bitmap = bmp,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = appear
                        scaleX = zoom
                        scaleY = zoom
                    }
            )
        }
        // Even dimming chosen by the user, plus a theme tint and readable top/bottom fades.
        Box(Modifier.fillMaxSize().background(c.background.copy(alpha = dim.coerceIn(0f, 0.9f))))
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            c.primary.copy(alpha = 0.14f),
                            Color.Transparent,
                            c.background.copy(alpha = 0.55f),
                            c.background.copy(alpha = 0.92f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun LogoBackground(modifier: Modifier) {
    val c = MaterialTheme.colorScheme
    val transition = rememberInfiniteTransition(label = "logoBg")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse),
        label = "logoBgDrift"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "logoBgPulse"
    )
    val primary = c.primary
    val tertiary = c.tertiary
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val w = this.size.width
                val h = this.size.height
                drawRect(
                    Brush.radialGradient(
                        listOf(primary.copy(alpha = 0.22f * pulse), Color.Transparent),
                        center = Offset(w * (0.2f + 0.5f * drift), h * 0.12f),
                        radius = w * 0.95f
                    )
                )
                drawRect(
                    Brush.radialGradient(
                        listOf(tertiary.copy(alpha = 0.16f), Color.Transparent),
                        center = Offset(w * (0.9f - 0.5f * drift), h * 0.9f),
                        radius = w * 0.9f
                    )
                )
            }
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.mathra_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 40.dp)
                .size(230.dp)
                .graphicsLayer {
                    alpha = 0.10f
                    val s = 0.97f + 0.05f * pulse
                    scaleX = s
                    scaleY = s
                }
                .clip(RoundedCornerShape(64.dp))
        )
    }
}
