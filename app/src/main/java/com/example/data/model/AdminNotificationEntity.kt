package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_notifications")
data class AdminNotificationEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val message: String,
  val enquiryId: String,
  val clientName: String,
  val service: String,
  val packageName: String,
  val phone: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false
)
