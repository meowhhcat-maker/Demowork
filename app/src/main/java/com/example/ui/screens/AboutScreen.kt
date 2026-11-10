package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AboutScreen(
  onNavigateToContact: () -> Unit,
  onNavigateToPackages: () -> Unit
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("about_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Brand Banner
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, BrandCyanPrimary.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "AUERAA",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = ".DIGITAL",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BrandCyanPrimary
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Creative Content. Professional Branding. Digital Growth.",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = BrandCyanLight,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "AUERAA.DIGITAL helps businesses, brands, entrepreneurs, professionals, and organizations build a stronger digital presence through creative content, digital marketing, and professional website solutions.",
          fontSize = 14.sp,
          lineHeight = 22.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      }
    }

    // Our 7 Pillars
    Text(
      text = "OUR AGENCY PILLARS",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp,
      color = BrandCyanPrimary
    )

    val pillars = listOf(
      Triple("Content Creation", "High-velocity Reels, aesthetic carousels, and viral visual formats.", Icons.Default.VideoCameraBack),
      Triple("Digital Marketing", "Full-funnel Meta and Google advertising with continuous ROAS tracking.", Icons.Default.TrendingUp),
      Triple("Website Development", "Lightning-fast responsive landing pages, portals, and custom apps.", Icons.Default.Language),
      Triple("Branding & Identity", "Distinctive visual systems, brand books, and typography curation.", Icons.Default.Palette),
      Triple("Social Media Management", "Organic profile growth, audience engagement, and community building.", Icons.Default.Share),
      Triple("Web Solutions", "Custom portals, digital menus, real estate listings, and CMS setups.", Icons.Default.Code),
      Triple("AI & Automation", "WhatsApp automated chatbots, CRM synchronization, and smart workflows.", Icons.Default.AutoAwesome)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      pillars.forEach { (name, desc, icon) ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BrandCyanPrimary.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = icon, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(text = name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
              Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }

    // Mission Statement
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text("Our Mission", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrandCyanPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "To eliminate the friction of digital growth for modern businesses. We combine high-aesthetic visual taste with ruthless data-driven performance engineering to deliver real, compounding business outcomes.",
          fontSize = 13.sp,
          lineHeight = 19.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Action Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = onNavigateToPackages,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
        modifier = Modifier.weight(1f)
      ) {
        Text("Browse Packages", color = Color.Black, fontWeight = FontWeight.Bold)
      }

      OutlinedButton(
        onClick = onNavigateToContact,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Text("Get in Touch")
      }
    }

    Spacer(modifier = Modifier.height(60.dp))
  }
}
