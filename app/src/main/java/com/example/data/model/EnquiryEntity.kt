package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enquiries")
data class EnquiryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val enquiryId: String, // e.g. "AUERAA-ENQ-2026-1042"
  val clientName: String,
  val phone: String,
  val whatsapp: String = "",
  val email: String = "",
  val businessName: String = "",
  val businessType: String = "",
  val location: String = "",
  val website: String = "",
  val instagram: String = "",
  val facebook: String = "",
  val service: String, // "Content Creation", "Digital Marketing", "Website Development"
  val packageName: String,
  val budget: String = "",
  val requirements: String = "",
  val preferredContact: String = "WhatsApp", // "WhatsApp", "Phone Call", "Email"
  val preferredTime: String = "Morning (9 AM - 12 PM)",
  val status: String = "New", // "New", "Contacted", "Follow-up", "Proposal Sent", "Converted", "Closed", "Rejected"
  val adminNotes: String = "",
  val followUpDate: String = "",
  val paymentStatus: String = "Payment Pending", // "Payment Pending", "Payment Received", "N/A"
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)
