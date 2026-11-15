package com.arslandaim.playtube.ui.screens.studio

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.arslandaim.playtube.R
import com.arslandaim.playtube.data.local.PreferencesManager
import com.arslandaim.playtube.ui.components.EmojiEffectStyle
import com.arslandaim.playtube.ui.components.EmojiEffectStyles
import com.arslandaim.playtube.ui.components.emojiStyleById
import com.arslandaim.playtube.ui.components.HOME_BG_LOGO
import com.arslandaim.playtube.ui.components.HOME_BG_OFF
import com.arslandaim.playtube.ui.components.HOME_BG_PHOTO
import com.arslandaim.playtube.ui.components.HomeBackgroundContent
import com.arslandaim.playtube.ui.components.PremiumBadge
import com.arslandaim.playtube.ui.components.StudioTool
import com.arslandaim.playtube.ui.components.ToolDashboardCard
import com.arslandaim.playtube.ui.components.ToolTabChip
import com.arslandaim.playtube.ui.components.PremiumButton
import com.arslandaim.playtube.ui.components.PremiumOutlineButton
import com.arslandaim.playtube.ui.components.PremiumStatPill
import com.arslandaim.playtube.ui.components.PremiumChip
import com.arslandaim.playtube.ui.components.PremiumIconButton
import com.arslandaim.playtube.ui.components.premiumBrush
import com.arslandaim.playtube.ui.components.premiumEntrance
import com.arslandaim.playtube.ui.components.premiumFloat
import com.arslandaim.playtube.ui.screens.player.PlaybackManager
import com.arslandaim.playtube.ui.theme.AppThemePalettes
import com.arslandaim.playtube.ui.theme.MixThemeCount
import com.arslandaim.playtube.ui.theme.ThemePalette
import com.arslandaim.playtube.ui.theme.settingsSwitchColors
import com.arslandaim.playtube.utils.VolumeBoostLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/** Drives the Premium Studio: app theme, emoji effects and sound boost. */
@OptIn(UnstableApi::class)
@HiltViewModel
class StudioViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val playbackManager: PlaybackManager
) : ViewModel() {

    val themeName: StateFlow<String> = preferencesManager.appThemeColor
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "RED")

    val effectsEnabled: StateFlow<Boolean> = preferencesManager.emojiEffectsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val effectStyleId: StateFlow<String> = preferencesManager.emojiEffectsStyle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "fire")

    val effectSpeed: StateFlow<Float> = preferencesManager.emojiEffectsSpeed
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1f)

    val effectFadeOut: StateFlow<Boolean> = preferencesManager.emojiEffectsFadeOut
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val homeBgStyle: StateFlow<String> = preferencesManager.homeBgStyle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HOME_BG_LOGO)

    val homeBgPath: StateFlow<String> = preferencesManager.homeBgPath
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val homeBgDim: StateFlow<Float> = preferencesManager.homeBgDim
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.45f)

    val volumeBoost: StateFlow<VolumeBoostLevel> = playbackManager.volumeBoostLevel

    fun setTheme(name: String) {
        viewModelScope.launch { preferencesManager.setAppThemeColor(name) }
    }

    fun setEffectsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setEmojiEffectsEnabled(enabled) }
    }

    fun applyEffect(styleId: String) {
        viewModelScope.launch {
            preferencesManager.setEmojiEffectsStyle(styleId)
            preferencesManager.setEmojiEffectsEnabled(true)
        }
    }

    fun setHomeBgStyle(style: String) {
        viewModelScope.launch { preferencesManager.setHomeBgStyle(style) }
    }

    fun setHomeBgDim(dim: Float) {
        viewModelScope.launch { preferencesManager.setHomeBgDim(dim) }
    }

    /** Copies the picked photo into app storage (scaled, rotated upright) and sets it as the Home background. */
    fun setHomePhoto(uri: Uri) {
        viewModelScope.launch {
            val saved = withContext(Dispatchers.IO) { savePhoto(uri) }
            if (saved != null) {
                val old = homeBgPath.value
                preferencesManager.setHomeBgPath(saved)
                preferencesManager.setHomeBgStyle(HOME_BG_PHOTO)
                if (old.isNotBlank() && old != saved) withContext(Dispatchers.IO) { File(old).delete() }
            }
        }
    }

    private fun savePhoto(uri: Uri): String? = try {
        val resolver = context.contentResolver
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        var sample = 1
        while (bounds.outWidth / sample > 3200 || bounds.outHeight / sample > 3200) sample *= 2
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        val decoded = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
        val rotation = try {
            resolver.openInputStream(uri)?.use {
                when (android.media.ExifInterface(it).getAttributeInt(
                    android.media.ExifInterface.TAG_ORIENTATION,
                    android.media.ExifInterface.ORIENTATION_NORMAL
                )) {
                    android.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    android.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    android.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        } catch (e: Exception) {
            0f
        }
        if (decoded == null) {
            null
        } else {
            val upright = if (rotation != 0f) {
                Bitmap.createBitmap(
                    decoded, 0, 0, decoded.width, decoded.height,
                    Matrix().apply { postRotate(rotation) }, true
                )
            } else decoded
            val file = File(context.filesDir, "home_bg_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { upright.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            file.absolutePath
        }
    } catch (e: Throwable) {
        null
    }

    fun setEffectFadeOut(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setEmojiEffectsFadeOut(enabled) }
    }

    fun setEffectSpeed(speed: Float) {
        viewModelScope.launch { preferencesManager.setEmojiEffectsSpeed(speed) }
    }

    fun setVolumeBoost(level: VolumeBoostLevel) {
        playbackManager.setVolumeBoostLevel(level)
        viewModelScope.launch { preferencesManager.setVolumeBoostLevel(level.name) }
    }
}


/**
 * Full-screen animated "Mathra Studio" opened from the small logo.
 * Swipe the cards (or use the arrows) to browse themes, effects and sound levels.
 */
@Composable
fun PremiumStudioDialog(
    onDismiss: () -> Unit,
    onOpenAbout: (() -> Unit)? = null,
    viewModel: StudioViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        StudioContent(viewModel = viewModel, onClose = onDismiss, onOpenAbout = onOpenAbout)
    }
}

@Composable
private fun StudioContent(
    viewModel: StudioViewModel,
    onClose: () -> Unit,
    onOpenAbout: (() -> Unit)?
) {
    val c = MaterialTheme.colorScheme
    var tab by rememberSaveable { mutableStateOf(StudioTool.DASHBOARD) }

    val themeName by viewModel.themeName.collectAsStateWithLifecycle()
    val effectsEnabled by viewModel.effectsEnabled.collectAsStateWithLifecycle()
    val effectStyleId by viewModel.effectStyleId.collectAsStateWithLifecycle()
    val effectSpeed by viewModel.effectSpeed.collectAsStateWithLifecycle()
    val effectFadeOut by viewModel.effectFadeOut.collectAsStateWithLifecycle()
    val bgStyle by viewModel.homeBgStyle.collectAsStateWithLifecycle()
    val bgPath by viewModel.homeBgPath.collectAsStateWithLifecycle()
    val bgDim by viewModel.homeBgDim.collectAsStateWithLifecycle()

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.setHomePhoto(uri)
    }
    val pickPhoto: () -> Unit = {
        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    val boost by viewModel.volumeBoost.collectAsStateWithLifecycle()

    val transition = rememberInfiniteTransition(label = "studioBg")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse),
        label = "drift"
    )
    val glowA = c.primary.copy(alpha = 0.38f)
    val glowB = c.tertiary.copy(alpha = 0.30f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .drawBehind {
                val w = size.width
                val h = size.height
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(glowA, Color.Transparent),
                        center = Offset(w * (0.15f + 0.5f * drift), h * 0.12f),
                        radius = w * 0.95f
                    )
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(glowB, Color.Transparent),
                        center = Offset(w * (0.9f - 0.5f * drift), h * 0.85f),
                        radius = w * 0.9f
                    )
                )
            }
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudioHeader(
                modifier = Modifier.premiumEntrance(0),
                onClose = onClose,
                onAbout = onOpenAbout
            )

            Row(
                modifier = Modifier
                    .premiumEntrance(1)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudioTool.entries.forEach { t ->
                    ToolTabChip(tool = t, selected = tab == t, onClick = { tab = t })
                }
            }

            Box(modifier = Modifier.premiumEntrance(2)) {
                AnimatedContent(
                    targetState = tab,
                    transitionSpec = {
                        (fadeIn(tween(280)) + slideInVertically(tween(280)) { it / 14 }) togetherWith
                            fadeOut(tween(140))
                    },
                    label = "studioTab"
                ) { current ->
                    when (current) {
                        StudioTool.DASHBOARD -> DashboardTab(
                            themeName = themeName,
                            effectsEnabled = effectsEnabled,
                            effectStyleId = effectStyleId,
                            boost = boost,
                            bgStyle = bgStyle,
                            onOpen = { tab = it },
                            onToggleEffects = viewModel::setEffectsEnabled,
                            onShuffleTheme = viewModel::setTheme
                        )
                        StudioTool.THEMES -> ThemesTab(themeName, viewModel::setTheme, pickPhoto)
                        StudioTool.BACKGROUND -> BackgroundTab(
                            style = bgStyle,
                            photoPath = bgPath,
                            dim = bgDim,
                            onPickPhoto = pickPhoto,
                            onStyle = viewModel::setHomeBgStyle,
                            onDim = viewModel::setHomeBgDim
                        )
                        StudioTool.EFFECTS -> EffectsTab(
                            enabled = effectsEnabled,
                            styleId = effectStyleId,
                            speed = effectSpeed,
                            fadeOut = effectFadeOut,
                            onFadeOut = viewModel::setEffectFadeOut,
                            onToggle = viewModel::setEffectsEnabled,
                            onApply = viewModel::applyEffect,
                            onSpeed = viewModel::setEffectSpeed
                        )
                        StudioTool.SOUND -> SoundTab(boost, viewModel::setVolumeBoost)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Project by Denuwan Akash",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFC857)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StudioHeader(modifier: Modifier, onClose: () -> Unit, onAbout: (() -> Unit)?) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.mathra_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .premiumFloat(2f)
                .size(44.dp)
                .clip(RoundedCornerShape(13.dp))
                .border(1.5.dp, premiumBrush(), RoundedCornerShape(13.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Mathra Studio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Text(
                "Dashboard  •  Themes  •  Background  •  Effects  •  Sound",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.65f)
            )
        }
        if (onAbout != null) {
            PremiumIconButton(Icons.Default.Info, onAbout, "About")
            Spacer(Modifier.width(8.dp))
        }
        PremiumIconButton(Icons.Default.Close, onClose, "Close")
    }
}

// ---------------------------------------------------------------- Dashboard

@Composable
private fun DashboardTab(
    themeName: String,
    effectsEnabled: Boolean,
    effectStyleId: String,
    boost: VolumeBoostLevel,
    bgStyle: String,
    onOpen: (StudioTool) -> Unit,
    onToggleEffects: (Boolean) -> Unit,
    onShuffleTheme: (String) -> Unit
) {
    val palette = AppThemePalettes.firstOrNull { it.name.equals(themeName, ignoreCase = true) }
        ?: AppThemePalettes.first()
    val effect = emojiStyleById(effectStyleId)
    val shape = RoundedCornerShape(28.dp)
    val transition = rememberInfiniteTransition(label = "dashHero")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "dashShift"
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Hero: live preview of the applied theme
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .drawBehind {
                    drawRect(
                        Brush.linearGradient(
                            colors = listOf(palette.primary, palette.secondary, palette.tertiary),
                            start = Offset(-size.width * 0.3f + size.width * 0.5f * shift, 0f),
                            end = Offset(size.width * (0.9f + 0.5f * shift), size.height)
                        )
                    )
                }
                .border(1.dp, Color.White.copy(alpha = 0.35f), shape)
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Your look right now",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        palette.displayName,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(palette.primary, palette.secondary, palette.tertiary).forEach { col ->
                        Box(
                            Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PremiumStatPill("${AppThemePalettes.size} themes")
                PremiumStatPill("$MixThemeCount mix themes")
                PremiumStatPill("${EmojiEffectStyles.size} effects")
                PremiumStatPill("${VolumeBoostLevel.entries.size} sound levels")
            }
        }

        // Tool cards, each with its own logo
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ToolDashboardCard(
                tool = StudioTool.THEMES,
                value = palette.displayName,
                subtitle = "${AppThemePalettes.size} to pick",
                active = true,
                onClick = { onOpen(StudioTool.THEMES) },
                modifier = Modifier.weight(1f)
            )
            ToolDashboardCard(
                tool = StudioTool.EFFECTS,
                value = if (effectsEnabled) effect.displayName else "Off",
                subtitle = if (effectsEnabled) "Running" else "Tap to set up",
                active = effectsEnabled,
                onClick = { onOpen(StudioTool.EFFECTS) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ToolDashboardCard(
                tool = StudioTool.SOUND,
                value = "${boost.label} volume",
                subtitle = boost.description,
                active = true,
                onClick = { onOpen(StudioTool.SOUND) },
                modifier = Modifier.weight(1f)
            )
            ToolDashboardCard(
                tool = StudioTool.BACKGROUND,
                value = when (bgStyle) {
                    HOME_BG_PHOTO -> "My photo"
                    HOME_BG_OFF -> "Off"
                    else -> "Mathra logo"
                },
                subtitle = "Home screen",
                active = bgStyle != HOME_BG_OFF,
                onClick = { onOpen(StudioTool.BACKGROUND) },
                modifier = Modifier.weight(1f)
            )
        }

        // Quick actions
        Text("Quick actions", fontWeight = FontWeight.SemiBold, color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PremiumOutlineButton(
                text = "Random mix",
                icon = Icons.Default.Shuffle,
                onClick = {
                    val pool = AppThemePalettes.takeLast(MixThemeCount)
                        .filter { !it.name.equals(themeName, ignoreCase = true) }
                    if (pool.isNotEmpty()) onShuffleTheme(pool.random().name)
                },
                modifier = Modifier.weight(1f)
            )
            PremiumButton(
                text = if (effectsEnabled) "Effects off" else "Effects on",
                onClick = { onToggleEffects(!effectsEnabled) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/** Horizontal strip of small thumbnails used to jump straight to an item in a carousel. */
@Composable
private fun QuickPickRow(
    count: Int,
    currentPage: Int,
    onPick: (Int) -> Unit,
    thumb: @Composable (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(count) { i ->
            val selected = i == currentPage
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = if (selected) 0.18f else 0.06f))
                    .border(
                        if (selected) 2.dp else 1.dp,
                        Color.White.copy(alpha = if (selected) 0.9f else 0.2f),
                        CircleShape
                    )
                    .clickable { onPick(i) }
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                thumb(i)
            }
        }
    }
}

// ---------------------------------------------------------------- Themes

@Composable
private fun ThemesTab(themeName: String, onApply: (String) -> Unit, onPickPhoto: () -> Unit) {
    val palettes = AppThemePalettes
    val selectedIndex = palettes.indexOfFirst { it.name.equals(themeName, ignoreCase = true) }.coerceAtLeast(0)
    val pager = rememberPagerState(initialPage = selectedIndex) { palettes.size }
    LaunchedEffect(themeName) {
        if (pager.currentPage != selectedIndex && !pager.isScrollInProgress) pager.animateScrollToPage(selectedIndex)
    }
    val current = palettes[pager.currentPage.coerceIn(0, palettes.lastIndex)]
    val isApplied = current.name.equals(themeName, ignoreCase = true)
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CarouselPager(pager) { page ->
            val p = palettes[page]
            ThemeCard(p, selected = p.name.equals(themeName, ignoreCase = true)) { onApply(p.name) }
        }
        CarouselControls(pager, current.displayName)
        QuickPickRow(
            count = palettes.size,
            currentPage = pager.currentPage,
            onPick = { i -> scope.launch { pager.animateScrollToPage(i) } }
        ) { i ->
            val pal = palettes[i]
            Box(
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(pal.primary, pal.secondary, pal.tertiary)))
            )
        }
        PremiumButton(
            text = if (isApplied) "✓ Applied" else "Apply theme",
            onClick = { onApply(current.name) },
            modifier = Modifier.fillMaxWidth()
        )
        PremiumOutlineButton(
            text = "📷  Add my photo as Home background",
            onClick = onPickPhoto,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ThemeCard(p: ThemePalette, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(Brush.linearGradient(listOf(p.primary, p.secondary, p.tertiary)))
            .border(if (selected) 3.dp else 1.dp, Color.White.copy(alpha = if (selected) 0.95f else 0.3f), shape)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(p.primary, p.secondary, p.tertiary).forEach { color ->
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                p.displayName,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                if (selected) "✓ Applied" else "Tap to apply",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// ---------------------------------------------------------------- Background

@Composable
private fun BackgroundTab(
    style: String,
    photoPath: String,
    dim: Float,
    onPickPhoto: () -> Unit,
    onStyle: (String) -> Unit,
    onDim: (Float) -> Unit
) {
    val c = MaterialTheme.colorScheme
    var dimDraft by remember(dim) { mutableStateOf(dim) }
    val shape = RoundedCornerShape(28.dp)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Live preview of what Home will look like
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(shape)
                .background(c.background)
                .border(2.dp, premiumBrush(), shape)
        ) {
            HomeBackgroundContent(style = style, photoPath = photoPath, dim = dimDraft)
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Text("Home preview", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(
                    when {
                        style == HOME_BG_PHOTO && photoPath.isNotBlank() -> "Your photo"
                        style == HOME_BG_OFF -> "Plain background"
                        else -> "Mathra logo"
                    },
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            // Fake video cards so the readability of the dimming can be judged
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(2) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.16f))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    )
                }
            }
        }

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PremiumChip(
                label = "Mathra logo",
                selected = style == HOME_BG_LOGO,
                onClick = { onStyle(HOME_BG_LOGO) }
            )
            PremiumChip(
                label = "My photo",
                selected = style == HOME_BG_PHOTO && photoPath.isNotBlank(),
                onClick = { if (photoPath.isBlank()) onPickPhoto() else onStyle(HOME_BG_PHOTO) }
            )
            PremiumChip(
                label = "Off",
                selected = style == HOME_BG_OFF,
                onClick = { onStyle(HOME_BG_OFF) }
            )
        }

        PremiumButton(
            text = if (photoPath.isBlank()) "📷  Choose a photo" else "📷  Change photo",
            onClick = onPickPhoto,
            modifier = Modifier.fillMaxWidth()
        )

        if (style == HOME_BG_PHOTO && photoPath.isNotBlank()) {
            Text("Photo dimming  •  ${(dimDraft * 100).toInt()}%", fontWeight = FontWeight.SemiBold, color = Color.White)
            Slider(
                value = dimDraft,
                onValueChange = { dimDraft = it },
                onValueChangeFinished = { onDim(dimDraft) },
                valueRange = 0f..0.85f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = c.primary,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
            Text(
                "Less dimming = brighter photo. More dimming keeps the videos easy to read.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        } else {
            Text(
                "Pick any photo from your gallery - it appears behind your Home screen with a slow, gentle zoom.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

// ---------------------------------------------------------------- Effects

@Composable
private fun EffectsTab(
    enabled: Boolean,
    styleId: String,
    speed: Float,
    fadeOut: Boolean,
    onFadeOut: (Boolean) -> Unit,
    onToggle: (Boolean) -> Unit,
    onApply: (String) -> Unit,
    onSpeed: (Float) -> Unit
) {
    val styles = EmojiEffectStyles
    val selectedIndex = styles.indexOfFirst { it.id == styleId }.coerceAtLeast(0)
    val pager = rememberPagerState(initialPage = selectedIndex) { styles.size }
    LaunchedEffect(styleId) {
        if (pager.currentPage != selectedIndex && !pager.isScrollInProgress) pager.animateScrollToPage(selectedIndex)
    }
    val current = styles[pager.currentPage.coerceIn(0, styles.lastIndex)]
    val isActive = enabled && current.id == styleId
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.07f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Emoji Effects", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (enabled) "ON - emojis fall across the app" else "OFF - tap to turn on",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f)
                )
            }
            Switch(checked = enabled, onCheckedChange = onToggle, colors = settingsSwitchColors())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.07f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Fade away at the end", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (fadeOut) "ON - emojis fade out smoothly before the edge" else "OFF - emojis stay visible until they leave the screen",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f)
                )
            }
            Switch(checked = fadeOut, onCheckedChange = onFadeOut, colors = settingsSwitchColors())
        }

        CarouselPager(pager) { page ->
            val s = styles[page]
            EffectCard(s, active = enabled && s.id == styleId) { onApply(s.id) }
        }
        CarouselControls(pager, current.displayName)
        QuickPickRow(
            count = styles.size,
            currentPage = pager.currentPage,
            onPick = { i -> scope.launch { pager.animateScrollToPage(i) } }
        ) { i ->
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(30.dp)) {
                Text(styles[i].emojis.first(), fontSize = 20.sp)
            }
        }
        PremiumButton(
            text = if (isActive) "✓ Active" else "Use this effect",
            onClick = { onApply(current.id) },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Fall speed", fontWeight = FontWeight.SemiBold, color = Color.White)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Slow" to 0.6f, "Normal" to 1.0f, "Fast" to 1.6f, "Super Fast" to 2.4f).forEach { (label, v) ->
                PremiumChip(label = label, selected = abs(speed - v) < 0.05f, onClick = { onSpeed(v) })
            }
        }
    }
}

@Composable
private fun EffectCard(style: EmojiEffectStyle, active: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    val c = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(Color(0xFF16161D))
            .background(Brush.linearGradient(listOf(c.primary.copy(alpha = 0.30f), c.tertiary.copy(alpha = 0.12f))))
            .border(if (active) 3.dp else 1.dp, if (active) c.primary else Color.White.copy(alpha = 0.18f), shape)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                style.emojis.take(4).forEachIndexed { i, e -> BobbingEmoji(e, i) }
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    style.displayName,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                if (active) "✓ Active" else "Tap to use",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun BobbingEmoji(emoji: String, index: Int) {
    val transition = rememberInfiniteTransition(label = "bob")
    val v by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900 + index * 220, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "bobV"
    )
    Text(
        emoji,
        fontSize = 38.sp,
        modifier = Modifier.graphicsLayer {
            translationY = (0.5f - v) * 22.dp.toPx()
            rotationZ = (v - 0.5f) * 16f
        }
    )
}

// ---------------------------------------------------------------- Sound

@Composable
private fun SoundTab(boost: VolumeBoostLevel, onApply: (VolumeBoostLevel) -> Unit) {
    val levels = VolumeBoostLevel.entries
    val selectedIndex = levels.indexOf(boost).coerceAtLeast(0)
    val pager = rememberPagerState(initialPage = selectedIndex) { levels.size }
    LaunchedEffect(boost) {
        if (pager.currentPage != selectedIndex && !pager.isScrollInProgress) pager.animateScrollToPage(selectedIndex)
    }
    val current = levels[pager.currentPage.coerceIn(0, levels.lastIndex)]
    val isActive = current == boost

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CarouselPager(pager) { page ->
            val l = levels[page]
            SoundCard(l, active = l == boost) { onApply(l) }
        }
        CarouselControls(pager, "${current.label} - ${current.description}")
        PremiumButton(
            text = if (isActive) "✓ Active" else "Use ${current.label} sound",
            onClick = { onApply(current) },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Applies to every video. 200% is the loud & clear sweet spot; 300%+ can distort.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun SoundCard(level: VolumeBoostLevel, active: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    val c = MaterialTheme.colorScheme
    val amplitude = (level.gainMb / 1200f).coerceIn(0.3f, 1f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(Color(0xFF16161D))
            .background(Brush.linearGradient(listOf(c.primary.copy(alpha = 0.30f), c.tertiary.copy(alpha = 0.12f))))
            .border(if (active) 3.dp else 1.dp, if (active) c.primary else Color.White.copy(alpha = 0.18f), shape)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EqualizerBars(amplitude)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(level.label, color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
                if (level == VolumeBoostLevel.X200) {
                    Spacer(Modifier.width(8.dp))
                    PremiumBadge("BEST")
                }
            }
            Text(level.description, color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(2.dp))
            Text(
                if (active) "✓ Active" else "Tap to use",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun EqualizerBars(amplitude: Float) {
    val transition = rememberInfiniteTransition(label = "eq")
    val brush = premiumBrush()
    Row(
        modifier = Modifier.height(64.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(9) { i ->
            val h by transition.animateFloat(
                initialValue = 0.25f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(450 + i * 70, easing = FastOutSlowInEasing),
                    RepeatMode.Reverse
                ),
                label = "bar$i"
            )
            Box(
                Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .graphicsLayer {
                        scaleY = (h * amplitude).coerceIn(0.12f, 1f)
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    }
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

// ---------------------------------------------------------------- Carousel

/** Swipeable carousel: neighbours peek in and shrink/fade away from the centre. */
@Composable
private fun CarouselPager(state: PagerState, content: @Composable (Int) -> Unit) {
    HorizontalPager(
        state = state,
        contentPadding = PaddingValues(horizontal = 52.dp),
        pageSpacing = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val offset = (state.currentPage - page) + state.currentPageOffsetFraction
                    val d = abs(offset).coerceIn(0f, 1f)
                    val sc = 1f - 0.12f * d
                    scaleX = sc
                    scaleY = sc
                    alpha = 1f - 0.5f * d
                }
        ) {
            content(page)
        }
    }
}

/** Previous / next arrows (they wrap around) plus a "3 / 24 - Name" label. */
@Composable
private fun CarouselControls(state: PagerState, label: String) {
    val scope = rememberCoroutineScope()
    val count = state.pageCount
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PremiumIconButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous",
            onClick = { scope.launch { state.animateScrollToPage((state.currentPage - 1 + count) % count) } }
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Text(
                label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                "${state.currentPage + 1} / $count",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium
            )
        }
        PremiumIconButton(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            onClick = { scope.launch { state.animateScrollToPage((state.currentPage + 1) % count) } }
        )
    }
}
