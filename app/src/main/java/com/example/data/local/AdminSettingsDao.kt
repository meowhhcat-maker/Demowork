package com.example.data.local

import androidx.room.*
import com.example.data.model.AdminSettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Agency Settings and Configuration.
 */
@Dao
interface AdminSettingsDao {
  @Query("SELECT * FROM admin_settings WHERE id = 1 LIMIT 1")
  fun getSettings(): Flow<AdminSettingsEntity?>

  @Query("SELECT * FROM admin_settings WHERE id = 1 LIMIT 1")
  suspend fun getSettingsDirect(): AdminSettingsEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(settings: AdminSettingsEntity)
}
