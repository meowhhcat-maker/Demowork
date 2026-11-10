package com.example.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.BrandCyanPrimary

/**
 * Authentication Guard Composable for Admin Routes.
 * Guarantees that unauthenticated users cannot access administrative views.
 */
@Composable
fun AdminAuthGuard(
  isAuthenticated: Boolean,
  onNavigateToLogin: () -> Unit,
  content: @Composable () -> Unit
) {
  if (!isAuthenticated) {
    LaunchedEffect(Unit) {
      onNavigateToLogin()
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)
        .testTag("admin_auth_guard_intercept"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.size(64.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Restricted Access",
              tint = BrandCyanPrimary,
              modifier = Modifier.size(32.dp)
            )
          }
        }

        Text(
          text = "Authentication Required",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "This area is restricted to AUERAA.DIGITAL administrators. Please enter your security PIN to continue.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          lineHeight = 18.sp
        )

        Button(
          onClick = onNavigateToLogin,
          colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("guard_unlock_btn")
        ) {
          Text(
            text = "Enter Admin PIN",
            color = Color.Black,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  } else {
    content()
  }
}

/**
 * Helper extension to navigate to Admin destination with authentication guard check.
 */
fun NavController.navigateToAdminGuarded(isAuthenticated: Boolean) {
  if (isAuthenticated) {
    navigate(AueraaDestinations.Admin.route)
  } else {
    navigate(AueraaDestinations.AdminLogin.route)
  }
}
