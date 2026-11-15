/*
 * PlayTube Project Original (2026)
 * arslandaim-hub (GitHub.com/arslandaim-hub)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.screens.settings

import androidx.compose.material.icons.filled.Lock
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.arslandaim.playtube.ui.theme.AppThemePalettes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.ui.res.stringResource
import com.arslandaim.playtube.R
import androidx.core.net.toUri
import com.arslandaim.playtube.BuildConfig
import com.arslandaim.playtube.ui.components.GlassSurface
import com.arslandaim.playtube.ui.components.PremiumBadge
import com.arslandaim.playtube.ui.screens.studio.PremiumStudioDialog
import com.arslandaim.playtube.ui.components.PremiumDashboardTile
import com.arslandaim.playtube.ui.components.premiumEntrance
import com.arslandaim.playtube.ui.components.premiumFloat
import com.arslandaim.playtube.ui.components.PremiumButton
import com.arslandaim.playtube.ui.components.PremiumChip
import com.arslandaim.playtube.ui.components.PremiumHeroCard
import com.arslandaim.playtube.ui.components.PremiumIconTile
import com.arslandaim.playtube.ui.components.PremiumProjectFooter
import com.arslandaim.playtube.ui.components.PremiumStatPill
import com.arslandaim.playtube.ui.components.premiumBrush
import androidx.compose.foundation.layout.fillMaxHeight
import com.arslandaim.playtube.ui.components.LanguageSelectionSheet
import com.arslandaim.playtube.ui.screens.library.GlobalGlassAlpha

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    updateViewModel: UpdateViewModel,
    onViewHistory: () -> Unit,
    onDataManagement: () -> Unit,
    onAppearanceCustomization: () -> Unit,
    onRecommendationSettings: () -> Unit,
    onBack: () -> Unit
) {
    val isSearchHistoryPaused by viewModel.isSearchHistoryPaused.collectAsStateWithLifecycle()
    val isPipEnabled by viewModel.isPipEnabled.collectAsStateWithLifecycle()
    val isBackgroundPlayEnabled by viewModel.isBackgroundPlayEnabled.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val availableLocales = viewModel.availableLocales
    val isAutoUpdateEnabled by updateViewModel.isAutoUpdateEnabled.collectAsStateWithLifecycle()
    val updateInfo by updateViewModel.updateInfo.collectAsStateWithLifecycle()
    val preferredAudioBitrateKbps by viewModel.preferredAudioBitrateKbps.collectAsStateWithLifecycle()
    val isEmojiEffectsEnabled by viewModel.isEmojiEffectsEnabled.collectAsStateWithLifecycle()
    val emojiEffectsStyleId by viewModel.emojiEffectsStyle.collectAsStateWithLifecycle()
    val isEmojiBeatReactive by viewModel.isEmojiBeatReactive.collectAsStateWithLifecycle()
    val isEmojiSoundEnabled by viewModel.isEmojiSoundEnabled.collectAsStateWithLifecycle()
    val emojiEffectsSpeed by viewModel.emojiEffectsSpeed.collectAsStateWithLifecycle()
    val emojiEffectsCount by viewModel.emojiEffectsCount.collectAsStateWithLifecycle()
    val appLockPinHash by viewModel.appLockPinHash.collectAsStateWithLifecycle()
    val volumeBoostName by viewModel.volumeBoostName.collectAsStateWithLifecycle()
    val soundBoostLabel = com.arslandaim.playtube.utils.VolumeBoostLevel.entries
        .firstOrNull { it.name == volumeBoostName }?.label ?: "200%"

    SettingsContent(
        isSearchHistoryPaused = isSearchHistoryPaused,
        isPipEnabled = isPipEnabled,
        isBackgroundPlayEnabled = isBackgroundPlayEnabled,
        appLanguage = appLanguage,
        availableLocales = availableLocales,
        isAutoUpdateEnabled = isAutoUpdateEnabled,
        updateInfo = updateInfo,
        preferredAudioBitrateKbps = preferredAudioBitrateKbps,
        isEmojiEffectsEnabled = isEmojiEffectsEnabled,
        emojiEffectsStyleId = emojiEffectsStyleId,
        isEmojiBeatReactive = isEmojiBeatReactive,
        isEmojiSoundEnabled = isEmojiSoundEnabled,
        emojiEffectsSpeed = emojiEffectsSpeed,
        emojiEffectsCount = emojiEffectsCount,
        isAppLockEnabled = appLockPinHash.isNotEmpty(),
        soundBoostLabel = soundBoostLabel,
        onSetSearchHistoryPaused = viewModel::setSearchHistoryPaused,
        onSetPipEnabled = viewModel::setPipEnabled,
        onSetBackgroundPlayEnabled = viewModel::setBackgroundPlayEnabled,
        onSetAppLanguage = viewModel::setAppLanguage,
        onSetAutoUpdateEnabled = updateViewModel::setAutoUpdateEnabled,
        onClearAllDownloads = viewModel::clearAllDownloads,
        onSetAudioBitrate = viewModel::setPreferredAudioBitrateKbps,
        onSetEmojiEffectsEnabled = viewModel::setEmojiEffectsEnabled,
        onSetEmojiStyle = viewModel::setEmojiEffectsStyle,
        onSetEmojiBeatReactive = viewModel::setEmojiBeatReactive,
        onSetEmojiSoundEnabled = viewModel::setEmojiSoundEnabled,
        onSetEmojiSpeed = viewModel::setEmojiEffectsSpeed,
        onSetEmojiCount = viewModel::setEmojiEffectsCount,
        onSetAppLockPin = viewModel::setAppLockPin,
        onViewHistory = onViewHistory,
        onDataManagement = onDataManagement,
        onAppearanceCustomization = onAppearanceCustomization,
        onRecommendationSettings = onRecommendationSettings,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    isSearchHistoryPaused: Boolean,
    isPipEnabled: Boolean,
    isBackgroundPlayEnabled: Boolean,
    appLanguage: String?,
    availableLocales: List<java.util.Locale>,
    isAutoUpdateEnabled: Boolean,
    updateInfo: com.arslandaim.playtube.domain.repository.UpdateInfo,
    preferredAudioBitrateKbps: Int,
    isEmojiEffectsEnabled: Boolean,
    emojiEffectsStyleId: String,
    isEmojiBeatReactive: Boolean,
    isEmojiSoundEnabled: Boolean,
    emojiEffectsSpeed: Float,
    emojiEffectsCount: Int,
    isAppLockEnabled: Boolean,
    soundBoostLabel: String,
    onSetSearchHistoryPaused: (Boolean) -> Unit,
    onSetPipEnabled: (Boolean) -> Unit,
    onSetBackgroundPlayEnabled: (Boolean) -> Unit,
    onSetAppLanguage: (String?) -> Unit,
    onSetAutoUpdateEnabled: (Boolean) -> Unit,
    onClearAllDownloads: () -> Unit,
    onSetAudioBitrate: (Int) -> Unit,
    onSetEmojiEffectsEnabled: (Boolean) -> Unit,
    onSetEmojiStyle: (String) -> Unit,
    onSetEmojiBeatReactive: (Boolean) -> Unit,
    onSetEmojiSoundEnabled: (Boolean) -> Unit,
    onSetEmojiSpeed: (Float) -> Unit,
    onSetEmojiCount: (Int) -> Unit,
    onSetAppLockPin: (String) -> Unit,
    onViewHistory: () -> Unit,
    onDataManagement: () -> Unit,
    onAppearanceCustomization: () -> Unit,
    onRecommendationSettings: () -> Unit,
    onBack: () -> Unit
) {
    var showClearDownloadsDialog by remember { mutableStateOf(false) }
    var showDeveloperDialog by remember { mutableStateOf(false) }
    var showStudio by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isPipSupported = remember {
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }
    
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (showDeveloperDialog) {
        com.arslandaim.playtube.ui.components.main.AboutDialog(onDismiss = { showDeveloperDialog = false })
    }

    if (showStudio) {
        PremiumStudioDialog(
            onDismiss = { showStudio = false },
            onOpenAbout = { showDeveloperDialog = true }
        )
    }

    if (showClearDownloadsDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.clear_all_downloads_title),
            message = stringResource(R.string.clear_all_downloads_desc),
            onDismiss = { showClearDownloadsDialog = false },
            onConfirm = {
                onClearAllDownloads()
                showClearDownloadsDialog = false
            }
        )
    }

    if (showLanguageSheet) {
        LanguageSelectionSheet(
            availableLocales = availableLocales,
            currentLanguageTag = appLanguage,
            onDismiss = { showLanguageSheet = false },
            onLanguageSelected = { tag ->
                onSetAppLanguage(tag)
                showLanguageSheet = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GlassSurface(tonalElevation = 0.dp) {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    },
                    actions = {
                        // Small logo - opens the animated Premium Studio (themes, effects, sound)
                        Image(
                            painter = painterResource(id = R.drawable.mathra_logo),
                            contentDescription = "Mathra Studio",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .premiumFloat(2f)
                                .size(36.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .border(1.5.dp, premiumBrush(), RoundedCornerShape(11.dp))
                                .clickable { showStudio = true }
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Premium hero header
            item {
                PremiumHeroCard(modifier = Modifier.premiumEntrance(0), onClick = { showDeveloperDialog = true }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.mathra_logo),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .premiumFloat()
                                .size(68.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(2.dp, premiumBrush(), RoundedCornerShape(18.dp))
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Mathra PlayTube",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Project by Denuwan Akash",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PremiumStatPill("v${BuildConfig.VERSION_NAME}")
                        PremiumStatPill(if (isEmojiEffectsEnabled) "Effects ON" else "Effects OFF")
                        PremiumStatPill("$soundBoostLabel Sound")
                        PremiumStatPill("Ad-free")
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    PremiumButton(
                        text = "About & Contact",
                        icon = Icons.Default.Info,
                        onClick = { showDeveloperDialog = true }
                    )
                }
            }

            // Animated live dashboard
            item {
                Column(
                    modifier = Modifier.premiumEntrance(1),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumDashboardTile(
                            icon = Icons.Default.AutoAwesome,
                            title = "Emoji Effects",
                            value = if (isEmojiEffectsEnabled) "ON" else "OFF",
                            active = isEmojiEffectsEnabled,
                            modifier = Modifier.weight(1f)
                        )
                        PremiumDashboardTile(
                            icon = Icons.Default.GraphicEq,
                            title = "Sound Boost",
                            value = soundBoostLabel,
                            active = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumDashboardTile(
                            icon = Icons.Default.MusicNote,
                            title = "Audio Quality",
                            value = if (preferredAudioBitrateKbps <= 0) "Auto" else "${preferredAudioBitrateKbps} kbps",
                            active = true,
                            modifier = Modifier.weight(1f)
                        )
                        PremiumDashboardTile(
                            icon = Icons.Default.PlayArrow,
                            title = "Background Play",
                            value = if (isBackgroundPlayEnabled) "ON" else "OFF",
                            active = isBackgroundPlayEnabled,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Update Category
            item {
                SettingsGroup(title = stringResource(R.string.settings_group_update)) {
                    SettingsSwitchItem(
                        title = stringResource(R.string.settings_auto_update),
                        subtitle = stringResource(R.string.settings_auto_update_desc),
                        icon = Icons.Default.Info,
                        checked = isAutoUpdateEnabled,
                        onCheckedChange = onSetAutoUpdateEnabled
                    )
                    if (isAutoUpdateEnabled && updateInfo.hasUpdate) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.update_available, updateInfo.latestVersion),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = updateInfo.releaseNotes,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, updateInfo.updateUrl.toUri())
                                    context.startActivity(intent)
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(stringResource(R.string.download_from_github), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // History & Privacy Category
            item {
                SettingsGroup(title = stringResource(R.string.settings_group_history_privacy)) {
                    SettingsItem(
                        title = stringResource(R.string.settings_view_history),
                        subtitle = stringResource(R.string.settings_view_history_desc),
                        icon = Icons.Default.History,
                        onClick = onViewHistory,
                        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsSwitchItem(
                        title = stringResource(R.string.settings_pause_search_history),
                        subtitle = stringResource(R.string.settings_pause_search_history_desc),
                        icon = Icons.Default.Pause,
                        checked = isSearchHistoryPaused,
                        onCheckedChange = onSetSearchHistoryPaused
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    if (isPipSupported) {
                        SettingsSwitchItem(
                            title = stringResource(R.string.settings_pip),
                            subtitle = stringResource(R.string.settings_pip_desc),
                            icon = Icons.Default.PictureInPicture,
                            checked = isPipEnabled,
                            onCheckedChange = onSetPipEnabled
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsSwitchItem(
                        title = stringResource(R.string.background_play),
                        subtitle = stringResource(R.string.background_play_desc),
                        icon = Icons.Default.PlayArrow,
                        checked = isBackgroundPlayEnabled,
                        onCheckedChange = onSetBackgroundPlayEnabled
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.settings_data_backup),
                        subtitle = stringResource(R.string.settings_data_backup_desc),
                        icon = Icons.Default.Info,
                        onClick = onDataManagement,
                        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.playtube_engine),
                        subtitle = stringResource(R.string.playtube_engine_desc),
                        icon = Icons.Default.AutoAwesome,
                        onClick = onRecommendationSettings,
                        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                    )
                }
            }

            // Appearance & Playback Category
            item {
                SettingsGroup(title = stringResource(R.string.settings_group_appearance_playback)) {
                    SettingsItem(
                        title = stringResource(R.string.appearance_customization),
                        subtitle = stringResource(R.string.appearance_customization_desc),
                        icon = Icons.Default.Palette,
                        onClick = onAppearanceCustomization,
                        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.app_language),
                        subtitle = appLanguage?.let { tag ->
                            val locale = java.util.Locale.forLanguageTag(tag)
                            locale.getDisplayLanguage(locale).replaceFirstChar { it.uppercase() }
                        } ?: stringResource(R.string.system_default),
                        icon = Icons.Default.Language,
                        onClick = { showLanguageSheet = true }
                    )
                }
            }

            // Audio Quality & Emoji Effects Category
            item {
                SettingsGroup(title = "Audio & Effects  ✨") {
                    // Audio Quality (bitrate) picker
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Audio Quality", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = if (preferredAudioBitrateKbps <= 0) "Auto (best available)" else "${preferredAudioBitrateKbps} kbps",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val bitrateOptions = listOf(0, 64, 96, 128, 160, 192, 200, 256, 320)
                            bitrateOptions.forEach { kbps ->
                                PremiumChip(
                                    selected = preferredAudioBitrateKbps == kbps,
                                    onClick = { onSetAudioBitrate(kbps) },
                                    label = if (kbps <= 0) "Auto" else "${kbps}"
                                )
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = "Sound Boost • $soundBoostLabel",
                        subtitle = "Loud & clear by default. Change it in Mathra Studio (tap the logo) or the player menu.",
                        icon = Icons.Default.GraphicEq
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsSwitchItem(
                        title = "Emoji Effects",
                        subtitle = "Off by default. Turn on to let emojis fall across the app",
                        icon = Icons.Default.EmojiEmotions,
                        checked = isEmojiEffectsEnabled,
                        onCheckedChange = onSetEmojiEffectsEnabled
                    )
                    if (isEmojiEffectsEnabled) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Effect Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                com.arslandaim.playtube.ui.components.EmojiEffectStyles.forEach { effectStyle ->
                                    EffectStyleCard(
                                        emoji = effectStyle.emojis.first(),
                                        name = effectStyle.displayName,
                                        premium = effectStyle.premium,
                                        selected = emojiEffectsStyleId == effectStyle.id,
                                        onClick = { onSetEmojiStyle(effectStyle.id) }
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Fall Speed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "Slow" to 0.6f,
                                    "Normal" to 1.0f,
                                    "Fast" to 1.6f,
                                    "Super Fast" to 2.4f
                                ).forEach { (label, value) ->
                                    PremiumChip(
                                        selected = kotlin.math.abs(emojiEffectsSpeed - value) < 0.05f,
                                        onClick = { onSetEmojiSpeed(value) },
                                        label = label
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Emoji Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "Few" to 10,
                                    "Normal" to 20,
                                    "Many" to 30,
                                    "Lots" to 45
                                ).forEach { (label, value) ->
                                    PremiumChip(
                                        selected = emojiEffectsCount == value,
                                        onClick = { onSetEmojiCount(value) },
                                        label = label
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        SettingsSwitchItem(
                            title = "Beat Reactive Intensity",
                            subtitle = "Effect speed builds from soft to strong like a rhythm",
                            icon = Icons.Default.GraphicEq,
                            checked = isEmojiBeatReactive,
                            onCheckedChange = onSetEmojiBeatReactive
                        )
                    }
                }
            }

            // Security
            item {
                var showPinDialog by remember { mutableStateOf(false) }
                if (showPinDialog) {
                    com.arslandaim.playtube.ui.components.SetPinDialog(
                        onDismiss = { showPinDialog = false },
                        onConfirm = { pin ->
                            onSetAppLockPin(pin)
                            showPinDialog = false
                        }
                    )
                }
                SettingsGroup(title = "Security") {
                    SettingsSwitchItem(
                        title = "App Lock (PIN)",
                        subtitle = if (isAppLockEnabled) "A 4-digit PIN is asked every time you open the app" else "Protect the app with a 4-digit PIN",
                        icon = Icons.Default.Lock,
                        checked = isAppLockEnabled,
                        onCheckedChange = { turnOn ->
                            if (turnOn) showPinDialog = true else onSetAppLockPin("")
                        }
                    )
                }
            }

            // Content Category
            item {
                SettingsGroup(title = stringResource(R.string.settings_group_content)) {
                    SettingsItem(
                        title = stringResource(R.string.settings_clear_downloads),
                        subtitle = stringResource(R.string.settings_clear_downloads_desc),
                        icon = Icons.Default.Delete,
                        onClick = { showClearDownloadsDialog = true },
                        titleColor = MaterialTheme.colorScheme.error
                    )
                }
            }

            // About Category
            item {
                SettingsGroup(title = stringResource(R.string.settings_group_about)) {
                    SettingsItem(
                        title = stringResource(R.string.settings_about_developer),
                        subtitle = stringResource(R.string.settings_about_developer_desc),
                        icon = Icons.Default.Person,
                        onClick = { showDeveloperDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.settings_contact_email),
                        subtitle = stringResource(R.string.settings_contact_email_desc),
                        icon = Icons.Default.Email,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, "mailto:denuvanakash5@gmail.com".toUri())
                            context.startActivity(intent)
                        },
                        trailingIcon = Icons.AutoMirrored.Filled.OpenInNew
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.settings_tiktok),
                        subtitle = stringResource(R.string.settings_tiktok_desc),
                        icon = Icons.Default.MusicNote,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, "https://www.tiktok.com/@mathra_quotes_1".toUri())
                            context.startActivity(intent)
                        },
                        trailingIcon = Icons.AutoMirrored.Filled.OpenInNew
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    SettingsItem(
                        title = stringResource(R.string.settings_app_version),
                        subtitle = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        icon = Icons.Default.Info,
                        onClick = null
                    )
                }
            }
            
            item { PremiumProjectFooter(modifier = Modifier.premiumEntrance(1).padding(top = 8.dp)) }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.premiumEntrance(1)) {
        Row(
            modifier = Modifier.padding(start = 8.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(CircleShape)
                    .background(premiumBrush())
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.6.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(24.dp)
                ),
            content = content
        )
    }
}

/** Selectable effect preview card: big emoji, name and optional PRO tag. */
@Composable
fun EffectStyleCard(
    emoji: String,
    name: String,
    premium: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val c = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .width(92.dp)
            .clip(shape)
            .background(if (selected) c.primary.copy(alpha = 0.16f) else c.surfaceContainerHigh)
            .border(
                if (selected) 2.dp else 1.dp,
                if (selected) c.primary else c.outlineVariant.copy(alpha = 0.4f),
                shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingIcon: ImageVector? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    val isError = titleColor == MaterialTheme.colorScheme.error

    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PremiumIconTile(
                icon = icon,
                tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    fontWeight = FontWeight.SemiBold
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun settingsSwitchColors(): SwitchColors = com.arslandaim.playtube.ui.theme.settingsSwitchColors()

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumIconTile(icon = icon)
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = settingsSwitchColors()
        )
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.confirm), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
