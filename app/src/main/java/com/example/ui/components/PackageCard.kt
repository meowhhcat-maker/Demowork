package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.theme.*

@Composable
fun PackageCard(
  pkg: PackageEntity,
  isFavorite: Boolean,
  isInComparison: Boolean,
  onViewDetails: () -> Unit,
  onSendEnquiry: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleComparison: () -> Unit,
  onShare: () -> Unit,
  onWhatsApp: () -> Unit,
  modifier: Modifier = Modifier
) {
  val categoryColor = when (pkg.category) {
    "Content Creation" -> BrandVioletPrimary
    "Digital Marketing" -> BrandCyanPrimary
    "Website Development" -> BrandEmeraldPrimary
    else -> BrandCyanPrimary
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(
      1.dp,
      if (pkg.featured) categoryColor.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (pkg.featured) 4.dp else 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onViewDetails() }
      .testTag("package_card_${pkg.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header: Badges & Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // ID Badge
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = pkg.id,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Category Chip
          Surface(
            color = categoryColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = pkg.category,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = categoryColor,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          if (pkg.featured) {
            Surface(
              color = BrandAmber.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Featured",
                  tint = BrandAmber,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Popular",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = BrandAmber
                )
              }
            }
          }
        }

        // Action Icons: Compare, Favorite, Share
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onToggleComparison,
            modifier = Modifier.size(34.dp).testTag("compare_btn_${pkg.id}")
          ) {
            Icon(
              imageVector = if (isInComparison) Icons.Default.CheckCircle else Icons.Outlined.CompareArrows,
              contentDescription = "Compare",
              tint = if (isInComparison) BrandCyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier.size(34.dp).testTag("fav_btn_${pkg.id}")
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = "Favorite",
              tint = if (isFavorite) BrandAmber else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }

          IconButton(
            onClick = onShare,
            modifier = Modifier.size(34.dp).testTag("share_btn_${pkg.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Package Name
      Text(
        text = pkg.name,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Price & Billing Type
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = pkg.priceFormatted,
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          color = categoryColor
        )
        if (pkg.billingType.isNotBlank() && !pkg.priceFormatted.contains("/month")) {
          Text(
            text = "(${pkg.billingType})",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Best For Pill
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        shape = RoundedCornerShape(6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "Best For: ${pkg.bestFor}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Short Explanation
      Text(
        text = pkg.description,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Key Services (Preview up to 3)
      val previewServices = pkg.serviceList.take(3)
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        previewServices.forEach { service ->
          Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = categoryColor,
              modifier = Modifier.size(15.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
              text = service,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
        if (pkg.serviceList.size > 3) {
          Text(
            text = "+ ${pkg.serviceList.size - 3} more deliverables",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = categoryColor,
            modifier = Modifier.padding(start = 22.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom CTA Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onViewDetails,
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          modifier = Modifier.weight(1f).testTag("view_details_${pkg.id}")
        ) {
          Text(
            text = "View Details",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Button(
          onClick = onSendEnquiry,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = categoryColor
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
          modifier = Modifier.weight(1.2f).testTag("send_enquiry_${pkg.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Enquire Now",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        }

        // WhatsApp Icon Button
        IconButton(
          onClick = onWhatsApp,
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF25D366).copy(alpha = 0.15f))
            .testTag("whatsapp_btn_${pkg.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "WhatsApp",
            tint = Color(0xFF25D366),
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
