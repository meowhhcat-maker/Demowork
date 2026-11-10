package com.example.ui.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.EnquiryEntity
import com.example.ui.theme.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEnquiriesScreen(
  enquiries: List<EnquiryEntity>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedStatus: String,
  onStatusChange: (String) -> Unit,
  selectedService: String,
  onServiceChange: (String) -> Unit,
  onUpdateStatus: (EnquiryEntity, String) -> Unit,
  onUpdatePayment: (EnquiryEntity, String) -> Unit,
  onUpdateNotes: (EnquiryEntity, String, String) -> Unit,
  onDeleteEnquiry: (EnquiryEntity) -> Unit
) {
  val context = LocalContext.current
  val statuses = listOf("All", "New", "Contacted", "Follow-up", "Proposal Sent", "Converted", "Closed", "Rejected")
  val services = listOf("All", "Content Creation", "Digital Marketing", "Website Development")

  var selectedEnquiryForDetail by remember { mutableStateOf<EnquiryEntity?>(null) }
  val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("admin_enquiries_screen")
  ) {
    // Header
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
      Text(
        text = "Lead & Client Enquiries",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "${enquiries.size} Enquiries in CRM Pipeline",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Search Field
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search by name, ID, phone, company...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("admin_enquiry_search")
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Status Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        statuses.forEach { status ->
          FilterChip(
            selected = selectedStatus == status,
            onClick = { onStatusChange(status) },
            label = { Text(status, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandCyanPrimary.copy(alpha = 0.2f),
              selectedLabelColor = BrandCyanPrimary
            )
          )
        }
      }
    }

    if (enquiries.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(14.dp))
          Text("No enquiries match your search or filter.", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(enquiries) { enq ->
          val statusColor = when (enq.status) {
            "New" -> BrandCyanPrimary
            "Contacted" -> BrandVioletPrimary
            "Follow-up" -> BrandAmber
            "Proposal Sent" -> Color(0xFF38BDF8)
            "Converted" -> BrandEmeraldPrimary
            "Closed" -> MaterialTheme.colorScheme.onSurfaceVariant
            "Rejected" -> MaterialTheme.colorScheme.error
            else -> BrandCyanPrimary
          }

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedEnquiryForDetail = enq }
              .testTag("enquiry_card_${enq.enquiryId}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Top Row: ID, Date, Status
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = enq.enquiryId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BrandCyanPrimary
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = dateFormat.format(Date(enq.createdAt)),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Surface(
                  color = statusColor.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = enq.status,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = enq.clientName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              )

              if (enq.businessName.isNotBlank()) {
                Text(
                  text = "${enq.businessName} • ${enq.businessType}",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Package & Budget
              Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text("Requested Package", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(enq.packageName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  }
                  Column(horizontalAlignment = Alignment.End) {
                    Text("Budget", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(enq.budget.ifBlank { "Not specified" }, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandCyanPrimary)
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Quick Actions: Call, WhatsApp, Details
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  // Call button
                  IconButton(
                    onClick = {
                      try {
                        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${enq.phone}")))
                      } catch (_: Exception) {}
                    },
                    modifier = Modifier.size(34.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)
                  ) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = BrandCyanPrimary, modifier = Modifier.size(16.dp))
                  }

                  // WhatsApp button
                  IconButton(
                    onClick = {
                      try {
                        val num = (if (enq.whatsapp.isNotBlank()) enq.whatsapp else enq.phone).replace(Regex("[^0-9+]"), "")
                        val msg = URLEncoder.encode("Hi ${enq.clientName}, this is regarding your enquiry (${enq.enquiryId}) for AUERAA.DIGITAL ${enq.packageName}.", StandardCharsets.UTF_8.toString())
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$num&text=$msg")))
                      } catch (_: Exception) {
                        Toast.makeText(context, "Cannot open WhatsApp", Toast.LENGTH_SHORT).show()
                      }
                    },
                    modifier = Modifier.size(34.dp).clip(CircleShape).background(WhatsAppGreen.copy(alpha = 0.15f))
                  ) {
                    Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = WhatsAppGreen, modifier = Modifier.size(16.dp))
                  }
                }

                TextButton(onClick = { selectedEnquiryForDetail = enq }) {
                  Text("Manage Lead", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandCyanPrimary)
                  Icon(Icons.Default.ArrowForward, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(14.dp))
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

  // ---------------------------------------------
  // FULL ENQUIRY DETAIL & MANAGEMENT MODAL
  // ---------------------------------------------
  if (selectedEnquiryForDetail != null) {
    val enq = selectedEnquiryForDetail!!
    var currentStatus by remember { mutableStateOf(enq.status) }
    var currentPayment by remember { mutableStateOf(enq.paymentStatus) }
    var notesText by remember { mutableStateOf(enq.adminNotes) }
    var followUpText by remember { mutableStateOf(enq.followUpDate) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { selectedEnquiryForDetail = null }) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(max = 640.dp)
          .testTag("manage_enquiry_modal")
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          // Modal Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = enq.clientName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
              Text(text = "ID: ${enq.enquiryId}", fontSize = 11.sp, color = BrandCyanPrimary)
            }
            IconButton(onClick = { selectedEnquiryForDetail = null }) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Column(
            modifier = Modifier
              .weight(1f)
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            // Client Contact Grid
            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Phone: ${enq.phone}", fontSize = 13.sp)
                if (enq.whatsapp.isNotBlank()) Text("WhatsApp: ${enq.whatsapp}", fontSize = 13.sp, color = WhatsAppGreen)
                if (enq.email.isNotBlank()) Text("Email: ${enq.email}", fontSize = 13.sp)
                if (enq.businessName.isNotBlank()) Text("Business: ${enq.businessName} (${enq.businessType})", fontSize = 13.sp)
                if (enq.location.isNotBlank()) Text("Location: ${enq.location}", fontSize = 13.sp)
                if (enq.website.isNotBlank()) Text("Website: ${enq.website}", fontSize = 13.sp)
                Text("Preferred Contact: ${enq.preferredContact} • ${enq.preferredTime}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            // Project Requirements
            Column {
              Text("Project Requirements", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = enq.requirements.ifBlank { "No detailed notes provided." },
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
            }

            // Change Status Selector
            Column {
              Text("Lead Pipeline Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("New", "Contacted", "Follow-up", "Proposal Sent", "Converted", "Closed", "Rejected").forEach { st ->
                  FilterChip(
                    selected = currentStatus == st,
                    onClick = {
                      currentStatus = st
                      onUpdateStatus(enq, st)
                    },
                    label = { Text(st, fontSize = 11.sp) }
                  )
                }
              }
            }

            // Payment Status (Payment Pending / Payment Received)
            Column {
              Text("Payment Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Payment Pending", "Payment Received").forEach { pay ->
                  FilterChip(
                    selected = currentPayment == pay,
                    onClick = {
                      currentPayment = pay
                      onUpdatePayment(enq, pay)
                    },
                    label = { Text(pay, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = if (pay == "Payment Received") BrandEmeraldPrimary.copy(alpha = 0.2f) else BrandAmber.copy(alpha = 0.2f),
                      selectedLabelColor = if (pay == "Payment Received") BrandEmeraldPrimary else BrandAmber
                    )
                  )
                }
              }
            }

            // Admin Notes & Follow-up Date
            OutlinedTextField(
              value = followUpText,
              onValueChange = { followUpText = it },
              label = { Text("Next Follow-up Date / Time") },
              placeholder = { Text("e.g. Tomorrow 11:00 AM") },
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = notesText,
              onValueChange = { notesText = it },
              label = { Text("Internal Admin CRM Notes") },
              placeholder = { Text("Record discussion notes, agreed budget, proposal link...") },
              minLines = 3,
              maxLines = 5,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Save Notes & Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                onUpdateNotes(enq, notesText, followUpText)
                Toast.makeText(context, "Lead notes updated successfully!", Toast.LENGTH_SHORT).show()
                selectedEnquiryForDetail = null
              },
              colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            IconButton(
              onClick = { showDeleteConfirm = true },
              modifier = Modifier.size(44.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
          }
        }
      }
    }

    if (showDeleteConfirm) {
      AlertDialog(
        onDismissRequest = { showDeleteConfirm = false },
        title = { Text("Delete Enquiry?") },
        text = { Text("Are you sure you want to delete enquiry ${enq.enquiryId} from ${enq.clientName}?") },
        confirmButton = {
          Button(
            onClick = {
              onDeleteEnquiry(enq)
              showDeleteConfirm = false
              selectedEnquiryForDetail = null
              Toast.makeText(context, "Enquiry deleted", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
          ) {
            Text("Delete")
          }
        },
        dismissButton = {
          TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
        }
      )
    }
  }
}
