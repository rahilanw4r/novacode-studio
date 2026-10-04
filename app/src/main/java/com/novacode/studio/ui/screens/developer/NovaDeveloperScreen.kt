package com.novacode.studio.ui.screens.developer

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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
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
import com.novacode.studio.R
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaPrimaryButton
import com.novacode.studio.ui.components.NovaSecondaryButton
import com.novacode.studio.ui.components.liquidAuraBorder
import com.novacode.studio.ui.components.liquidBounceClick
import com.novacode.studio.ui.theme.NovaAmber
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaSurfaceVariant
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

private const val DEVELOPER_TELEGRAM_HANDLE = "@RahilAnw4r"
private const val DEVELOPER_TELEGRAM_URL = "https://t.me/RahilAnw4r"
private const val PROJECT_GITHUB_URL = "https://github.com/rahilanw4r/novacode-studio"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaDeveloperScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NovaObsidian,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Creator & Developer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NovaTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NovaTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaObsidian)
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
                NovaGlassCard(
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
                                .background(NovaSurfaceElevated)
                                .border(1.dp, NovaBorder, RoundedCornerShape(18.dp)),
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
                                color = NovaTextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Lead Creator & Systems Architect",
                                color = NovaEmerald,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = DEVELOPER_TELEGRAM_HANDLE,
                                color = NovaCyan,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = "Architect of NovaCode Studio. Building autonomous AI coding environments and native on-device Linux virtualization for modern creators.",
                            color = NovaTextSecondary,
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
                            NovaPrimaryButton(
                                text = "Telegram",
                                icon = Icons.Default.Send,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEVELOPER_TELEGRAM_URL))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f),
                                height = 44.dp
                            )
                            NovaSecondaryButton(
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
                        color = NovaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
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
                                            .background(NovaCyan.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(16.dp))
                                    }
                                    Column {
                                        Text("Telegram Account", color = NovaTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                        Text(DEVELOPER_TELEGRAM_HANDLE, color = NovaTextMuted, fontSize = 11.5.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NovaSurfaceElevated)
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(DEVELOPER_TELEGRAM_HANDLE))
                                            Toast.makeText(context, "Telegram copied: $DEVELOPER_TELEGRAM_HANDLE", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NovaTextSecondary, modifier = Modifier.size(13.dp))
                                        Text("Copy", color = NovaTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            HorizontalDivider(color = NovaBorder)

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
                                            .background(NovaEmerald.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Code, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(16.dp))
                                    }
                                    Column {
                                        Text("GitHub Repository", color = NovaTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                        Text("rahilanw4r/novacode-studio", color = NovaTextMuted, fontSize = 11.5.sp, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = NovaTextSecondary, modifier = Modifier.size(17.dp))
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
                        color = NovaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            SpecRow(icon = Icons.Default.Shield, label = "Studio Version", value = "v1.0.11 (Build 12)")
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
            Icon(icon, contentDescription = null, tint = NovaTextSecondary, modifier = Modifier.size(15.dp))
            Text(label, color = NovaTextSecondary, fontSize = 12.5.sp)
        }
        Text(value, color = NovaTextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
    }
}
