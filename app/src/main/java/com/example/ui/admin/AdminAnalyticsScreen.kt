package com.example.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnquiryEntity
import com.example.data.model.PackageEntity
import com.example.ui.theme.*

@Composable
fun AdminAnalyticsScreen(
  packages: List<PackageEntity>,
  enquiries: List<EnquiryEntity>
) {
  val totalPackages = packages.size
  val activePackages = packages.count { it.active }
  val totalEnquiries = enquiries.size
  val newEnquiries = enquiries.count { it.status == "New" }
  val followUps = enquiries.count { it.status == "Follow-up" }
  val converted = enquiries.count { it.status == "Converted" }
  val conversionRate = if (totalEnquiries > 0) (converted.toFloat() / totalEnquiries * 100).toInt() else 0

  val contentEnq = enquiries.count { it.service == "Content Creation" }
  val marketingEnq = enquiries.count { it.service == "Digital Marketing" }
  val webEnq = enquiries.count { it.service == "Website Development" }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp)
      .testTag("admin_analytics_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Executive Performance Analytics", fontWeight = FontWeight.Bold, fontSize = 18.sp)

    // Metric Summary Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      AnalyticsMetricCard("Total Enquiries", "$totalEnquiries", BrandCyanPrimary, Modifier.weight(1f))
      AnalyticsMetricCard("Converted Leads", "$converted", BrandEmeraldPrimary, Modifier.weight(1f))
      AnalyticsMetricCard("Conversion Rate", "$conversionRate%", BrandAmber, Modifier.weight(1f))
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      AnalyticsMetricCard("New Leads", "$newEnquiries", BrandCyanLight, Modifier.weight(1f))
      AnalyticsMetricCard("Active Follow-ups", "$followUps", BrandVioletPrimary, Modifier.weight(1f))
      AnalyticsMetricCard("Active Catalog", "$activePackages / $totalPackages", Color(0xFF38BDF8), Modifier.weight(1f))
    }

    // Chart 1: Enquiries by Service Category
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Enquiries by Service Pillar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(14.dp))

        val maxService = maxOf(contentEnq, marketingEnq, webEnq, 1)

        AnalyticsBarRow("Content Creation", contentEnq, maxService, BrandVioletPrimary)
        Spacer(modifier = Modifier.height(10.dp))
        AnalyticsBarRow("Digital Marketing", marketingEnq, maxService, BrandCyanPrimary)
        Spacer(modifier = Modifier.height(10.dp))
        AnalyticsBarRow("Website Development", webEnq, maxService, BrandEmeraldPrimary)
      }
    }

    // Chart 2: Pipeline Status Distribution
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Lead Pipeline Breakdown", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(14.dp))

        val statusList = listOf(
          "New" to enquiries.count { it.status == "New" },
          "Contacted" to enquiries.count { it.status == "Contacted" },
          "Follow-up" to enquiries.count { it.status == "Follow-up" },
          "Proposal Sent" to enquiries.count { it.status == "Proposal Sent" },
          "Converted" to enquiries.count { it.status == "Converted" },
          "Closed / Won" to enquiries.count { it.status == "Closed" }
        )

        val maxStatus = statusList.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

        statusList.forEach { (st, count) ->
          AnalyticsBarRow(st, count, maxStatus, BrandCyanPrimary)
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }

    // Chart 3: Top Requested Packages
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Top Requested Packages", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(12.dp))

        val topPackages = enquiries.groupingBy { it.packageName }.eachCount()
          .toList()
          .sortedByDescending { it.second }
          .take(5)

        if (topPackages.isEmpty()) {
          Text("No enquiry data yet", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
          val maxTop = topPackages.firstOrNull()?.second?.coerceAtLeast(1) ?: 1
          topPackages.forEach { (pkgName, count) ->
            AnalyticsBarRow(pkgName, count, maxTop, BrandAmber)
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(80.dp))
  }
}

@Composable
fun AnalyticsMetricCard(title: String, value: String, accentColor: Color, modifier: Modifier = Modifier) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(modifier = Modifier.height(4.dp))
      Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = accentColor)
    }
  }
}

@Composable
fun AnalyticsBarRow(label: String, count: Int, max: Int, color: Color) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
      Text("$count", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
    Spacer(modifier = Modifier.height(4.dp))
    val fraction = (count.toFloat() / max).coerceIn(0.04f, 1f)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction)
          .fillMaxHeight()
          .clip(RoundedCornerShape(4.dp))
          .background(color)
      )
    }
  }
}
