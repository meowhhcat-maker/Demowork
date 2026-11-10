package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AueraaDarkColorScheme = darkColorScheme(
  primary = BrandCyanPrimary,
  onPrimary = Color.Black,
  primaryContainer = BrandCyanDark,
  onPrimaryContainer = Color.White,
  secondary = BrandVioletPrimary,
  onSecondary = Color.White,
  secondaryContainer = BrandVioletDark,
  onSecondaryContainer = Color.White,
  tertiary = BrandEmeraldPrimary,
  onTertiary = Color.Black,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
  error = BrandRose,
  onError = Color.White
)

private val AueraaLightColorScheme = lightColorScheme(
  primary = BrandCyanDark,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = Color(0xFF0369A1),
  secondary = BrandVioletPrimary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFEDE9FE),
  onSecondaryContainer = Color(0xFF5B21B6),
  tertiary = BrandEmeraldDark,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightTextSecondary,
  outline = LightBorder,
  error = BrandRose,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to sleek agency dark theme
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) AueraaDarkColorScheme else AueraaLightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
