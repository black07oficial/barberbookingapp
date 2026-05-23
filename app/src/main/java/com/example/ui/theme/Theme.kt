package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryOrange,
    secondary = LightOrange,
    background = DarkBlack,
    surface = Color(0xFF1E1E24),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = SoftWhite,
    onSurface = SoftWhite
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryOrange,
    secondary = LightOrange,
    background = CreamBackground,
    surface = SoftWhite,
    surfaceVariant = SurfaceMedium,
    outline = DividerGrey,
    onPrimary = Color.White,
    onSecondary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextSoftGrey
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  // Disable dynamic color to enforce our warm organic barber styling perfectly
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
