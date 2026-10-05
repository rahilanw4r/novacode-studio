package com.pocketide.app.ui.screens.developer

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import com.pocketide.app.BuildConfig
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.R
import com.pocketide.app.ui.components.PocketGlassCard
import com.pocketide.app.ui.components.PocketPrimaryButton
import com.pocketide.app.ui.components.PocketSecondaryButton
import com.pocketide.app.ui.components.liquidAuraBorder
import com.pocketide.app.ui.components.liquidBounceClick
import com.pocketide.app.ui.theme.PocketAmber
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketPurple
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketSurfaceVariant
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

private const val DEVELOPER_TELEGRAM_HANDLE = "@RahilAnw4r"
private const val DEVELOPER_TELEGRAM_URL = "https://t.me/RahilAnw4r"
private const val PROJECT_GITHUB_URL = "https://github.com/rahilanw4r/pocket-ide"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocketDeveloperScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PocketObsidian,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Creator & Developer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = PocketTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PocketTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PocketObsidian)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Profile Card
            item {
                PocketGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidAuraBorder(enabled = true, shape = RoundedCornerShape(16.dp), strokeWidth = 1.5.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Avatar / Custom Logo
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(PocketSurfaceElevated)
                                .border(1.dp, PocketBorder, RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "Developer Avatar",
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Rahil Anwar",
                                color = PocketTextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Lead Creator & Systems Architect",
                                color = PocketEmerald,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = DEVELOPER_TELEGRAM_HANDLE,
                                color = PocketCyan,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = "Architect of Pocket IDE. Building autonomous AI coding environments and native on-device Linux virtualization for modern creators.",
                            color = PocketTextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(Modifier.height(4.dp))

                        // Quick Contact Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PocketPrimaryButton(
                                text = "Telegram",
                                icon = Icons.AutoMirrored.Filled.Send,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEVELOPER_TELEGRAM_URL))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                height = 44.dp
                            )
                            PocketSecondaryButton(
                                text = "GitHub Repo",
                                icon = Icons.Default.Code,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PROJECT_GITHUB_URL))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                height = 44.dp
                            )
                        }
                    }
                }
            }

            // Official Channels & Contact
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Official Channels & Contact",
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    PocketGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Telegram Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEVELOPER_TELEGRAM_URL))
                                        context.startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PocketCyan.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = PocketCyan, modifier = Modifier.size(16.dp))
                                    }
                                    Column {
                                        Text("Telegram Account", color = PocketTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                        Text(DEVELOPER_TELEGRAM_HANDLE, color = PocketTextMuted, fontSize = 11.5.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PocketSurfaceElevated)
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(DEVELOPER_TELEGRAM_HANDLE))
                                            Toast.makeText(context, "Telegram copied: $DEVELOPER_TELEGRAM_HANDLE", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = PocketTextSecondary, modifier = Modifier.size(13.dp))
                                        Text("Copy", color = PocketTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            HorizontalDivider(color = PocketBorder)

                            // GitHub Repository Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PROJECT_GITHUB_URL))
                                        context.startActivity(intent)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PocketEmerald.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Code, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(16.dp))
                                    }
                                    Column {
                                        Text("GitHub Repository", color = PocketTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                        Text("rahilanw4r/pocket-ide", color = PocketTextMuted, fontSize = 11.5.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = PocketTextSecondary, modifier = Modifier.size(17.dp))
                            }
                        }
                    }
                }
            }

            // Architecture & Specifications
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Engine & Architecture",
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    PocketGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            SpecRow(icon = Icons.Default.Shield, label = "Studio Version", value = "v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})")
                            SpecRow(icon = Icons.Default.Memory, label = "Virtualization Runtime", value = "PRoot Isolated ARM64")
                            SpecRow(icon = Icons.Default.Person, label = "Lead Developer", value = "Rahil Anwar")
                            SpecRow(icon = Icons.Default.Code, label = "Linux Base", value = "Ubuntu 24.04 LTS")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = PocketTextSecondary, modifier = Modifier.size(15.dp))
            Text(label, color = PocketTextSecondary, fontSize = 12.5.sp)
        }
        Text(value, color = PocketTextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
    }
}
