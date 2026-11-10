package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminSettingsEntity
import com.example.ui.theme.BrandCyanPrimary

@Composable
fun TermsScreen(settings: AdminSettingsEntity) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("terms_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Gavel,
        contentDescription = null,
        tint = BrandCyanPrimary,
        modifier = Modifier.size(26.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = "Terms & Conditions",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "AUERAA.DIGITAL Agency Policies & Client Agreement",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Text(
      text = "By commissioning any digital services with AUERAA.DIGITAL, the client acknowledges and agrees to the standard terms outlined below. These ensure transparency, operational agility, and mutual protection.",
      fontSize = 13.sp,
      lineHeight = 18.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    // Terms Sections
    TermsSectionCard(
      title = "1. Content Creation Terms",
      content = settings.contentTerms
    )

    TermsSectionCard(
      title = "2. Digital Marketing & Ads Policy",
      content = settings.marketingTerms
    )

    TermsSectionCard(
      title = "3. Website Development Terms",
      content = settings.webTerms
    )

    TermsSectionCard(
      title = "4. Payment Terms & Billing",
      content = settings.paymentTerms
    )

    TermsSectionCard(
      title = "5. Revision & Change Request Policy",
      content = settings.revisionTerms
    )

    TermsSectionCard(
      title = "6. Third-Party Charges & Ad Spend",
      content = "All third-party charges, including Meta Ad Spend, Google Ads click budget, Shopify app subscriptions, domain registrations, and email SMS gateway API charges, are billed directly to the client's payment card and are independent of AUERAA.DIGITAL management retainers."
    )

    TermsSectionCard(
      title = "7. Cancellation & Refund Policy",
      content = settings.cancellationRefundTerms
    )

    TermsSectionCard(
      title = "8. Client Responsibilities & Intellectual Property",
      content = "Clients must supply brand assets, high-resolution logos, and feedback promptly. Upon final payment settlement, all finalized custom design deliverables and website code ownership transfer fully to the client, while AUERAA.DIGITAL retains portfolio showcase rights."
    )

    Spacer(modifier = Modifier.height(60.dp))
  }
}

@Composable
fun TermsSectionCard(title: String, content: String) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = BrandCyanPrimary
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = content,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
