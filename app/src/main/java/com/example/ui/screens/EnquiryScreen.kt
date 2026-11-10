package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.EnquiryEntity
import com.example.data.model.PackageEntity
import com.example.ui.components.WhatsAppButton
import com.example.ui.theme.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnquiryScreen(
  allPackages: List<PackageEntity>,
  prefilledService: String?,
  prefilledPackage: String?,
  onBack: () -> Unit,
  onSubmitEnquiry: (EnquiryEntity, (String) -> Unit) -> Unit,
  onOpenWhatsApp: (String?) -> Unit
) {
  val context = LocalContext.current

  // Form Fields
  var fullName by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var whatsappNumber by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var businessName by remember { mutableStateOf("") }

  // Project Info
  val serviceOptions = listOf("Content Creation", "Digital Marketing", "Website Development")
  var selectedService by remember { mutableStateOf(prefilledService ?: "Content Creation") }

  // Packages dynamically filtered by selected service
  val packageOptionsForService = remember(selectedService, allPackages) {
    val list = allPackages
      .filter { it.category == selectedService && it.active }
      .map { it.name }
      .toMutableList()
    list.add(0, "Custom Package / Not Sure")
    list
  }

  var selectedPackage by remember {
    mutableStateOf(prefilledPackage ?: "Custom Package / Not Sure")
  }

  // Update selected package if service changes
  LaunchedEffect(selectedService) {
    if (prefilledPackage == null || !packageOptionsForService.contains(selectedPackage)) {
      selectedPackage = packageOptionsForService.firstOrNull() ?: "Custom Package / Not Sure"
    }
  }

  // Business Info
  var businessType by remember { mutableStateOf("Small Business") }
  val businessTypeOptions = listOf(
    "Small Business", "Startup", "Restaurant", "Hotel", "Resort",
    "E-commerce", "Real Estate", "Personal Brand", "Corporate", "Creator", "Education", "Other"
  )

  var location by remember { mutableStateOf("") }
  var websiteUrl by remember { mutableStateOf("") }
  var instagramUrl by remember { mutableStateOf("") }
  var facebookUrl by remember { mutableStateOf("") }

  // Requirements
  var requirements by remember { mutableStateOf("") }

  // Budget
  val budgetOptions = listOf(
    "Under ₹10,000", "₹10,000–₹25,000", "₹25,000–₹50,000",
    "₹50,000–₹1,00,000", "₹1,00,000+", "Not Sure"
  )
  var selectedBudget by remember { mutableStateOf("₹10,000–₹25,000") }

  // Preferred Contact
  val contactMethodOptions = listOf("WhatsApp", "Phone Call", "Email")
  var preferredContact by remember { mutableStateOf("WhatsApp") }

  val contactTimeOptions = listOf(
    "Morning (9 AM - 12 PM)", "Afternoon (12 PM - 4 PM)",
    "Evening (4 PM - 8 PM)", "Flexible / Anytime"
  )
  var preferredTime by remember { mutableStateOf("Morning (9 AM - 12 PM)") }

  // UI Dropdown States
  var serviceDropdownExpanded by remember { mutableStateOf(false) }
  var packageDropdownExpanded by remember { mutableStateOf(false) }
  var budgetDropdownExpanded by remember { mutableStateOf(false) }
  var businessTypeDropdownExpanded by remember { mutableStateOf(false) }
  var timeDropdownExpanded by remember { mutableStateOf(false) }

  // Validation State
  var nameError by remember { mutableStateOf(false) }
  var phoneError by remember { mutableStateOf(false) }

  // Success Dialog State
  var submittedEnquiryId by remember { mutableStateOf<String?>(null) }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("enquiry_screen")
  ) {
    // ---------------------------------------------
    // TOP BAR
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
          text = "Client Project Enquiry",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    }

    // ---------------------------------------------
    // FORM BODY
    // ---------------------------------------------
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      // Intro Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandCyanPrimary.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, BrandCyanPrimary.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Tell Us About Your Vision",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = BrandCyanPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Fill in your requirements below. Our agency strategist will contact you with a customized proposal.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 17.sp
          )
        }
      }

      // SECTION 1: Personal Information
      FormSectionHeader(title = "1. Personal Information")

      OutlinedTextField(
        value = fullName,
        onValueChange = {
          fullName = it
          if (it.isNotBlank()) nameError = false
        },
        label = { Text("Full Name *") },
        isError = nameError,
        supportingText = { if (nameError) Text("Full name is required", color = MaterialTheme.colorScheme.error) },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_fullname_input")
      )

      OutlinedTextField(
        value = phone,
        onValueChange = {
          phone = it
          if (it.isNotBlank()) phoneError = false
        },
        label = { Text("Phone Number *") },
        isError = phoneError,
        supportingText = { if (phoneError) Text("Valid phone number is required", color = MaterialTheme.colorScheme.error) },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_phone_input")
      )

      OutlinedTextField(
        value = whatsappNumber,
        onValueChange = { whatsappNumber = it },
        label = { Text("WhatsApp Number (if different)") },
        leadingIcon = { Icon(Icons.Default.Chat, contentDescription = null, tint = WhatsAppGreen) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_whatsapp_input")
      )

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email Address") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_email_input")
      )

      OutlinedTextField(
        value = businessName,
        onValueChange = { businessName = it },
        label = { Text("Company / Business Name") },
        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_company_input")
      )

      // SECTION 2: Project Information
      FormSectionHeader(title = "2. Project Information")

      // Select Service Dropdown
      ExposedDropdownMenuBox(
        expanded = serviceDropdownExpanded,
        onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded }
      ) {
        OutlinedTextField(
          value = selectedService,
          onValueChange = {},
          readOnly = true,
          label = { Text("Select Service *") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().menuAnchor().testTag("enquiry_service_selector")
        )
        ExposedDropdownMenu(
          expanded = serviceDropdownExpanded,
          onDismissRequest = { serviceDropdownExpanded = false }
        ) {
          serviceOptions.forEach { opt ->
            DropdownMenuItem(
              text = { Text(opt) },
              onClick = {
                selectedService = opt
                serviceDropdownExpanded = false
              }
            )
          }
        }
      }

      // Select Package Dropdown (Auto updates based on selected service)
      ExposedDropdownMenuBox(
        expanded = packageDropdownExpanded,
        onExpandedChange = { packageDropdownExpanded = !packageDropdownExpanded }
      ) {
        OutlinedTextField(
          value = selectedPackage,
          onValueChange = {},
          readOnly = true,
          label = { Text("Select Package") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = packageDropdownExpanded) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().menuAnchor().testTag("enquiry_package_selector")
        )
        ExposedDropdownMenu(
          expanded = packageDropdownExpanded,
          onDismissRequest = { packageDropdownExpanded = false }
        ) {
          packageOptionsForService.forEach { pkgName ->
            DropdownMenuItem(
              text = { Text(pkgName) },
              onClick = {
                selectedPackage = pkgName
                packageDropdownExpanded = false
              }
            )
          }
        }
      }

      // SECTION 3: Business Information
      FormSectionHeader(title = "3. Business Details")

      // Business Type Dropdown
      ExposedDropdownMenuBox(
        expanded = businessTypeDropdownExpanded,
        onExpandedChange = { businessTypeDropdownExpanded = !businessTypeDropdownExpanded }
      ) {
        OutlinedTextField(
          value = businessType,
          onValueChange = {},
          readOnly = true,
          label = { Text("Business Type") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = businessTypeDropdownExpanded) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
          expanded = businessTypeDropdownExpanded,
          onDismissRequest = { businessTypeDropdownExpanded = false }
        ) {
          businessTypeOptions.forEach { bt ->
            DropdownMenuItem(
              text = { Text(bt) },
              onClick = {
                businessType = bt
                businessTypeDropdownExpanded = false
              }
            )
          }
        }
      }

      OutlinedTextField(
        value = location,
        onValueChange = { location = it },
        label = { Text("Business Location (City / State)") },
        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
        value = websiteUrl,
        onValueChange = { websiteUrl = it },
        label = { Text("Website URL (if existing)") },
        leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = instagramUrl,
          onValueChange = { instagramUrl = it },
          label = { Text("Instagram @handle") },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
          value = facebookUrl,
          onValueChange = { facebookUrl = it },
          label = { Text("Facebook URL") },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        )
      }

      // SECTION 4: Requirements
      FormSectionHeader(title = "4. Requirements")

      OutlinedTextField(
        value = requirements,
        onValueChange = { requirements = it },
        label = { Text("Tell us about your requirements") },
        placeholder = { Text("Describe your project, timeline, deliverables, or current pain points...") },
        minLines = 4,
        maxLines = 8,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_requirements_input")
      )

      // Budget Dropdown
      ExposedDropdownMenuBox(
        expanded = budgetDropdownExpanded,
        onExpandedChange = { budgetDropdownExpanded = !budgetDropdownExpanded }
      ) {
        OutlinedTextField(
          value = selectedBudget,
          onValueChange = {},
          readOnly = true,
          label = { Text("Estimated Budget") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = budgetDropdownExpanded) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().menuAnchor().testTag("enquiry_budget_selector")
        )
        ExposedDropdownMenu(
          expanded = budgetDropdownExpanded,
          onDismissRequest = { budgetDropdownExpanded = false }
        ) {
          budgetOptions.forEach { b ->
            DropdownMenuItem(
              text = { Text(b) },
              onClick = {
                selectedBudget = b
                budgetDropdownExpanded = false
              }
            )
          }
        }
      }

      // SECTION 5: Contact Preference
      FormSectionHeader(title = "5. Communication Preferences")

      Text(
        text = "Preferred Contact Method",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        contactMethodOptions.forEach { method ->
          FilterChip(
            selected = preferredContact == method,
            onClick = { preferredContact = method },
            label = { Text(method, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandCyanPrimary.copy(alpha = 0.2f),
              selectedLabelColor = BrandCyanPrimary
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Preferred Time Dropdown
      ExposedDropdownMenuBox(
        expanded = timeDropdownExpanded,
        onExpandedChange = { timeDropdownExpanded = !timeDropdownExpanded }
      ) {
        OutlinedTextField(
          value = preferredTime,
          onValueChange = {},
          readOnly = true,
          label = { Text("Preferred Contact Time") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeDropdownExpanded) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
          expanded = timeDropdownExpanded,
          onDismissRequest = { timeDropdownExpanded = false }
        ) {
          contactTimeOptions.forEach { t ->
            DropdownMenuItem(
              text = { Text(t) },
              onClick = {
                preferredTime = t
                timeDropdownExpanded = false
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Submit Button
      Button(
        onClick = {
          nameError = fullName.isBlank()
          phoneError = phone.isBlank()

          if (!nameError && !phoneError) {
            val randomId = "AUERAA-ENQ-2026-${Random.nextInt(1000, 9999)}"
            val enquiry = EnquiryEntity(
              enquiryId = randomId,
              clientName = fullName.trim(),
              phone = phone.trim(),
              whatsapp = if (whatsappNumber.isNotBlank()) whatsappNumber.trim() else phone.trim(),
              email = email.trim(),
              businessName = businessName.trim(),
              businessType = businessType,
              location = location.trim(),
              website = websiteUrl.trim(),
              instagram = instagramUrl.trim(),
              facebook = facebookUrl.trim(),
              service = selectedService,
              packageName = selectedPackage,
              budget = selectedBudget,
              requirements = requirements.trim(),
              preferredContact = preferredContact,
              preferredTime = preferredTime,
              status = "New"
            )

            onSubmitEnquiry(enquiry) { id ->
              submittedEnquiryId = id
            }
          }
        },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_submit_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          tint = Color.Black,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Send Enquiry",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
      }

      Spacer(modifier = Modifier.height(60.dp))
    }
  }

  // ---------------------------------------------
  // SUCCESS DIALOG
  // ---------------------------------------------
  if (submittedEnquiryId != null) {
    Dialog(onDismissRequest = {
      submittedEnquiryId = null
      onBack()
    }) {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth().testTag("enquiry_success_dialog")
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(RoundedCornerShape(18.dp))
              .background(BrandEmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Success",
              tint = BrandEmeraldPrimary,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Thank You for Contacting AUERAA.DIGITAL!",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Your enquiry has been successfully submitted. Our strategic growth team will review your requirements and reach out within 2 hours.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Enquiry ID pill with Copy button
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "ENQUIRY REFERENCE ID",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.8.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = submittedEnquiryId!!,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Black,
                  color = BrandCyanPrimary
                )
              }

              IconButton(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Enquiry ID", submittedEnquiryId)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Enquiry ID copied to clipboard", Toast.LENGTH_SHORT).show()
              }) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = "Copy ID",
                  tint = BrandCyanPrimary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Quick WhatsApp button
          WhatsAppButton(
            text = "Chat on WhatsApp Now",
            onClick = {
              onOpenWhatsApp("Enquiry $submittedEnquiryId for $selectedPackage")
            },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = {
              submittedEnquiryId = null
              onBack()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Done")
          }
        }
      }
    }
  }
}

@Composable
fun FormSectionHeader(title: String) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp,
      color = BrandCyanPrimary
    )
    Spacer(modifier = Modifier.height(4.dp))
    HorizontalDivider(color = BrandCyanPrimary.copy(alpha = 0.2f), thickness = 1.dp)
  }
}
