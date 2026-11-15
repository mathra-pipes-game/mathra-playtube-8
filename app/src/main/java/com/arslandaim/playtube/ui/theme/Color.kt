/*
 * PlayTube Project Original (2026)
 * arslandaim-hub (GitHub.com/arslandaim-hub)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Branding (Red)
val YoutubeRed = Color(0xFFFF0000)
val DarkRed = Color(0xFFB90000)

// Extended Theme Palette Data Class
data class ThemePalette(
    val name: String,
    val displayName: String,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val primaryContainer: Color = primary.copy(alpha = 0.20f),
    val onPrimaryContainer: Color = primary,
    val secondaryContainer: Color = secondary.copy(alpha = 0.20f),
    val onSecondaryContainer: Color = secondary,
    val tertiaryContainer: Color = tertiary.copy(alpha = 0.18f),
    val onTertiaryContainer: Color = tertiary
)

val AppThemePalettes = listOf(
    ThemePalette(
        name = "RED",
        displayName = "Classic Red",
        primary = Color(0xFFE53935),
        secondary = Color(0xFFD81B60),
        tertiary = Color(0xFFFF8A65),
        primaryContainer = Color(0xFFFFDAD6),
        onPrimaryContainer = Color(0xFF410002),
        secondaryContainer = Color(0xFFFFD9E2),
        onSecondaryContainer = Color(0xFF3E001D)
    ),
    ThemePalette(
        name = "BLUE",
        displayName = "Ocean Blue",
        primary = Color(0xFF1E88E5),
        secondary = Color(0xFF00ACC1),
        tertiary = Color(0xFF5C6BC0),
        primaryContainer = Color(0xFFD7E3FF),
        onPrimaryContainer = Color(0xFF001B3E),
        secondaryContainer = Color(0xFFC2E8FF),
        onSecondaryContainer = Color(0xFF001E2B)
    ),
    ThemePalette(
        name = "GREEN",
        displayName = "Emerald Green",
        primary = Color(0xFF2E7D32),
        secondary = Color(0xFF00897B),
        tertiary = Color(0xFF7CB342),
        primaryContainer = Color(0xFFB7F397),
        onPrimaryContainer = Color(0xFF042100),
        secondaryContainer = Color(0xFFA7F0E4),
        onSecondaryContainer = Color(0xFF00201B)
    ),
    ThemePalette(
        name = "PURPLE",
        displayName = "Royal Purple",
        primary = Color(0xFF8E24AA),
        secondary = Color(0xFFD81B60),
        tertiary = Color(0xFF5C6BC0),
        primaryContainer = Color(0xFFF6D8FF),
        onPrimaryContainer = Color(0xFF320048),
        secondaryContainer = Color(0xFFFFD9E2),
        onSecondaryContainer = Color(0xFF3E001D)
    ),
    ThemePalette(
        name = "AMBER",
        displayName = "Sunset Amber",
        primary = Color(0xFFF57C00),
        secondary = Color(0xFFE53935),
        tertiary = Color(0xFFFBC02D),
        primaryContainer = Color(0xFFFFDCC1),
        onPrimaryContainer = Color(0xFF301400),
        secondaryContainer = Color(0xFFFFDAD6),
        onSecondaryContainer = Color(0xFF410002)
    ),
    ThemePalette(
        name = "CYAN",
        displayName = "Neon Cyan",
        primary = Color(0xFF00ACC1),
        secondary = Color(0xFF1E88E5),
        tertiary = Color(0xFF26A69A),
        primaryContainer = Color(0xFFC2E8FF),
        onPrimaryContainer = Color(0xFF001E2B),
        secondaryContainer = Color(0xFFD7E3FF),
        onSecondaryContainer = Color(0xFF001B3E)
    ),
    ThemePalette(
        name = "PINK",
        displayName = "Rose Pink",
        primary = Color(0xFFE91E63),
        secondary = Color(0xFF8E24AA),
        tertiary = Color(0xFFFF7043),
        primaryContainer = Color(0xFFFFD9E2),
        onPrimaryContainer = Color(0xFF3E001D),
        secondaryContainer = Color(0xFFF6D8FF),
        onSecondaryContainer = Color(0xFF320048)
    ),
    // ---- Premium "mix" themes (three-colour blends) ----
    ThemePalette("SUNSET_MIX", "Sunset Mix", Color(0xFFFF6B35), Color(0xFFFF3CAC), Color(0xFFFFB703)),
    ThemePalette("AURORA_MIX", "Aurora Mix", Color(0xFF00C896), Color(0xFF7B61FF), Color(0xFF00B4D8)),
    ThemePalette("MIDNIGHT_MIX", "Midnight Mix", Color(0xFF5B6CFF), Color(0xFF9B5DE5), Color(0xFF00BBF9)),
    ThemePalette("LAVA_MIX", "Lava Mix", Color(0xFFFF3B30), Color(0xFFFF7A00), Color(0xFFFFC300)),
    ThemePalette("NEON_POP", "Neon Pop", Color(0xFFFF2E93), Color(0xFF7C3AED), Color(0xFF00D9C0)),
    ThemePalette("ROYAL_GOLD", "Royal Gold", Color(0xFFE09F00), Color(0xFFFF7B00), Color(0xFFD4A017)),
    ThemePalette("ROSE_GOLD", "Rose Gold", Color(0xFFE0788F), Color(0xFFC9A227), Color(0xFFFF9EB5)),
    ThemePalette("MINT_FRESH", "Mint Fresh", Color(0xFF1FB5A6), Color(0xFF3A86FF), Color(0xFF52C77A)),
    ThemePalette("DEEP_OCEAN", "Deep Ocean Mix", Color(0xFF0077B6), Color(0xFF5E60CE), Color(0xFF00A8E8)),
    ThemePalette("CHERRY_BERRY", "Cherry Berry", Color(0xFFD81B60), Color(0xFF6A1B9A), Color(0xFFFF6F91)),
    ThemePalette("COSMIC_MIX", "Cosmic Mix", Color(0xFF6C4CFF), Color(0xFFFF4FD8), Color(0xFF29D3FF)),
    ThemePalette("TROPICAL_MIX", "Tropical Mix", Color(0xFF00BFA6), Color(0xFFFF6F3C), Color(0xFFFFD23F)),
    ThemePalette("CANDY_MIX", "Candy Mix", Color(0xFFFF5C8A), Color(0xFF7DD3FC), Color(0xFFFFD166)),
    ThemePalette("FOREST_DAWN", "Forest Dawn", Color(0xFF2E9E5B), Color(0xFFF4A300), Color(0xFF9BD14B)),
    ThemePalette("ELECTRIC_MIX", "Electric Mix", Color(0xFF2979FF), Color(0xFFD500F9), Color(0xFF00E5FF))
)

/** Number of three-colour "mix" themes (everything after the 7 classic single-colour ones). */
val MixThemeCount: Int get() = AppThemePalettes.size - 7

fun getThemePalette(colorName: String): ThemePalette {
    return AppThemePalettes.find { it.name.equals(colorName, ignoreCase = true) }
        ?: AppThemePalettes.first()
}

// Light Theme Surface Containers & Colors
val LightBackground = Color(0xFFF9F9FB)
val LightSurface = Color(0xFFF0F2F5)
val LightSurfaceVariant = Color(0xFFE4E7EC)
val LightOnSurface = Color(0xFF0F0F12)
val LightOnSurfaceVariant = Color(0xFF5A5C63)
val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
val LightSurfaceContainerLow = Color(0xFFF5F6F9)
val LightSurfaceContainer = Color(0xFFEEF0F4)
val LightSurfaceContainerHigh = Color(0xFFE6E8ED)
val LightSurfaceContainerHighest = Color(0xFFDCDEE3)

// Dark Theme Surface Containers & Colors (Deep Charcoal Dark)
val DarkBackground = Color(0xFF0F0F12)
val DarkSurface = Color(0xFF18181C)
val DarkSurfaceVariant = Color(0xFF24242A)
val DarkOnSurface = Color(0xFFF5F5F7)
val DarkOnSurfaceVariant = Color(0xFFA2A4AD)
val DarkSurfaceContainerLowest = Color(0xFF0A0A0C)
val DarkSurfaceContainerLow = Color(0xFF141418)
val DarkSurfaceContainer = Color(0xFF1C1C22)
val DarkSurfaceContainerHigh = Color(0xFF25252D)
val DarkSurfaceContainerHighest = Color(0xFF2E2E38)

// OLED Theme Surface Containers & Colors (Pure Pitch Black #000000)
val OledBackground = Color(0xFF000000)
val OledSurface = Color(0xFF0C0C0C)
val OledSurfaceVariant = Color(0xFF161616)
val OledOnSurface = Color(0xFFFFFFFF)
val OledOnSurfaceVariant = Color(0xFFB0B0B0)
val OledSurfaceContainerLowest = Color(0xFF000000)
val OledSurfaceContainerLow = Color(0xFF080808)
val OledSurfaceContainer = Color(0xFF121212)
val OledSurfaceContainerHigh = Color(0xFF1C1C1C)
val OledSurfaceContainerHighest = Color(0xFF262626)

// Special & Feature Accent Colors
val IncognitoPurple = Color(0xFF7C4DFF)
val OnlineGreen = Color(0xFF00C853)
val OfflineGray = Color(0xFF757575)

// Standard Alpha for Glass & Elevation Overlays
const val GlassAlpha = 0.88f
const val ScrimAlpha = 0.50f
