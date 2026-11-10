package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
  entities = [
    PackageEntity::class,
    EnquiryEntity::class,
    CustomPackageEntity::class,
    AdminSettingsEntity::class,
    AdminNotificationEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun packageDao(): PackageDao
  abstract fun enquiryDao(): EnquiryDao
  abstract fun customPackageDao(): CustomPackageDao
  abstract fun adminSettingsDao(): AdminSettingsDao
  abstract fun notificationDao(): NotificationDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "aueraa_digital_db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
