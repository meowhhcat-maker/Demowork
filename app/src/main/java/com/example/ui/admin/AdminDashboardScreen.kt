package com.example.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
  packages: List<PackageEntity>,
  enquiries: List<EnquiryEntity>,
  customPackages: List<CustomPackageEntity>,
  settings: AdminSettingsEntity,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedStatus: String,
  onStatusChange: (String) -> Unit,
  selectedService: String,
  onServiceChange: (String) -> Unit,
  onUpdateStatus: (EnquiryEntity, String) -> Unit,
  onUpdatePayment: (EnquiryEntity, String) -> Unit,
  onUpdateNotes: (EnquiryEntity, String, String) -> Unit,
  onDeleteEnquiry: (EnquiryEntity) -> Unit,
  onSavePackage: (PackageEntity) -> Unit,
  onDeletePackage: (PackageEntity) -> Unit,
  onDuplicatePackage: (PackageEntity, (String) -> Unit) -> Unit,
  onCreateCustomPackage: (CustomPackageEntity, (String) -> Unit) -> Unit,
  onOpenCustomPackagePreview: (String) -> Unit,
  onDeleteCustomPackage: (CustomPackageEntity) -> Unit,
  onSaveSettings: (AdminSettingsEntity) -> Unit,
  onResetCatalog: () -> Unit,
  onLogout: () -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  var selectedTab by remember { mutableStateOf("Enquiries") }
  val tabs = listOf("Enquiries", "Packages", "Custom Quotes", "Analytics", "Settings")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("admin_dashboard_screen")
  ) {
    // Top Bar
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 3.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = BrandEmeraldPrimary.copy(alpha = 0.15f),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "ADMIN CONSOLE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BrandEmeraldPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "AUERAA.DIGITAL",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        IconButton(onClick = onLogout, modifier = Modifier.testTag("admin_logout_btn")) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Logout",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Scrollable Sub-Navigation Tabs
    ScrollableTabRow(
      selectedTabIndex = tabs.indexOf(selectedTab).coerceAtLeast(0),
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = BrandCyanPrimary,
      edgePadding = 16.dp,
      indicator = { tabPositions ->
        val index = tabs.indexOf(selectedTab).coerceAtLeast(0)
        if (index < tabPositions.size) {
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
            color = BrandCyanPrimary,
            height = 3.dp
          )
        }
      }
    ) {
      tabs.forEach { tabName ->
        val badgeCount = when (tabName) {
          "Enquiries" -> enquiries.count { it.status == "New" }
          "Custom Quotes" -> customPackages.size
          else -> 0
        }

        Tab(
          selected = selectedTab == tabName,
          onClick = { selectedTab = tabName },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = tabName,
                fontWeight = if (selectedTab == tabName) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (selectedTab == tabName) BrandCyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant
              )
              if (badgeCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = if (tabName == "Enquiries") BrandRose else BrandCyanPrimary.copy(alpha = 0.2f),
                  shape = androidx.compose.foundation.shape.CircleShape
                ) {
                  Text(
                    text = "$badgeCount",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tabName == "Enquiries") Color.White else BrandCyanPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          },
          modifier = Modifier.testTag("admin_tab_${tabName.replace(" ", "_")}")
        )
      }
    }

    // Tab Body
    Box(modifier = Modifier.weight(1f)) {
      when (selectedTab) {
        "Enquiries" -> {
          AdminEnquiriesScreen(
            enquiries = enquiries,
            searchQuery = searchQuery,
            onSearchChange = onSearchChange,
            selectedStatus = selectedStatus,
            onStatusChange = onStatusChange,
            selectedService = selectedService,
            onServiceChange = onServiceChange,
            onUpdateStatus = onUpdateStatus,
            onUpdatePayment = onUpdatePayment,
            onUpdateNotes = onUpdateNotes,
            onDeleteEnquiry = onDeleteEnquiry
          )
        }
        "Packages" -> {
          AdminPackagesScreen(
            packages = packages,
            onSavePackage = onSavePackage,
            onDeletePackage = onDeletePackage,
            onDuplicatePackage = onDuplicatePackage
          )
        }
        "Custom Quotes" -> {
          AdminCustomPackageScreen(
            customPackages = customPackages,
            onCreateCustomPackage = onCreateCustomPackage,
            onOpenCustomPackagePreview = onOpenCustomPackagePreview,
            onDeleteCustomPackage = onDeleteCustomPackage,
            onOpenWhatsApp = onOpenWhatsApp
          )
        }
        "Analytics" -> {
          AdminAnalyticsScreen(
            packages = packages,
            enquiries = enquiries
          )
        }
        "Settings" -> {
          AdminSettingsScreen(
            settings = settings,
            onSaveSettings = onSaveSettings,
            onResetCatalog = onResetCatalog
          )
        }
      }
    }
  }
}
