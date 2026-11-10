package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomPackageEntity
import com.example.ui.components.WhatsAppButton
import com.example.ui.theme.*

@Composable
fun CustomPackageViewScreen(
  customPackage: CustomPackageEntity?,
  onBack: () -> Unit,
  onAcceptPackage: (CustomPackageEntity) -> Unit,
  onSendEnquiry: (CustomPackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  if (customPackage == null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Custom Package not found or expired.", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Go Back") }
      }
    }
    return
  }

  var accepted by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("custom_package_screen")
  ) {
    // Top Bar
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
          text = "Customized Client Proposal",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Prepared For Pill
      Surface(
        color = BrandCyanPrimary.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Verified, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Exclusively Prepared for: ${customPackage.clientName}",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = BrandCyanPrimary
          )
        }
      }

      // Package Name & Custom Price
      Text(
        text = customPackage.packageName,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onBackground
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = customPackage.priceFormatted,
          fontSize = 28.sp,
          fontWeight = FontWeight.Black,
          color = BrandCyanPrimary
        )
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "Quote ID: ${customPackage.shareToken}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      // Deliverables & Timeline Grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Est. Timeline", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(customPackage.timeline, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
        }

        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Revisions", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(customPackage.revisions, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
        }
      }

      // Services Included
      Text("Included Scope & Deliverables", fontWeight = FontWeight.Bold, fontSize = 17.sp)

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        customPackage.serviceList.forEach { s ->
          Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandEmeraldPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Text(s, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
          }
        }
      }

      if (customPackage.notes.isNotBlank()) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text("Proposal Notes", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandCyanPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(customPackage.notes, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }

    // Action CTAs
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              accepted = true
              onAcceptPackage(customPackage)
              Toast.makeText(context, "Proposal Accepted! Our team will connect to initiate onboarding.", Toast.LENGTH_LONG).show()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (accepted) BrandEmeraldPrimary else BrandCyanPrimary),
            modifier = Modifier.weight(1.2f)
          ) {
            Icon(imageVector = if (accepted) Icons.Default.Check else Icons.Default.ThumbUp, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (accepted) "Accepted ✓" else "Accept Package", color = Color.Black, fontWeight = FontWeight.Bold)
          }

          WhatsAppButton(
            text = "WhatsApp",
            onClick = {
              onOpenWhatsApp("Custom Quote ${customPackage.shareToken} for ${customPackage.clientName}")
            },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
