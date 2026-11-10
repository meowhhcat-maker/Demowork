package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.theme.*

@Composable
fun CompareScreen(
  allPackages: List<PackageEntity>,
  comparisonPackages: List<PackageEntity>,
  onRemoveFromComparison: (String) -> Unit,
  onAddPackageToComparison: (String) -> Unit,
  onClearComparison: () -> Unit,
  onSendEnquiry: (PackageEntity) -> Unit,
  onExplorePackages: () -> Unit
) {
  var showPackagePicker by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("compare_screen")
  ) {
    // ---------------------------------------------
    // TOP HEADER
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Package Comparison",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Compare up to 3 packages side-by-side",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (comparisonPackages.isNotEmpty()) {
          TextButton(onClick = onClearComparison) {
            Text("Clear All", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
          }
        }
      }
    }

    if (comparisonPackages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.CompareArrows,
            contentDescription = null,
            tint = BrandCyanPrimary,
            modifier = Modifier.size(60.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No packages selected for comparison",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Tap the compare icon (⇄) on any package card to compare features side-by-side.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onExplorePackages,
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary)
          ) {
            Text("Browse Packages", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      // Comparison Table
      val horizontalScroll = rememberScrollState()
      val verticalScroll = rememberScrollState()

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(verticalScroll)
          .padding(16.dp)
      ) {
        // Add more packages button
        if (comparisonPackages.size < 3) {
          Button(
            onClick = { showPackagePicker = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = BrandCyanPrimary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Another Package to Compare (${comparisonPackages.size}/3)", color = MaterialTheme.colorScheme.onSurface)
          }
          Spacer(modifier = Modifier.height(16.dp))
        }

        // Horizontal Scrollable Grid
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .horizontalScroll(horizontalScroll)
              .padding(12.dp)
          ) {
            // Feature Labels Column
            Column(
              modifier = Modifier.width(130.dp),
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Text("Package", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandCyanPrimary)
              Text("Price", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Category", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Billing Cycle", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Best For", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Key Services", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Action", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            // Columns for each selected package
            comparisonPackages.forEach { pkg ->
              Column(
                modifier = Modifier
                  .width(190.dp)
                  .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                // Header with Remove Button
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = pkg.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                  )
                  IconButton(
                    onClick = { onRemoveFromComparison(pkg.id) },
                    modifier = Modifier.size(26.dp)
                  ) {
                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                  }
                }

                // Price
                Text(
                  text = pkg.priceFormatted,
                  fontWeight = FontWeight.Black,
                  fontSize = 16.sp,
                  color = BrandCyanPrimary
                )

                // Category
                Text(
                  text = pkg.category,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Billing
                Text(
                  text = pkg.billingType,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Best For
                Text(
                  text = pkg.bestFor,
                  fontSize = 12.sp,
                  lineHeight = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )

                // Services
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  pkg.serviceList.take(4).forEach { s ->
                    Row(verticalAlignment = Alignment.Top) {
                      Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmeraldPrimary, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(text = s, fontSize = 11.sp, lineHeight = 15.sp)
                    }
                  }
                }

                // Enquiry Button
                Button(
                  onClick = { onSendEnquiry(pkg) },
                  colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text("Enquire", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(60.dp))
      }
    }
  }

  // Package Picker Dialog
  if (showPackagePicker) {
    AlertDialog(
      onDismissRequest = { showPackagePicker = false },
      title = { Text("Select Package to Compare") },
      text = {
        LazyColumn(
          modifier = Modifier.height(300.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val remaining = allPackages.filter { it.active && !comparisonPackages.any { cp -> cp.id == it.id } }
          items(remaining) { pkg ->
            Surface(
              onClick = {
                onAddPackageToComparison(pkg.id)
                showPackagePicker = false
              },
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(pkg.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                  Text(pkg.category, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(pkg.priceFormatted, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandCyanPrimary)
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showPackagePicker = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
