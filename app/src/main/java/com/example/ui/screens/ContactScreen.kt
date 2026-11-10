package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminSettingsEntity
import com.example.ui.components.WhatsAppButton
import com.example.ui.theme.*

@Composable
fun ContactScreen(
  settings: AdminSettingsEntity,
  onOpenWhatsApp: (String?) -> Unit,
  onNavigateToEnquiry: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("contact_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    Column {
      Text(
        text = "GET IN TOUCH",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandCyanPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Connect With AUERAA.DIGITAL",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "We are ready to discuss your business challenges and tailor a high-growth solution.",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Direct WhatsApp Primary Action Card
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.5.dp, WhatsAppGreen.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(WhatsAppGreen.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = WhatsAppGreen, modifier = Modifier.size(24.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text("Instant WhatsApp Chat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(settings.whatsapp, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
        WhatsAppButton(
          text = "Chat on WhatsApp Now",
          onClick = { onOpenWhatsApp(null) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    // Contact Details List
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Phone Call
        ContactRow(
          icon = Icons.Default.Phone,
          title = "Direct Call",
          value = settings.phone,
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings.phone}"))
              context.startActivity(intent)
            } catch (_: Exception) {}
          }
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        // Email
        ContactRow(
          icon = Icons.Default.Email,
          title = "Email Enquiries",
          value = settings.email,
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${settings.email}"))
              context.startActivity(intent)
            } catch (_: Exception) {}
          }
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        // Location
        ContactRow(
          icon = Icons.Default.LocationOn,
          title = "Studio Headquarters",
          value = settings.address,
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(settings.address)}"))
              context.startActivity(intent)
            } catch (_: Exception) {}
          }
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        // Business Hours
        ContactRow(
          icon = Icons.Default.Schedule,
          title = "Business Hours",
          value = settings.businessHours,
          onClick = {}
        )
      }
    }

    // Social Channels
    Text(
      text = "SOCIAL PROFILES",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp,
      color = BrandCyanPrimary
    )

    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        SocialRow("Instagram", settings.instagram, Icons.Default.CameraAlt)
        SocialRow("Facebook", settings.facebook, Icons.Default.Public)
        SocialRow("LinkedIn", settings.linkedin, Icons.Default.Work)
        SocialRow("YouTube", settings.youtube, Icons.Default.VideoLibrary)
      }
    }

    // Enquiry Shortcut Button
    Button(
      onClick = onNavigateToEnquiry,
      shape = RoundedCornerShape(14.dp),
      colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
      modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
    ) {
      Icon(Icons.Default.Send, contentDescription = null, tint = Color.Black)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Submit Online Project Enquiry", color = Color.Black, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(60.dp))
  }
}

@Composable
fun ContactRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(BrandCyanPrimary.copy(alpha = 0.12f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(18.dp))
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
  }
}

@Composable
fun SocialRow(platform: String, handle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = icon, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(18.dp))
    Spacer(modifier = Modifier.width(10.dp))
    Text(text = platform, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.width(90.dp))
    Text(text = handle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}
