package com.example.data.local

import androidx.room.*
import com.example.data.model.CustomPackageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Custom Client Quotes & Proposals.
 */
@Dao
interface CustomPackageDao {
  @Query("SELECT * FROM custom_packages ORDER BY createdAt DESC")
  fun getAllCustomPackages(): Flow<List<CustomPackageEntity>>

  @Query("SELECT * FROM custom_packages WHERE shareToken = :token LIMIT 1")
  fun getCustomPackageByToken(token: String): Flow<CustomPackageEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomPackage(pkg: CustomPackageEntity): Long

  @Delete
  suspend fun deleteCustomPackage(pkg: CustomPackageEntity)
}
