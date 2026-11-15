package com.arslandaim.playtube.ui.components.main

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.arslandaim.playtube.BuildConfig
import com.arslandaim.playtube.R
import com.arslandaim.playtube.ui.components.PremiumBadge
import com.arslandaim.playtube.ui.components.PremiumButton
import com.arslandaim.playtube.ui.components.PremiumOutlineButton
import com.arslandaim.playtube.ui.components.PremiumProjectFooter
import com.arslandaim.playtube.ui.components.PremiumStatPill
import com.arslandaim.playtube.ui.components.premiumBrush

/** "About Mathra PlayTube" dialog - shared by the top bar logo and the Settings screen. */
@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        icon = {
            Image(
                painter = painterResource(id = R.drawable.mathra_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, premiumBrush(), RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mathra PlayTube",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                PremiumStatPill("Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Free YouTube experience\nSmooth Emoji Effects, loud & clear 200% sound, background play, downloads and more.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PremiumOutlineButton(
                        text = "Email",
                        icon = Icons.Default.Email,
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_SENDTO, "mailto:denuvanakash5@gmail.com".toUri()))
                        }
                    )
                    PremiumOutlineButton(
                        text = "TikTok",
                        icon = Icons.Default.MusicNote,
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, "https://www.tiktok.com/@mathra_quotes_1".toUri()))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                PremiumProjectFooter()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Built on the open-source PlayTube engine (GPL-3.0)",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        },
        confirmButton = {
            PremiumButton(text = "Close", onClick = onDismiss)
        }
    )
}
