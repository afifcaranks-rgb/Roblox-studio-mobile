package com.afifcaranks.robloxstudiomobile.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val StudioColors = darkColorScheme(primary = Color(0xFF4C9AFF), secondary = Color(0xFF8AB4F8), background = Color(0xFF11151D), surface = Color(0xFF1B2230), onPrimary = Color.White, onBackground = Color.White, onSurface = Color.White)
@Composable fun StudioMobileTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = StudioColors, content = content) }
