package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_packages")
data class CustomPackageEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val shareToken: String, // e.g. "CP-7489"
  val clientName: String,
  val packageName: String,
  val category: String,
  val price: Double,
  val priceFormatted: String,
  val services: String, // pipe '|' separated
  val deliverables: String,
  val timeline: String,
  val revisions: String,
  val notes: String,
  val createdAt: Long = System.currentTimeMillis()
) {
  val serviceList: List<String>
    get() = if (services.isBlank()) emptyList() else services.split("|").map { it.trim() }.filter { it.isNotEmpty() }
}
