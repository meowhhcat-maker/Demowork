package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.components.WhatsAppButton
import com.example.ui.theme.*

@Composable
fun PackageDetailScreen(
  pkg: PackageEntity?,
  isFavorite: Boolean,
  isInComparison: Boolean,
  onBack: () -> Unit,
  onSendEnquiry: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleComparison: () -> Unit,
  onShare: () -> Unit,
  onWhatsApp: () -> Unit
) {
  if (pkg == null) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text("Package not found.")
    }
    return
  }

  val categoryColor = when (pkg.category) {
    "Content Creation" -> BrandVioletPrimary
    "Digital Marketing" -> BrandCyanPrimary
    "Website Development" -> BrandEmeraldPrimary
    else -> BrandCyanPrimary
  }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("package_detail_screen")
  ) {
    // ---------------------------------------------
    // TOP APP BAR
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn")) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onToggleComparison) {
            Icon(
              imageVector = if (isInComparison) Icons.Default.CheckCircle else Icons.Outlined.CompareArrows,
              contentDescription = "Compare",
              tint = if (isInComparison) BrandCyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(onClick = onToggleFavorite) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Bookmark",
              tint = if (isFavorite) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(onClick = onShare) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // ---------------------------------------------
    // SCROLLABLE CONTENT
    // ---------------------------------------------
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(20.dp)
    ) {
      // Category & ID Pill
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = categoryColor.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = pkg.category,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = categoryColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }

        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = pkg.id,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }

        if (pkg.featured) {
          Surface(
            color = BrandAmber.copy(alpha = 0.15f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "★ Featured",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BrandAmber,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Package Name
      Text(
        text = pkg.name,
        fontSize = 26.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 32.sp,
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Price & Billing
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = pkg.priceFormatted,
          fontSize = 28.sp,
          fontWeight = FontWeight.Black,
          color = categoryColor
        )
        if (pkg.billingType.isNotBlank() && !pkg.priceFormatted.contains("/month")) {
          Text(
            text = "(${pkg.billingType})",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Best For Card
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
              .size(38.dp)
              .clip(CircleShape)
              .background(categoryColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Stars,
              contentDescription = null,
              tint = categoryColor,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "RECOMMENDED FOR",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = pkg.bestFor,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Explanation
      Text(
        text = "Package Overview",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = pkg.description,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Services Included
      Text(
        text = "Services & Deliverables Included",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(12.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        pkg.serviceList.forEach { service ->
          Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(26.dp)
                  .clip(CircleShape)
                  .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = categoryColor,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = service,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      if (pkg.deliverables.isNotBlank()) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
          text = "Delivery Method",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = pkg.deliverables,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Important Notes
      if (pkg.notes.isNotBlank()) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = BrandCyanPrimary,
              modifier = Modifier.size(20.dp).padding(top = 1.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Important Notes",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = pkg.notes,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }

    // ---------------------------------------------
    // STICKY BOTTOM CTA SECTION
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        Text(
          text = "Interested in this package?",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onSendEnquiry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = categoryColor),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = Modifier.weight(1.2f).testTag("detail_send_enquiry_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Send Enquiry",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color.Black
            )
          }

          WhatsAppButton(
            text = "WhatsApp Us",
            onClick = onWhatsApp,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
