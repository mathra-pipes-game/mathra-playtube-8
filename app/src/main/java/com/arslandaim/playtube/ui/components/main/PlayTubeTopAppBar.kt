/*
 * PlayTube Project Original (2026)
 * arslandaim-hub (GitHub.com/arslandaim-hub)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.components.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arslandaim.playtube.MainViewModel
import com.arslandaim.playtube.R
import com.arslandaim.playtube.ui.navigation.Destination
import com.arslandaim.playtube.ui.screens.settings.UpdateViewModel
import com.arslandaim.playtube.ui.theme.IncognitoPurple
import com.arslandaim.playtube.ui.components.PremiumIconButton
import com.arslandaim.playtube.ui.components.premiumBrush
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayTubeTopAppBar(
    isIncognitoMode: Boolean,
    currentRoute: String?,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    updateViewModel: UpdateViewModel,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Mathra logo + name (tap to open Settings)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            navController.navigate(Destination.Settings) {
                                launchSingleTop = true
                            }
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mathra_logo),
                        contentDescription = "Mathra PlayTube",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .border(1.2.dp, premiumBrush(), RoundedCornerShape(9.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Mathra ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("PlayTube")
                            }
                        },
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 19.sp,
                            letterSpacing = (-0.3).sp
                        )
                    )
                    Text(
                        text = "by Denuwan Akash",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    }
                }

                if (isIncognitoMode) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = IncognitoPurple.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.incognito_label),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        },
        actions = {
            if (currentRoute?.contains("Search") == false) {
                PremiumIconButton(
                    icon = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    onClick = {
                        navController.navigate(Destination.Search()) {
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            val updateInfo by updateViewModel.updateInfo.collectAsStateWithLifecycle()
            val isAutoUpdateEnabled by updateViewModel.isAutoUpdateEnabled.collectAsStateWithLifecycle()

            PremiumIconButton(
                icon = if (isIncognitoMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = stringResource(R.string.incognito_label),
                tint = if (isIncognitoMode) IncognitoPurple else MaterialTheme.colorScheme.onSurface,
                active = isIncognitoMode,
                onClick = { mainViewModel.toggleIncognitoMode() }
            )
            Spacer(modifier = Modifier.width(8.dp))

            BadgedBox(
                badge = {
                    if (isAutoUpdateEnabled && updateInfo.hasUpdate) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text("!")
                        }
                    }
                }
            ) {
                PremiumIconButton(
                    icon = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings),
                    onClick = {
                        navController.navigate(Destination.Settings) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
