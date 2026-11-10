package com.example.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PackageEntity
import com.example.ui.theme.BrandCyanPrimary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPackageEditDialog(
  packageToEdit: PackageEntity?,
  onDismiss: () -> Unit,
  onSave: (PackageEntity) -> Unit
) {
  val isEditing = packageToEdit != null

  var name by remember { mutableStateOf(packageToEdit?.name ?: "") }
  var category by remember { mutableStateOf(packageToEdit?.category ?: "Content Creation") }
  var price by remember { mutableStateOf(packageToEdit?.price?.toString() ?: "15000") }
  var billingType by remember { mutableStateOf(packageToEdit?.billingType ?: "One-Time") }
  var bestFor by remember { mutableStateOf(packageToEdit?.bestFor ?: "Small Businesses & Startups") }
  var description by remember { mutableStateOf(packageToEdit?.description ?: "") }
  var services by remember { mutableStateOf(packageToEdit?.services ?: "") }
  var deliverables by remember { mutableStateOf(packageToEdit?.deliverables ?: "Cloud link with high-res files") }
  var notes by remember { mutableStateOf(packageToEdit?.notes ?: "Includes 2 rounds of design revisions.") }
  var featured by remember { mutableStateOf(packageToEdit?.featured ?: false) }
  var active by remember { mutableStateOf(packageToEdit?.active ?: true) }
  var displayOrder by remember { mutableStateOf(packageToEdit?.displayOrder?.toString() ?: "10") }

  val categoryOptions = listOf("Content Creation", "Digital Marketing", "Website Development")
  val billingOptions = listOf("One-Time", "Monthly", "Starting at")
  var categoryExpanded by remember { mutableStateOf(false) }
  var billingExpanded by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 620.dp)
        .testTag("admin_package_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        // Title Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isEditing) "Edit Package" else "Add New Package",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(scrollState),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Package Name *") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("edit_pkg_name")
          )

          // Category Dropdown
          ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
              expanded = categoryExpanded,
              onDismissRequest = { categoryExpanded = false }
            ) {
              categoryOptions.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat) },
                  onClick = {
                    category = cat
                    categoryExpanded = false
                  }
                )
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = price,
              onValueChange = { price = it },
              label = { Text("Price (INR) *") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f).testTag("edit_pkg_price")
            )

            // Billing Type
            ExposedDropdownMenuBox(
              expanded = billingExpanded,
              onExpandedChange = { billingExpanded = !billingExpanded },
              modifier = Modifier.weight(1f)
            ) {
              OutlinedTextField(
                value = billingType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Billing") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = billingExpanded) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().menuAnchor()
              )
              ExposedDropdownMenu(
                expanded = billingExpanded,
                onDismissRequest = { billingExpanded = false }
              ) {
                billingOptions.forEach { b ->
                  DropdownMenuItem(
                    text = { Text(b) },
                    onClick = {
                      billingType = b
                      billingExpanded = false
                    }
                  )
                }
              }
            }
          }

          OutlinedTextField(
            value = bestFor,
            onValueChange = { bestFor = it },
            label = { Text("Best For (Target Industry)") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Package Explanation") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = services,
            onValueChange = { services = it },
            label = { Text("Services Included (separated by | or comma)") },
            placeholder = { Text("e.g. 15 Graphics|5 Reels|Captions & Hashtags") },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = deliverables,
            onValueChange = { deliverables = it },
            label = { Text("Deliverables / Formats") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Important Notes & Terms") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )

          // Switches: Featured & Active
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Featured / Popular Badge", fontSize = 14.sp)
            Switch(checked = featured, onCheckedChange = { featured = it })
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Active (Visible to Clients)", fontSize = 14.sp)
            Switch(checked = active, onCheckedChange = { active = it })
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
          onClick = {
            if (name.isNotBlank()) {
              val parsedPrice = price.toDoubleOrNull() ?: 10000.0
              val formattedPrice = when (billingType) {
                "Monthly" -> "₹${String.format("%,.0f", parsedPrice)}/month"
                "Starting at" -> "₹${String.format("%,.0f", parsedPrice)}+"
                else -> "₹${String.format("%,.0f", parsedPrice)}"
              }

              // Standardize pipe delimiter
              val cleanServices = services.replace(",", "|").split("|")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .joinToString("|")

              val newOrUpdated = PackageEntity(
                id = packageToEdit?.id ?: ("PKG-" + UUID.randomUUID().toString().substring(0, 6).uppercase()),
                name = name.trim(),
                category = category,
                price = parsedPrice,
                priceFormatted = formattedPrice,
                billingType = billingType,
                bestFor = bestFor.trim(),
                description = description.trim(),
                services = cleanServices,
                deliverables = deliverables.trim(),
                notes = notes.trim(),
                featured = featured,
                active = active,
                displayOrder = displayOrder.toIntOrNull() ?: 10,
                isFavorite = packageToEdit?.isFavorite ?: false,
                createdAt = packageToEdit?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
              )

              onSave(newOrUpdated)
              onDismiss()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("edit_pkg_save_btn")
        ) {
          Text(if (isEditing) "Save Changes" else "Publish Package", color = Color.Black, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
