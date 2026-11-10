package com.example.data.repository

import android.content.Context
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class AgencyRepository(context: Context) {
  private val db = AppDatabase.getDatabase(context)
  private val packageDao = db.packageDao()
  private val enquiryDao = db.enquiryDao()
  private val customPackageDao = db.customPackageDao()
  private val adminSettingsDao = db.adminSettingsDao()
  private val notificationDao = db.notificationDao()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      initializeDefaultsIfNeeded()
    }
  }

  suspend fun initializeDefaultsIfNeeded() {
    if (packageDao.getPackageCount() == 0) {
      packageDao.insertAllPackages(InitialData.getDefaultPackages())
    }
    if (enquiryDao.getEnquiryCount() == 0) {
      enquiryDao.insertAllEnquiries(InitialData.getDefaultEnquiries())
    }
    if (adminSettingsDao.getSettingsDirect() == null) {
      adminSettingsDao.insertOrUpdate(AdminSettingsEntity())
    }
  }

  // --- Packages ---
  val allPackages: Flow<List<PackageEntity>> = packageDao.getAllPackages()

  fun getPackageById(id: String): Flow<PackageEntity?> = packageDao.getPackageById(id)

  fun getPackagesByCategory(category: String): Flow<List<PackageEntity>> =
    packageDao.getPackagesByCategory(category)

  val favoritePackages: Flow<List<PackageEntity>> = packageDao.getFavoritePackages()

  suspend fun savePackage(pkg: PackageEntity) {
    packageDao.insertPackage(pkg)
  }

  suspend fun updatePackage(pkg: PackageEntity) {
    packageDao.updatePackage(pkg.copy(updatedAt = System.currentTimeMillis()))
  }

  suspend fun deletePackage(pkg: PackageEntity) {
    packageDao.deletePackage(pkg)
  }

  suspend fun duplicatePackage(pkg: PackageEntity): String {
    val newId = "PKG-" + UUID.randomUUID().toString().substring(0, 6).uppercase()
    val duplicate = pkg.copy(
      id = newId,
      name = "${pkg.name} (Copy)",
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )
    packageDao.insertPackage(duplicate)
    return newId
  }

  suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
    packageDao.updateFavorite(id, isFavorite)
  }

  // --- Enquiries ---
  val allEnquiries: Flow<List<EnquiryEntity>> = enquiryDao.getAllEnquiries()

  fun getEnquiryById(id: Long): Flow<EnquiryEntity?> = enquiryDao.getEnquiryById(id)

  suspend fun submitEnquiry(enquiry: EnquiryEntity): Long {
    val id = enquiryDao.insertEnquiry(enquiry)
    // Create admin notification
    notificationDao.insertNotification(
      AdminNotificationEntity(
        title = "New Enquiry Received",
        message = "${enquiry.clientName} enquired about ${enquiry.packageName} (${enquiry.service})",
        enquiryId = enquiry.enquiryId,
        clientName = enquiry.clientName,
        service = enquiry.service,
        packageName = enquiry.packageName,
        phone = enquiry.phone,
        timestamp = System.currentTimeMillis()
      )
    )
    return id
  }

  suspend fun updateEnquiry(enquiry: EnquiryEntity) {
    enquiryDao.updateEnquiry(enquiry.copy(updatedAt = System.currentTimeMillis()))
  }

  suspend fun deleteEnquiry(enquiry: EnquiryEntity) {
    enquiryDao.deleteEnquiry(enquiry)
  }

  // --- Custom Packages ---
  val allCustomPackages: Flow<List<CustomPackageEntity>> = customPackageDao.getAllCustomPackages()

  fun getCustomPackageByToken(token: String): Flow<CustomPackageEntity?> =
    customPackageDao.getCustomPackageByToken(token)

  suspend fun saveCustomPackage(pkg: CustomPackageEntity): Long {
    return customPackageDao.insertCustomPackage(pkg)
  }

  suspend fun deleteCustomPackage(pkg: CustomPackageEntity) {
    customPackageDao.deleteCustomPackage(pkg)
  }

  // --- Admin Settings ---
  val adminSettings: Flow<AdminSettingsEntity?> = adminSettingsDao.getSettings()

  suspend fun updateAdminSettings(settings: AdminSettingsEntity) {
    adminSettingsDao.insertOrUpdate(settings)
  }

  // --- Notifications ---
  val notifications: Flow<List<AdminNotificationEntity>> = notificationDao.getAllNotifications()

  suspend fun markNotificationRead(id: Long) {
    notificationDao.markAsRead(id)
  }

  suspend fun clearNotifications() {
    notificationDao.clearAllNotifications()
  }

  // --- Reset to default factory data ---
  suspend fun resetCatalog() {
    packageDao.insertAllPackages(InitialData.getDefaultPackages())
    adminSettingsDao.insertOrUpdate(AdminSettingsEntity())
  }
}
