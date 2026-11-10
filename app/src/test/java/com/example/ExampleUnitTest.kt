package com.example

import com.example.data.model.InitialData
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testInitialPackagesCoverage() {
    val packages = InitialData.getDefaultPackages()
    assertTrue("Should have 60 total packages", packages.size >= 60)

    val contentCount = packages.count { it.category == "Content Creation" }
    val marketingCount = packages.count { it.category == "Digital Marketing" }
    val webCount = packages.count { it.category == "Website Development" }

    assertEquals("Should have 25 Content Creation packages", 25, contentCount)
    assertEquals("Should have 25 Digital Marketing packages", 25, marketingCount)
    assertEquals("Should have 10 Website Development packages", 10, webCount)

    // Check pricing formatting
    packages.forEach { pkg ->
      assertTrue("Package price must be positive", pkg.price > 0)
      assertTrue("Package formatted price should have rupee symbol", pkg.priceFormatted.contains("₹"))
      assertTrue("Package must have at least one service listed", pkg.serviceList.isNotEmpty())
    }
  }

  @Test
  fun testDefaultEnquiriesExist() {
    val enquiries = InitialData.getDefaultEnquiries()
    assertTrue(enquiries.isNotEmpty())
    enquiries.forEach { enq ->
      assertTrue(enq.enquiryId.startsWith("AUERAA-ENQ"))
      assertTrue(enq.clientName.isNotBlank())
      assertTrue(enq.phone.isNotBlank())
      assertTrue("Status should be valid", enq.status in listOf("New", "Contacted", "Follow-up", "Proposal Sent", "Converted", "Closed", "Rejected"))
    }
  }

  @Test
  fun testPackageEntityMethods() {
    val testPkg = InitialData.getDefaultPackages().first()
    assertNotNull(testPkg.serviceList)
    assertTrue(testPkg.serviceList.isNotEmpty())
  }
}
