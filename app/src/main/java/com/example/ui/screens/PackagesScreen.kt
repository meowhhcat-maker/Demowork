package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.components.PackageCard
import com.example.ui.theme.BrandCyanPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedCategory: String,
  onCategoryChange: (String) -> Unit,
  selectedBudget: String,
  onBudgetChange: (String) -> Unit,
  selectedBusinessType: String,
  onBusinessTypeChange: (String) -> Unit,
  packages: List<PackageEntity>,
  favorites: List<PackageEntity>,
  comparisonIds: List<String>,
  onSelectPackage: (String) -> Unit,
  onSendEnquiry: (PackageEntity) -> Unit,
  onToggleFavorite: (String, Boolean) -> Unit,
  onToggleComparison: (String) -> Unit,
  onSharePackage: (PackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  val categories = listOf("All", "Content Creation", "Digital Marketing", "Website Development")
  val budgets = listOf("All", "Under ₹10,000", "₹10,000–₹25,000", "₹25,000–₹50,000", "₹50,000–₹1,00,000", "₹1,00,000+")
  val businessTypes = listOf(
    "All", "Small Business", "Startup", "Restaurant", "Hotel", "Resort",
    "E-commerce", "Real Estate", "Personal Brand", "Corporate", "Creator", "Education"
  )

  var showFiltersDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("packages_screen")
  ) {
    // ---------------------------------------------
    // SEARCH BAR
    // ---------------------------------------------
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search by package, industry, feature, price...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = BrandCyanPrimary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("packages_search_field")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category Chips Horizontal Scroll
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        categories.forEach { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { onCategoryChange(cat) },
            label = { Text(cat, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandCyanPrimary.copy(alpha = 0.2f),
              selectedLabelColor = BrandCyanPrimary
            ),
            modifier = Modifier.testTag("filter_cat_$cat")
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Secondary Filters Row (Budget & Business Type quick selector)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Budget Filter Chips
        budgets.drop(1).forEach { b ->
          SuggestionChip(
            onClick = {
              onBudgetChange(if (selectedBudget == b) "All" else b)
            },
            label = {
              Text(
                text = b,
                fontSize = 11.sp,
                color = if (selectedBudget == b) BrandCyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
              containerColor = if (selectedBudget == b) BrandCyanPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Business Type chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        businessTypes.drop(1).forEach { bt ->
          SuggestionChip(
            onClick = {
              onBusinessTypeChange(if (selectedBusinessType == bt) "All" else bt)
            },
            label = {
              Text(
                text = bt,
                fontSize = 11.sp,
                color = if (selectedBusinessType == bt) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
              containerColor = if (selectedBusinessType == bt) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
            )
          )
        }
      }
    }

    // Results Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${packages.size} Packages Found",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (searchQuery.isNotEmpty() || selectedCategory != "All" || selectedBudget != "All" || selectedBusinessType != "All") {
        TextButton(
          onClick = {
            onSearchChange("")
            onCategoryChange("All")
            onBudgetChange("All")
            onBusinessTypeChange("All")
          }
        ) {
          Text("Reset Filters", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
        }
      }
    }

    // ---------------------------------------------
    // RESULTS LIST
    // ---------------------------------------------
    if (packages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(54.dp)
          )
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "No packages matched your filters.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Try clearing the search or choosing 'All' categories.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              onSearchChange("")
              onCategoryChange("All")
              onBudgetChange("All")
              onBusinessTypeChange("All")
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary)
          ) {
            Text("Clear All Filters", color = androidx.compose.ui.graphics.Color.Black)
          }
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(packages) { pkg ->
          val isFav = favorites.any { it.id == pkg.id }
          val inComp = comparisonIds.contains(pkg.id)

          PackageCard(
            pkg = pkg,
            isFavorite = isFav,
            isInComparison = inComp,
            onViewDetails = { onSelectPackage(pkg.id) },
            onSendEnquiry = { onSendEnquiry(pkg) },
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
}
