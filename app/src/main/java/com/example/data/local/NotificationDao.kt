package com.example.data.local

import androidx.room.*
import com.example.data.model.AdminNotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Agency Alerts and Lead Notifications.
 */
@Dao
interface NotificationDao {
  @Query("SELECT * FROM admin_notifications ORDER BY timestamp DESC")
  fun getAllNotifications(): Flow<List<AdminNotificationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: AdminNotificationEntity): Long

  @Query("UPDATE admin_notifications SET isRead = 1 WHERE id = :id")
  suspend fun markAsRead(id: Long)

  @Query("DELETE FROM admin_notifications")
  suspend fun clearAllNotifications()
}
