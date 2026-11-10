package com.example.data.local

import androidx.room.*
import com.example.data.model.PackageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for AUERAA.DIGITAL Service Packages.
 * Provides reactive Flow streams and suspended CRUD operations.
 */
@Dao
interface PackageDao {
  @Query("SELECT * FROM packages ORDER BY displayOrder ASC, id ASC")
  fun getAllPackages(): Flow<List<PackageEntity>>

  @Query("SELECT * FROM packages WHERE id = :id LIMIT 1")
  fun getPackageById(id: String): Flow<PackageEntity?>

  @Query("SELECT * FROM packages WHERE id = :id LIMIT 1")
  suspend fun getPackageByIdDirect(id: String): PackageEntity?

  @Query("SELECT * FROM packages WHERE category = :category AND active = 1 ORDER BY displayOrder ASC")
  fun getPackagesByCategory(category: String): Flow<List<PackageEntity>>

  @Query("SELECT * FROM packages WHERE isFavorite = 1")
  fun getFavoritePackages(): Flow<List<PackageEntity>>

  @Query("SELECT * FROM packages WHERE active = 1 AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR services LIKE '%' || :query || '%') ORDER BY displayOrder ASC")
  fun searchPackages(query: String): Flow<List<PackageEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPackage(pkg: PackageEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllPackages(packages: List<PackageEntity>)

  @Update
  suspend fun updatePackage(pkg: PackageEntity)

  @Delete
  suspend fun deletePackage(pkg: PackageEntity)

  @Query("DELETE FROM packages WHERE id = :id")
  suspend fun deletePackageById(id: String)

  @Query("UPDATE packages SET isFavorite = :isFavorite WHERE id = :id")
  suspend fun updateFavorite(id: String, isFavorite: Boolean)

  @Query("SELECT COUNT(*) FROM packages")
  suspend fun getPackageCount(): Int
}
