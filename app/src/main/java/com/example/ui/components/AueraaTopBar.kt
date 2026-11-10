package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AueraaTopBar(
  currentRoute: String,
  unreadNotifications: Int,
  isAdmin: Boolean,
  isDarkTheme: Boolean,
  onNavigate: (String) -> Unit,
  onOpenNotifications: () -> Unit,
  onOpenAdmin: () -> Unit,
  onToggleTheme: () -> Unit
) {
  Surface(
    color = MaterialTheme.colorScheme.background,
    tonalElevation = 4.dp,
    modifier = Modifier.fillMaxWidth().testTag("aueraa_top_bar")
  ) {
    Column {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Logo & Brand Name
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onNavigate("home") }
            .testTag("brand_logo_button")
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .border(1.dp, BrandCyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_aueraa_logo),
              contentDescription = "AUERAA.DIGITAL Logo",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "AUERAA",
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onBackground
              )
              Text(
                text = ".DIGITAL",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                letterSpacing = 0.5.sp,
                color = BrandCyanPrimary
              )
            }
            Text(
              text = "Creative • Marketing • Web",
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Action Icons
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Search Icon
          IconButton(
            onClick = { onNavigate("packages") },
            modifier = Modifier.size(40.dp).testTag("topbar_search_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search Packages",
              tint = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.size(22.dp)
            )
          }

          // Enquiry Quick Button
          FilledTonalButton(
            onClick = { onNavigate("enquiry") },
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = BrandCyanPrimary.copy(alpha = 0.15f),
              contentColor = BrandCyanPrimary
            ),
            modifier = Modifier.testTag("topbar_enquiry_button")
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = "Enquiry",
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Enquire",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          // Admin / Lock Button
          IconButton(
            onClick = onOpenAdmin,
            modifier = Modifier.size(40.dp).testTag("topbar_admin_button")
          ) {
            Box {
              Icon(
                imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                contentDescription = if (isAdmin) "Admin Panel" else "Admin Login",
                tint = if (isAdmin) BrandEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
              )
              if (unreadNotifications > 0 && isAdmin) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(BrandRose)
                    .align(Alignment.TopEnd)
                )
              }
            }
          }

          // Theme Toggle
          IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.size(36.dp).testTag("topbar_theme_toggle")
          ) {
            Icon(
              imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
              contentDescription = "Toggle Theme",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        thickness = 1.dp
      )
    }
  }
}
