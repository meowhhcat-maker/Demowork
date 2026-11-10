package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packages")
data class PackageEntity(
  @PrimaryKey val id: String,
  val name: String,
  val category: String, // "Content Creation", "Digital Marketing", "Website Development"
  val price: Double,
  val priceFormatted: String, // e.g. "₹14,999", "₹22,000/month"
  val billingType: String, // "One-Time", "Monthly", "Starting at"
  val bestFor: String,
  val description: String,
  val services: String, // separated by pipe '|'
  val deliverables: String,
  val notes: String,
  val featured: Boolean = false,
  val active: Boolean = true,
  val displayOrder: Int = 0,
  val isFavorite: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
) {
  val serviceList: List<String>
    get() = if (services.isBlank()) emptyList() else services.split("|").map { it.trim() }.filter { it.isNotEmpty() }
}
