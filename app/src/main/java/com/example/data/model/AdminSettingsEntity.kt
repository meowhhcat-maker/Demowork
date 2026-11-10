package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_settings")
data class AdminSettingsEntity(
  @PrimaryKey val id: Int = 1,
  val businessName: String = "AUERAA.DIGITAL",
  val tagline: String = "Creative Content. Professional Branding. Digital Growth.",
  val email: String = "hello@aueraa.digital",
  val phone: String = "+91 98765 43210",
  val whatsapp: String = "+91 98765 43210",
  val address: String = "104 Cyber Park, Silicon Heights, Bangalore, India",
  val instagram: String = "@aueraa.digital",
  val facebook: String = "facebook.com/aueraa.digital",
  val youtube: String = "youtube.com/@aueraadigital",
  val linkedin: String = "linkedin.com/company/aueraa-digital",
  val businessHours: String = "Mon - Sat: 9:30 AM - 7:00 PM IST",
  val notificationEmail: String = "leads@aueraa.digital",
  val autoResponseMsg: String = "Thank you for contacting AUERAA.DIGITAL! Our team is reviewing your project requirements and will reach out shortly.",
  val currency: String = "₹",
  val adminPin: String = "1234",
  val contentTerms: String = "All creative assets require 3-5 working days production cycle. High-res raw client media must be provided before shoot/edit kickoff. Up to 2 rounds of creative revisions included per asset.",
  val marketingTerms: String = "Ad spend budget is paid directly to Meta/Google and is separate from agency retainer fees. Performance marketing optimization cycles run on 30-day billing periods. Detailed weekly & monthly KPI reports provided.",
  val webTerms: String = "Domain, cloud hosting, and third-party SaaS plugin fees are paid by client. Final website deployment occurs upon full project sign-off and milestone settlement. Includes 30 days post-launch technical warranty.",
  val paymentTerms: String = "One-time projects: 50% advance booking deposit, 50% upon final sign-off prior to high-res release/deployment. Retainers: 100% upfront at the start of each monthly billing cycle.",
  val revisionTerms: String = "Content Creation includes 2 revision rounds per deliverable. Website development includes design mockup revision and staging feedback cycles before production deployment.",
  val cancellationRefundTerms: String = "Retainers may be paused or cancelled with 14 days prior notice before the next billing cycle. Advance deposits for initiated creative or development work are non-refundable."
)
