package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AgencyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class AgencyViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = AgencyRepository(application)

  // Navigation State
  private val _currentRoute = MutableStateFlow("home")
  val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

  fun navigateTo(route: String) {
    _currentRoute.value = route
  }

  // Dark Theme
  private val _isDarkTheme = MutableStateFlow(true)
  val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
  fun toggleTheme() {
    _isDarkTheme.value = !_isDarkTheme.value
  }

  // Packages Flow
  val allPackages: StateFlow<List<PackageEntity>> = repository.allPackages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val favoritePackages: StateFlow<List<PackageEntity>> = repository.favoritePackages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Package Filters & Search
  val searchQuery = MutableStateFlow("")
  val selectedCategoryFilter = MutableStateFlow("All")
  val selectedBudgetFilter = MutableStateFlow("All")
  val selectedBusinessTypeFilter = MutableStateFlow("All")

  val filteredPackages: StateFlow<List<PackageEntity>> = combine(
    allPackages,
    searchQuery,
    selectedCategoryFilter,
    selectedBudgetFilter,
    selectedBusinessTypeFilter
  ) { packages, query, category, budget, businessType ->
    packages.filter { pkg ->
      val matchesQuery = query.isBlank() ||
        pkg.name.contains(query, ignoreCase = true) ||
        pkg.description.contains(query, ignoreCase = true) ||
        pkg.bestFor.contains(query, ignoreCase = true) ||
        pkg.services.contains(query, ignoreCase = true)

      val matchesCategory = category == "All" || pkg.category.equals(category, ignoreCase = true)

      val matchesBudget = when (budget) {
        "All" -> true
        "Under ₹10,000" -> pkg.price < 10000
        "₹10,000–₹25,000" -> pkg.price in 10000.0..25000.0
        "₹25,000–₹50,000" -> pkg.price in 25000.0..50000.0
        "₹50,000–₹1,00,000" -> pkg.price in 50000.0..100000.0
        "₹1,00,000+" -> pkg.price >= 100000.0
        else -> true
      }

      val matchesBusinessType = if (businessType == "All") true else {
        pkg.bestFor.contains(businessType, ignoreCase = true) ||
          pkg.description.contains(businessType, ignoreCase = true)
      }

      pkg.active && matchesQuery && matchesCategory && matchesBudget && matchesBusinessType
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Selected Package for Detail View
  private val _selectedPackageId = MutableStateFlow<String?>(null)
  val selectedPackageId: StateFlow<String?> = _selectedPackageId.asStateFlow()

  val selectedPackage: StateFlow<PackageEntity?> = combine(
    allPackages,
    _selectedPackageId
  ) { packages, id ->
    packages.find { it.id == id }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  fun selectPackageForDetail(id: String) {
    _selectedPackageId.value = id
    navigateTo("package_detail")
  }

  // Package Comparison (Up to 3)
  private val _comparisonPackageIds = MutableStateFlow<List<String>>(emptyList())
  val comparisonPackageIds: StateFlow<List<String>> = _comparisonPackageIds.asStateFlow()

  val comparisonPackages: StateFlow<List<PackageEntity>> = combine(
    allPackages,
    _comparisonPackageIds
  ) { packages, ids ->
    packages.filter { it.id in ids }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun toggleComparison(packageId: String) {
    val current = _comparisonPackageIds.value.toMutableList()
    if (current.contains(packageId)) {
      current.remove(packageId)
    } else {
      if (current.size >= 3) {
        current.removeAt(0)
      }
      current.add(packageId)
    }
    _comparisonPackageIds.value = current
  }

  fun clearComparison() {
    _comparisonPackageIds.value = emptyList()
  }

  // Favorites
  fun toggleFavorite(packageId: String, currentFav: Boolean) {
    viewModelScope.launch {
      repository.toggleFavorite(packageId, !currentFav)
    }
  }

  // Enquiry Pre-selection
  val enquiryPrefilledPackage = MutableStateFlow<String?>(null)
  val enquiryPrefilledService = MutableStateFlow<String?>("Content Creation")

  fun startEnquiryForPackage(pkg: PackageEntity) {
    enquiryPrefilledPackage.value = pkg.name
    enquiryPrefilledService.value = pkg.category
    navigateTo("enquiry")
  }

  // Enquiry Submission
  private val _lastSubmittedEnquiry = MutableStateFlow<EnquiryEntity?>(null)
  val lastSubmittedEnquiry: StateFlow<EnquiryEntity?> = _lastSubmittedEnquiry.asStateFlow()

  fun submitEnquiry(enquiry: EnquiryEntity, onComplete: (String) -> Unit) {
    viewModelScope.launch {
      repository.submitEnquiry(enquiry)
      _lastSubmittedEnquiry.value = enquiry
      onComplete(enquiry.enquiryId)
    }
  }

  fun clearLastSubmittedEnquiry() {
    _lastSubmittedEnquiry.value = null
  }

  // Enquiries (Admin)
  val allEnquiries: StateFlow<List<EnquiryEntity>> = repository.allEnquiries
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val adminEnquirySearch = MutableStateFlow("")
  val adminEnquiryStatusFilter = MutableStateFlow("All")
  val adminEnquiryServiceFilter = MutableStateFlow("All")

  val filteredEnquiries: StateFlow<List<EnquiryEntity>> = combine(
    allEnquiries,
    adminEnquirySearch,
    adminEnquiryStatusFilter,
    adminEnquiryServiceFilter
  ) { enquiries, search, status, service ->
    enquiries.filter { enq ->
      val matchesSearch = search.isBlank() ||
        enq.clientName.contains(search, ignoreCase = true) ||
        enq.enquiryId.contains(search, ignoreCase = true) ||
        enq.phone.contains(search, ignoreCase = true) ||
        enq.businessName.contains(search, ignoreCase = true) ||
        enq.packageName.contains(search, ignoreCase = true)

      val matchesStatus = status == "All" || enq.status.equals(status, ignoreCase = true)
      val matchesService = service == "All" || enq.service.equals(service, ignoreCase = true)

      matchesSearch && matchesStatus && matchesService
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun updateEnquiryStatus(enquiry: EnquiryEntity, newStatus: String) {
    viewModelScope.launch {
      repository.updateEnquiry(enquiry.copy(status = newStatus))
    }
  }

  fun updateEnquiryPayment(enquiry: EnquiryEntity, newPaymentStatus: String) {
    viewModelScope.launch {
      repository.updateEnquiry(enquiry.copy(paymentStatus = newPaymentStatus))
    }
  }

  fun updateEnquiryNotes(enquiry: EnquiryEntity, notes: String, followUp: String) {
    viewModelScope.launch {
      repository.updateEnquiry(enquiry.copy(adminNotes = notes, followUpDate = followUp))
    }
  }

  fun deleteEnquiry(enquiry: EnquiryEntity) {
    viewModelScope.launch {
      repository.deleteEnquiry(enquiry)
    }
  }

  // Dynamic Package Management (Admin)
  fun saveOrUpdatePackage(pkg: PackageEntity) {
    viewModelScope.launch {
      repository.savePackage(pkg)
    }
  }

  fun deletePackage(pkg: PackageEntity) {
    viewModelScope.launch {
      repository.deletePackage(pkg)
    }
  }

  fun duplicatePackage(pkg: PackageEntity, onComplete: (String) -> Unit) {
    viewModelScope.launch {
      val newId = repository.duplicatePackage(pkg)
      onComplete(newId)
    }
  }

  // Custom Packages
  val allCustomPackages: StateFlow<List<CustomPackageEntity>> = repository.allCustomPackages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _activeCustomToken = MutableStateFlow<String?>(null)
  val activeCustomToken: StateFlow<String?> = _activeCustomToken.asStateFlow()

  val activeCustomPackage: StateFlow<CustomPackageEntity?> = combine(
    allCustomPackages,
    _activeCustomToken
  ) { packages, token ->
    packages.find { it.shareToken == token }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  fun openCustomPackage(token: String) {
    _activeCustomToken.value = token
    navigateTo("custom_package")
  }

  fun createCustomPackage(pkg: CustomPackageEntity, onCreated: (String) -> Unit) {
    viewModelScope.launch {
      repository.saveCustomPackage(pkg)
      onCreated(pkg.shareToken)
    }
  }

  fun deleteCustomPackage(pkg: CustomPackageEntity) {
    viewModelScope.launch {
      repository.deleteCustomPackage(pkg)
    }
  }

  // Admin Settings
  val adminSettings: StateFlow<AdminSettingsEntity> = repository.adminSettings
    .map { it ?: AdminSettingsEntity() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminSettingsEntity())

  fun updateAdminSettings(settings: AdminSettingsEntity) {
    viewModelScope.launch {
      repository.updateAdminSettings(settings)
    }
  }

  fun resetCatalogToDefaults() {
    viewModelScope.launch {
      repository.resetCatalog()
    }
  }

  // Admin Notifications
  val notifications: StateFlow<List<AdminNotificationEntity>> = repository.notifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun markNotificationRead(id: Long) {
    viewModelScope.launch {
      repository.markNotificationRead(id)
    }
  }

  fun clearAllNotifications() {
    viewModelScope.launch {
      repository.clearNotifications()
    }
  }

  // Admin Security Auth
  private val _isAdminAuthenticated = MutableStateFlow(false)
  val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

  fun authenticateAdmin(pin: String): Boolean {
    val correctPin = adminSettings.value.adminPin
    val success = pin == correctPin
    if (success) {
      _isAdminAuthenticated.value = true
    }
    return success
  }

  fun logoutAdmin() {
    _isAdminAuthenticated.value = false
    navigateTo("home")
  }

  // Helpers for WhatsApp & External Sharing
  fun openWhatsApp(context: Context, packageName: String? = null, customMessage: String? = null) {
    val phone = adminSettings.value.whatsapp.replace(Regex("[^0-9+]"), "")
    val message = customMessage ?: if (packageName != null) {
      "Hi AUERAA.DIGITAL, I'm interested in the $packageName package. I would like to know more about the services and process."
    } else {
      "Hi AUERAA.DIGITAL, I would like to discuss digital solutions for my business."
    }

    try {
      val encoded = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
      val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phone&text=$encoded")
      val intent = Intent(Intent.ACTION_VIEW, uri)
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show()
    }
  }

  fun sharePackage(context: Context, pkg: PackageEntity) {
    val text = """
      *AUERAA.DIGITAL* - ${pkg.name}
      Price: ${pkg.priceFormatted} (${pkg.billingType})
      Best For: ${pkg.bestFor}
      
      Services Included:
      ${pkg.serviceList.joinToString("\n") { "• $it" }}
      
      Enquire now: hello@aueraa.digital | WhatsApp: ${adminSettings.value.whatsapp}
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "AUERAA.DIGITAL - ${pkg.name}")
      putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Package via"))
  }
}
