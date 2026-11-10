package com.example.ui.admin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.theme.*

@Composable
fun AdminPackagesScreen(
  packages: List<PackageEntity>,
  onSavePackage: (PackageEntity) -> Unit,
  onDeletePackage: (PackageEntity) -> Unit,
  onDuplicatePackage: (PackageEntity, (String) -> Unit) -> Unit
) {
  val context = LocalContext.current
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Content Creation", "Digital Marketing", "Website Development")

  var editingPackage by remember { mutableStateOf<PackageEntity?>(null) }
  var showCreateDialog by remember { mutableStateOf(false) }
  var packageToDelete by remember { mutableStateOf<PackageEntity?>(null) }

  val filteredPackages = packages.filter { pkg ->
    val matchesSearch = searchQuery.isBlank() ||
      pkg.name.contains(searchQuery, ignoreCase = true) ||
      pkg.id.contains(searchQuery, ignoreCase = true)
    val matchesCat = selectedCategory == "All" || pkg.category == selectedCategory
    matchesSearch && matchesCat
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("admin_packages_screen")
  ) {
    // Action Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Dynamic Catalog Manager", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Text("${packages.size} Total Packages", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Button(
        onClick = { showCreateDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        modifier = Modifier.testTag("admin_add_pkg_btn")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Package", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }

    // Search and Category Chips
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search packages...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        categories.forEach { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = { Text(cat, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandCyanPrimary.copy(alpha = 0.2f),
              selectedLabelColor = BrandCyanPrimary
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Packages List
    LazyColumn(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.weight(1f)
    ) {
      items(filteredPackages) { pkg ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  color = MaterialTheme.colorScheme.surfaceVariant,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = pkg.id,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                  color = if (pkg.active) BrandEmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = if (pkg.active) "Active" else "Hidden",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = if (pkg.active) BrandEmeraldPrimary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                if (pkg.featured) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    color = BrandAmber.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = "Featured",
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.sp,
                      color = BrandAmber,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              Text(
                text = pkg.priceFormatted,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = BrandCyanPrimary
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = pkg.name,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "${pkg.category} • Best for: ${pkg.bestFor}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Edit, Duplicate, Delete
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = { editingPackage = pkg },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit", fontSize = 12.sp)
              }

              OutlinedButton(
                onClick = {
                  onDuplicatePackage(pkg) { newId ->
                    Toast.makeText(context, "Package duplicated as $newId", Toast.LENGTH_SHORT).show()
                  }
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Duplicate", fontSize = 12.sp)
              }

              IconButton(
                onClick = { packageToDelete = pkg },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }

  // Create Dialog
  if (showCreateDialog) {
    AdminPackageEditDialog(
      packageToEdit = null,
      onDismiss = { showCreateDialog = false },
      onSave = { newPkg ->
        onSavePackage(newPkg)
        Toast.makeText(context, "Package created successfully!", Toast.LENGTH_SHORT).show()
      }
    )
  }

  // Edit Dialog
  if (editingPackage != null) {
    AdminPackageEditDialog(
      packageToEdit = editingPackage,
      onDismiss = { editingPackage = null },
      onSave = { updatedPkg ->
        onSavePackage(updatedPkg)
        Toast.makeText(context, "Package updated!", Toast.LENGTH_SHORT).show()
      }
    )
  }

  // Delete Confirm Dialog
  if (packageToDelete != null) {
    AlertDialog(
      onDismissRequest = { packageToDelete = null },
      title = { Text("Delete Package?") },
      text = { Text("Are you sure you want to remove '${packageToDelete!!.name}'? This action cannot be undone.") },
      confirmButton = {
        Button(
          onClick = {
            onDeletePackage(packageToDelete!!)
            packageToDelete = null
            Toast.makeText(context, "Package deleted", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Delete")
        }
      },
      dismissButton = {
        TextButton(onClick = { packageToDelete = null }) { Text("Cancel") }
      }
    )
  }
}
