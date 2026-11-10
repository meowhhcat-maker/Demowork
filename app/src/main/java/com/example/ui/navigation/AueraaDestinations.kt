package com.example.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AueraaDestinations(
  val route: String,
  val arguments: List<NamedNavArgument> = emptyList()
) {
  object Home : AueraaDestinations("home")

  object Services : AueraaDestinations(
    route = "services?category={category}",
    arguments = listOf(
      navArgument("category") {
        type = NavType.StringType
        defaultValue = "Content Creation"
      }
    )
  ) {
    fun createRoute(category: String = "Content Creation") = "services?category=$category"
  }

  object Packages : AueraaDestinations("packages")

  object PackageDetail : AueraaDestinations(
    route = "package_detail/{packageId}",
    arguments = listOf(
      navArgument("packageId") {
        type = NavType.StringType
      }
    )
  ) {
    fun createRoute(packageId: String) = "package_detail/$packageId"
  }

  object Enquiry : AueraaDestinations(
    route = "enquiry?package={package}&service={service}",
    arguments = listOf(
      navArgument("package") {
        type = NavType.StringType
        defaultValue = ""
      },
      navArgument("service") {
        type = NavType.StringType
        defaultValue = "Content Creation"
      }
    )
  ) {
    fun createRoute(packageName: String? = null, serviceName: String? = null): String {
      val p = packageName ?: ""
      val s = serviceName ?: "Content Creation"
      return "enquiry?package=$p&service=$s"
    }
  }

  object Compare : AueraaDestinations("compare")

  object Favorites : AueraaDestinations("favorites")

  object About : AueraaDestinations("about")

  object Contact : AueraaDestinations("contact")

  object Terms : AueraaDestinations("terms")

  object CustomPackage : AueraaDestinations(
    route = "custom_package/{token}",
    arguments = listOf(
      navArgument("token") {
        type = NavType.StringType
      }
    )
  ) {
    fun createRoute(token: String) = "custom_package/$token"
  }

  object AdminLogin : AueraaDestinations("admin_login")

  object Admin : AueraaDestinations("admin")
}
