package com.example.data.local

import androidx.room.*
import com.example.data.model.EnquiryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Client Enquiries and Leads.
 * Provides pipeline filtering and lead lifecycle management.
 */
@Dao
interface EnquiryDao {
  @Query("SELECT * FROM enquiries ORDER BY createdAt DESC")
  fun getAllEnquiries(): Flow<List<EnquiryEntity>>

  @Query("SELECT * FROM enquiries WHERE id = :id LIMIT 1")
  fun getEnquiryById(id: Long): Flow<EnquiryEntity?>

  @Query("SELECT * FROM enquiries WHERE enquiryId = :enquiryId LIMIT 1")
  fun getEnquiryByRefId(enquiryId: String): Flow<EnquiryEntity?>

  @Query("SELECT * FROM enquiries WHERE status = :status ORDER BY createdAt DESC")
  fun getEnquiriesByStatus(status: String): Flow<List<EnquiryEntity>>

  @Query("SELECT * FROM enquiries WHERE service = :service ORDER BY createdAt DESC")
  fun getEnquiriesByService(service: String): Flow<List<EnquiryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEnquiry(enquiry: EnquiryEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllEnquiries(list: List<EnquiryEntity>)

  @Update
  suspend fun updateEnquiry(enquiry: EnquiryEntity)

  @Query("UPDATE enquiries SET status = :newStatus, updatedAt = :updatedAt WHERE id = :id")
  suspend fun updateEnquiryStatus(id: Long, newStatus: String, updatedAt: Long = System.currentTimeMillis())

  @Query("UPDATE enquiries SET paymentStatus = :newPaymentStatus, updatedAt = :updatedAt WHERE id = :id")
  suspend fun updatePaymentStatus(id: Long, newPaymentStatus: String, updatedAt: Long = System.currentTimeMillis())

  @Delete
  suspend fun deleteEnquiry(enquiry: EnquiryEntity)

  @Query("DELETE FROM enquiries WHERE id = :id")
  suspend fun deleteEnquiryById(id: Long)

  @Query("SELECT COUNT(*) FROM enquiries")
  suspend fun getEnquiryCount(): Int
}
