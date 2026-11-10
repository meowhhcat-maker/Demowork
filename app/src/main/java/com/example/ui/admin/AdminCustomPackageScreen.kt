package com.example.ui.admin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CustomPackageEntity
import com.example.ui.theme.BrandCyanPrimary
import com.example.ui.theme.WhatsAppGreen
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCustomPackageScreen(
  customPackages: List<CustomPackageEntity>,
  onCreateCustomPackage: (CustomPackageEntity, (String) -> Unit) -> Unit,
  onOpenCustomPackagePreview: (String) -> Unit,
  onDeleteCustomPackage: (CustomPackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  val context = LocalContext.current
  var showCreateDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("admin_custom_package_screen")
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Custom Client Quotes", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Text("Generate bespoke shareable proposals", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Button(
        onClick = { showCreateDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("New Quote", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }

    if (customPackages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(14.dp))
          Text("No custom proposals generated yet.", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Create customized scopes & pricing for specific clients with shareable links.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(onClick = { showCreateDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary)) {
            Text("Create First Custom Quote", color = Color.Black)
          }
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(customPackages) { cp ->
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
                Surface(
                  color = BrandCyanPrimary.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "TOKEN: ${cp.shareToken}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = BrandCyanPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }

                Text(
                  text = cp.priceFormatted,
                  fontWeight = FontWeight.Black,
                  fontSize = 16.sp,
                  color = BrandCyanPrimary
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = cp.packageName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              )

              Text(
                text = "Prepared for: ${cp.clientName} • ${cp.category}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "Timeline: ${cp.timeline} | Revisions: ${cp.revisions}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Actions
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                OutlinedButton(
                  onClick = { onOpenCustomPackagePreview(cp.shareToken) },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text("View Quote", fontSize = 12.sp)
                }

                IconButton(
                  onClick = {
                    val shareUrl = "https://aueraa.digital/quote/${cp.shareToken}"
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Quote Link", shareUrl))
                    Toast.makeText(context, "Quote link copied to clipboard!", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier.size(38.dp)
                ) {
                  Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link", modifier = Modifier.size(18.dp))
                }

                IconButton(
                  onClick = {
                    val message = "Hi ${cp.clientName}, here is your customized proposal from AUERAA.DIGITAL: https://aueraa.digital/quote/${cp.shareToken} ($${cp.priceFormatted})"
                    onOpenWhatsApp(message)
                  },
                  modifier = Modifier.size(38.dp)
                ) {
                  Icon(Icons.Default.Chat, contentDescription = "Send WhatsApp", tint = WhatsAppGreen, modifier = Modifier.size(18.dp))
                }

                IconButton(
                  onClick = { onDeleteCustomPackage(cp) },
                  modifier = Modifier.size(38.dp)
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
  }

  // Create Custom Quote Dialog
  if (showCreateDialog) {
    var clientName by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Content Creation") }
    var price by remember { mutableStateOf("25000") }
    var services by remember { mutableStateOf("") }
    var deliverables by remember { mutableStateOf("High-res files on Google Drive") }
    var timeline by remember { mutableStateOf("7 Working Days") }
    var revisions by remember { mutableStateOf("3 Rounds") }
    var notes by remember { mutableStateOf("50% advance to initiate project kick-off.") }

    val categoryOptions = listOf("Content Creation", "Digital Marketing", "Website Development")
    var catExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { showCreateDialog = false }) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(max = 620.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Create Custom Quote", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            IconButton(onClick = { showCreateDialog = false }) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Column(
            modifier = Modifier
              .weight(1f)
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            OutlinedTextField(
              value = clientName,
              onValueChange = { clientName = it },
              label = { Text("Client / Business Name *") },
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = packageName,
              onValueChange = { packageName = it },
              label = { Text("Custom Scope Name *") },
              placeholder = { Text("e.g. VIP Brand Revival Sprint") },
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
              expanded = catExpanded,
              onExpandedChange = { catExpanded = !catExpanded }
            ) {
              OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().menuAnchor()
              )
              ExposedDropdownMenu(
                expanded = catExpanded,
                onDismissRequest = { catExpanded = false }
              ) {
                categoryOptions.forEach { c ->
                  DropdownMenuItem(text = { Text(c) }, onClick = { category = c; catExpanded = false })
                }
              }
            }

            OutlinedTextField(
              value = price,
              onValueChange = { price = it },
              label = { Text("Custom Quote Price (INR) *") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = services,
              onValueChange = { services = it },
              label = { Text("Services Included (separated by | or comma)") },
              placeholder = { Text("e.g. 10 4K Reels|Brand Book PDF|Landing Page") },
              minLines = 3,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = timeline,
                onValueChange = { timeline = it },
                label = { Text("Timeline") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
              OutlinedTextField(
                value = revisions,
                onValueChange = { revisions = it },
                label = { Text("Revisions") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
            }

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
              label = { Text("Special Terms / Notes") },
              minLines = 2,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = {
              if (clientName.isNotBlank() && packageName.isNotBlank()) {
                val token = "CP-" + Random.nextInt(1000, 9999)
                val parsedPrice = price.toDoubleOrNull() ?: 25000.0
                val formatted = "₹${String.format("%,.0f", parsedPrice)}"
                val cleanServices = services.replace(",", "|").split("|")
                  .map { it.trim() }
                  .filter { it.isNotEmpty() }
                  .joinToString("|")

                val entity = CustomPackageEntity(
                  shareToken = token,
                  clientName = clientName.trim(),
                  packageName = packageName.trim(),
                  category = category,
                  price = parsedPrice,
                  priceFormatted = formatted,
                  services = cleanServices,
                  deliverables = deliverables.trim(),
                  timeline = timeline.trim(),
                  revisions = revisions.trim(),
                  notes = notes.trim()
                )

                onCreateCustomPackage(entity) {
                  showCreateDialog = false
                  Toast.makeText(context, "Custom quote $token generated!", Toast.LENGTH_SHORT).show()
                }
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Generate Proposal Link", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
