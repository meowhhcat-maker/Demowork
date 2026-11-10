package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AdminNotificationEntity
import com.example.ui.theme.BrandCyanPrimary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationDialog(
  notifications: List<AdminNotificationEntity>,
  onDismiss: () -> Unit,
  onNotificationClick: (AdminNotificationEntity) -> Unit,
  onClearAll: () -> Unit
) {
  val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 550.dp)
        .testTag("notifications_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = null,
              tint = BrandCyanPrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Agency Alerts (${notifications.size})",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (notifications.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No pending notifications.",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        } else {
          LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(notifications) { notif ->
              Surface(
                color = if (!notif.isRead) BrandCyanPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onNotificationClick(notif) }
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = notif.title,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = dateFormat.format(Date(notif.timestamp)),
                      fontSize = 10.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = notif.message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Phone: ${notif.phone}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandCyanPrimary
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          TextButton(
            onClick = onClearAll,
            modifier = Modifier.align(Alignment.End)
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Clear All")
          }
        }
      }
    }
  }
}
