package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.components.PackageCard
import com.example.ui.theme.*

@Composable
fun ServicesScreen(
  packages: List<PackageEntity>,
  favorites: List<PackageEntity>,
  comparisonIds: List<String>,
  initialCategory: String = "Content Creation",
  onSelectPackage: (String) -> Unit,
  onSendEnquiryForPackage: (PackageEntity) -> Unit,
  onToggleFavorite: (String, Boolean) -> Unit,
  onToggleComparison: (String) -> Unit,
  onSharePackage: (PackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  val categories = listOf("Content Creation", "Digital Marketing", "Website Development")
  var selectedTab by remember { mutableStateOf(initialCategory) }

  val currentPackages = packages.filter { it.category == selectedTab && it.active }

  val tabColor = when (selectedTab) {
    "Content Creation" -> BrandVioletPrimary
    "Digital Marketing" -> BrandCyanPrimary
    "Website Development" -> BrandEmeraldPrimary
    else -> BrandCyanPrimary
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("services_screen")
  ) {
    // ---------------------------------------------
    // TOP CATEGORY TABS
    // ---------------------------------------------
    ScrollableTabRow(
      selectedTabIndex = categories.indexOf(selectedTab).coerceAtLeast(0),
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = tabColor,
      edgePadding = 16.dp,
      indicator = { tabPositions ->
        val index = categories.indexOf(selectedTab).coerceAtLeast(0)
        if (index < tabPositions.size) {
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
            color = tabColor,
            height = 3.dp
          )
        }
      }
    ) {
      categories.forEach { cat ->
        val count = packages.count { it.category == cat && it.active }
        Tab(
          selected = selectedTab == cat,
          onClick = { selectedTab = cat },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = cat,
                fontWeight = if (selectedTab == cat) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (selectedTab == cat) tabColor else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = if (selectedTab == cat) tabColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp)
              ) {
                Text(
                  text = "$count",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (selectedTab == cat) tabColor else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          },
          modifier = Modifier.testTag("tab_${cat.replace(" ", "_")}")
        )
      }
    }

    // ---------------------------------------------
    // CATEGORY INTRO BANNER
    // ---------------------------------------------
    Surface(
      color = tabColor.copy(alpha = 0.08f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        val (header, sub) = when (selectedTab) {
          "Content Creation" -> Pair(
            "Visual Storytelling That Stops The Scroll",
            "From trending Reels & TikTok cuts to multi-slide educational carousels and brand design suites."
          )
          "Digital Marketing" -> Pair(
            "Performance Marketing With Predictable ROAS",
            "Targeted Meta Ads, Google Intent Dominators, Local SEO 3-Pack rankings, and retention funnels."
          )
          else -> Pair(
            "High-Converting Websites & Web Solutions",
            "Lightning-fast landing pages, full CMS business portals, e-commerce storefronts, and custom web apps."
          )
        }

        Text(
          text = header,
          fontSize = 17.sp,
          fontWeight = FontWeight.Black,
          color = tabColor
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = sub,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )
      }
    }

    // ---------------------------------------------
    // PACKAGE LIST
    // ---------------------------------------------
    LazyColumn(
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.weight(1f)
    ) {
      items(currentPackages) { pkg ->
        val isFav = favorites.any { it.id == pkg.id }
        val inComp = comparisonIds.contains(pkg.id)

        PackageCard(
          pkg = pkg,
          isFavorite = isFav,
          isInComparison = inComp,
          onViewDetails = { onSelectPackage(pkg.id) },
          onSendEnquiry = { onSendEnquiryForPackage(pkg) },
          onToggleFavorite = { onToggleFavorite(pkg.id, isFav) },
          onToggleComparison = { onToggleComparison(pkg.id) },
          onShare = { onSharePackage(pkg) },
          onWhatsApp = { onOpenWhatsApp(pkg.name) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(60.dp))
      }
    }
  }
}
