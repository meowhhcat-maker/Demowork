package com.example.ui.admin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminSettingsEntity
import com.example.ui.theme.BrandCyanPrimary

@Composable
fun AdminSettingsScreen(
  settings: AdminSettingsEntity,
  onSaveSettings: (AdminSettingsEntity) -> Unit,
  onResetCatalog: () -> Unit
) {
  val context = LocalContext.current

  var businessName by remember { mutableStateOf(settings.businessName) }
  var tagline by remember { mutableStateOf(settings.tagline) }
  var email by remember { mutableStateOf(settings.email) }
  var phone by remember { mutableStateOf(settings.phone) }
  var whatsapp by remember { mutableStateOf(settings.whatsapp) }
  var address by remember { mutableStateOf(settings.address) }
  var instagram by remember { mutableStateOf(settings.instagram) }
  var facebook by remember { mutableStateOf(settings.facebook) }
  var youtube by remember { mutableStateOf(settings.youtube) }
  var linkedin by remember { mutableStateOf(settings.linkedin) }
  var businessHours by remember { mutableStateOf(settings.businessHours) }
  var notificationEmail by remember { mutableStateOf(settings.notificationEmail) }
  var autoResponseMsg by remember { mutableStateOf(settings.autoResponseMsg) }
  var adminPin by remember { mutableStateOf(settings.adminPin) }

  var showResetConfirm by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp)
      .testTag("admin_settings_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Agency Configuration & Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)

    // Section 1: Business Identity
    SettingsCard("Business Information") {
      OutlinedTextField(
        value = businessName,
        onValueChange = { businessName = it },
        label = { Text("Agency Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = tagline,
        onValueChange = { tagline = it },
        label = { Text("Brand Tagline") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = address,
        onValueChange = { address = it },
        label = { Text("Studio Office Address") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = businessHours,
        onValueChange = { businessHours = it },
        label = { Text("Operating Business Hours") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
    }

    // Section 2: Contact & WhatsApp Channels
    SettingsCard("Contact & Channels") {
      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Official Phone") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = whatsapp,
        onValueChange = { whatsapp = it },
        label = { Text("WhatsApp Business Number (with country code)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Contact Email") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
    }

    // Section 3: Social Profiles
    SettingsCard("Social Media Handles") {
      OutlinedTextField(
        value = instagram,
        onValueChange = { instagram = it },
        label = { Text("Instagram Handle") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = facebook,
        onValueChange = { facebook = it },
        label = { Text("Facebook URL") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = linkedin,
        onValueChange = { linkedin = it },
        label = { Text("LinkedIn URL") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = youtube,
        onValueChange = { youtube = it },
        label = { Text("YouTube URL") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
    }

    // Section 4: Enquiry & Auto-Response
    SettingsCard("Enquiry & Notifications") {
      OutlinedTextField(
        value = notificationEmail,
        onValueChange = { notificationEmail = it },
        label = { Text("Notification Email for New Leads") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )
      OutlinedTextField(
        value = autoResponseMsg,
        onValueChange = { autoResponseMsg = it },
        label = { Text("Client Auto-Response Message") },
        minLines = 2,
        maxLines = 4,
        modifier = Modifier.fillMaxWidth()
      )
    }

    // Section 5: Security PIN
    SettingsCard("Security Credentials") {
      OutlinedTextField(
        value = adminPin,
        onValueChange = { adminPin = it },
        label = { Text("Admin Security PIN") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("settings_admin_pin")
      )
      Text(
        text = "This PIN protects the Admin dashboard and customer enquiry pipeline.",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Save Settings Button
    Button(
      onClick = {
        val updated = settings.copy(
          businessName = businessName.trim(),
          tagline = tagline.trim(),
          email = email.trim(),
          phone = phone.trim(),
          whatsapp = whatsapp.trim(),
          address = address.trim(),
          instagram = instagram.trim(),
          facebook = facebook.trim(),
          youtube = youtube.trim(),
          linkedin = linkedin.trim(),
          businessHours = businessHours.trim(),
          notificationEmail = notificationEmail.trim(),
          autoResponseMsg = autoResponseMsg.trim(),
          adminPin = adminPin.trim()
        )
        onSaveSettings(updated)
        Toast.makeText(context, "Agency settings saved successfully!", Toast.LENGTH_SHORT).show()
      },
      colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth().testTag("save_settings_btn")
    ) {
      Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Save All Settings", color = Color.Black, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Factory Reset Button
    OutlinedButton(
      onClick = { showResetConfirm = true },
      colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Icon(Icons.Default.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.error)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Restore Default Agency Catalog")
    }

    Spacer(modifier = Modifier.height(80.dp))
  }

  if (showResetConfirm) {
    AlertDialog(
      onDismissRequest = { showResetConfirm = false },
      title = { Text("Restore Default Packages?") },
      text = { Text("This will reload all 60 initial agency packages and reset settings to default presets.") },
      confirmButton = {
        Button(
          onClick = {
            onResetCatalog()
            showResetConfirm = false
            Toast.makeText(context, "Catalog restored to default!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Confirm Reset")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirm = false }) { Text("Cancel") }
      }
    )
  }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BrandCyanPrimary)
      content()
    }
  }
}
