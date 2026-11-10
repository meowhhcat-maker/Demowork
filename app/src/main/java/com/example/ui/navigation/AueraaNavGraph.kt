package com.example.ui.navigation

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.model.PackageEntity
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminLoginScreen
import com.example.ui.screens.*
import com.example.viewmodel.AgencyViewModel

private const val TRANSITION_DURATION = 320
private const val TRANSITION_DURATION_EXIT = 260
private val TransitionEasing = FastOutSlowInEasing

@Composable
fun AueraaNavGraph(
  navController: NavHostController,
  viewModel: AgencyViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  // State observations
  val allPackages by viewModel.allPackages.collectAsStateWithLifecycle()
  val filteredPackages by viewModel.filteredPackages.collectAsStateWithLifecycle()
  val favoritePackages by viewModel.favoritePackages.collectAsStateWithLifecycle()

  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
  val selectedBudgetFilter by viewModel.selectedBudgetFilter.collectAsStateWithLifecycle()
  val selectedBusinessTypeFilter by viewModel.selectedBusinessTypeFilter.collectAsStateWithLifecycle()

  val comparisonIds by viewModel.comparisonPackageIds.collectAsStateWithLifecycle()
  val comparisonPackages by viewModel.comparisonPackages.collectAsStateWithLifecycle()

  val allEnquiries by viewModel.allEnquiries.collectAsStateWithLifecycle()
  val filteredEnquiries by viewModel.filteredEnquiries.collectAsStateWithLifecycle()
  val adminEnquirySearch by viewModel.adminEnquirySearch.collectAsStateWithLifecycle()
  val adminEnquiryStatusFilter by viewModel.adminEnquiryStatusFilter.collectAsStateWithLifecycle()
  val adminEnquiryServiceFilter by viewModel.adminEnquiryServiceFilter.collectAsStateWithLifecycle()

  val allCustomPackages by viewModel.allCustomPackages.collectAsStateWithLifecycle()
  val adminSettings by viewModel.adminSettings.collectAsStateWithLifecycle()
  val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()

  NavHost(
    navController = navController,
    startDestination = AueraaDestinations.Home.route,
    modifier = modifier,
    // Global default transitions for smooth fluid backstack motion
    enterTransition = {
      fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Start,
          animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
          initialOffset = { it / 5 }
        )
    },
    exitTransition = {
      fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Start,
          animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
          targetOffset = { it / 5 }
        )
    },
    popEnterTransition = {
      fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.End,
          animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
          initialOffset = { it / 5 }
        )
    },
    popExitTransition = {
      fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.End,
          animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
          targetOffset = { it / 5 }
        )
    }
  ) {
    // ---------------------------------------------
    // 1. HOME DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.Home.route,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing))
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            targetOffset = { it / 6 }
          )
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 6 }
          )
      }
    ) {
      HomeScreen(
        packages = allPackages,
        favorites = favoritePackages,
        comparisonIds = comparisonIds,
        onNavigateToCategory = { category ->
          viewModel.selectedCategoryFilter.value = category
          navController.navigate(AueraaDestinations.Services.createRoute(category))
        },
        onNavigateToPackages = {
          viewModel.selectedCategoryFilter.value = "All"
          navController.navigate(AueraaDestinations.Packages.route)
        },
        onNavigateToEnquiry = {
          navController.navigate(AueraaDestinations.Enquiry.createRoute())
        },
        onSelectPackage = { packageId ->
          navController.navigate(AueraaDestinations.PackageDetail.createRoute(packageId))
        },
        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
        onToggleComparison = { id -> viewModel.toggleComparison(id) },
        onSharePackage = { pkg -> viewModel.sharePackage(context, pkg) },
        onOpenWhatsApp = { pkgName -> viewModel.openWhatsApp(context, pkgName) },
        onNavigateToAbout = { navController.navigate(AueraaDestinations.About.route) },
        onNavigateToContact = { navController.navigate(AueraaDestinations.Contact.route) },
        onNavigateToTerms = { navController.navigate(AueraaDestinations.Terms.route) }
      )
    }

    // ---------------------------------------------
    // 2. SERVICES DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.Services.route,
      arguments = AueraaDestinations.Services.arguments,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      }
    ) { backStackEntry ->
      val categoryArg = backStackEntry.arguments?.getString("category") ?: "Content Creation"

      ServicesScreen(
        packages = allPackages,
        favorites = favoritePackages,
        comparisonIds = comparisonIds,
        initialCategory = categoryArg,
        onSelectPackage = { packageId ->
          navController.navigate(AueraaDestinations.PackageDetail.createRoute(packageId))
        },
        onSendEnquiryForPackage = { pkg ->
          navController.navigate(AueraaDestinations.Enquiry.createRoute(pkg.name, pkg.category))
        },
        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
        onToggleComparison = { id -> viewModel.toggleComparison(id) },
        onSharePackage = { pkg -> viewModel.sharePackage(context, pkg) },
        onOpenWhatsApp = { pkgName -> viewModel.openWhatsApp(context, pkgName) }
      )
    }

    // ---------------------------------------------
    // 3. PACKAGES DESTINATION (Search & Filter)
    // ---------------------------------------------
    composable(AueraaDestinations.Packages.route) {
      PackagesScreen(
        searchQuery = searchQuery,
        onSearchChange = { viewModel.searchQuery.value = it },
        selectedCategory = selectedCategoryFilter,
        onCategoryChange = { viewModel.selectedCategoryFilter.value = it },
        selectedBudget = selectedBudgetFilter,
        onBudgetChange = { viewModel.selectedBudgetFilter.value = it },
        selectedBusinessType = selectedBusinessTypeFilter,
        onBusinessTypeChange = { viewModel.selectedBusinessTypeFilter.value = it },
        packages = filteredPackages,
        favorites = favoritePackages,
        comparisonIds = comparisonIds,
        onSelectPackage = { packageId ->
          navController.navigate(AueraaDestinations.PackageDetail.createRoute(packageId))
        },
        onSendEnquiry = { pkg ->
          navController.navigate(AueraaDestinations.Enquiry.createRoute(pkg.name, pkg.category))
        },
        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
        onToggleComparison = { id -> viewModel.toggleComparison(id) },
        onSharePackage = { pkg -> viewModel.sharePackage(context, pkg) },
        onOpenWhatsApp = { pkgName -> viewModel.openWhatsApp(context, pkgName) }
      )
    }

    // ---------------------------------------------
    // 4. PACKAGE DETAIL DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.PackageDetail.route,
      arguments = AueraaDestinations.PackageDetail.arguments,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)
          )
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)
          )
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)
          )
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)
          )
      }
    ) { backStackEntry ->
      val packageId = backStackEntry.arguments?.getString("packageId") ?: ""
      val pkg = allPackages.find { it.id == packageId }
      val isFav = pkg?.let { p -> favoritePackages.any { it.id == p.id } } ?: false
      val inComp = pkg?.let { p -> comparisonIds.contains(p.id) } ?: false

      PackageDetailScreen(
        pkg = pkg,
        isFavorite = isFav,
        isInComparison = inComp,
        onBack = { navController.popBackStack() },
        onSendEnquiry = {
          if (pkg != null) {
            navController.navigate(AueraaDestinations.Enquiry.createRoute(pkg.name, pkg.category))
          }
        },
        onToggleFavorite = {
          if (pkg != null) viewModel.toggleFavorite(pkg.id, isFav)
        },
        onToggleComparison = {
          if (pkg != null) viewModel.toggleComparison(pkg.id)
        },
        onShare = {
          if (pkg != null) viewModel.sharePackage(context, pkg)
        },
        onWhatsApp = {
          if (pkg != null) viewModel.openWhatsApp(context, pkg.name)
        }
      )
    }

    // ---------------------------------------------
    // 5. ENQUIRY DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.Enquiry.route,
      arguments = AueraaDestinations.Enquiry.arguments,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      }
    ) { backStackEntry ->
      val prefilledPackageName = backStackEntry.arguments?.getString("package").takeIf { !it.isNullOrBlank() }
      val prefilledServiceName = backStackEntry.arguments?.getString("service").takeIf { !it.isNullOrBlank() } ?: "Content Creation"

      EnquiryScreen(
        allPackages = allPackages,
        prefilledService = prefilledServiceName,
        prefilledPackage = prefilledPackageName,
        onBack = { navController.popBackStack() },
        onSubmitEnquiry = { enquiry, onComplete ->
          viewModel.submitEnquiry(enquiry, onComplete)
        },
        onOpenWhatsApp = { customMsg ->
          viewModel.openWhatsApp(context, customMessage = customMsg)
        }
      )
    }

    // ---------------------------------------------
    // 6. COMPARE DESTINATION
    // ---------------------------------------------
    composable(AueraaDestinations.Compare.route) {
      CompareScreen(
        allPackages = allPackages,
        comparisonPackages = comparisonPackages,
        onRemoveFromComparison = { id -> viewModel.toggleComparison(id) },
        onAddPackageToComparison = { id -> viewModel.toggleComparison(id) },
        onClearComparison = { viewModel.clearComparison() },
        onSendEnquiry = { pkg ->
          navController.navigate(AueraaDestinations.Enquiry.createRoute(pkg.name, pkg.category))
        },
        onExplorePackages = { navController.navigate(AueraaDestinations.Packages.route) }
      )
    }

    // ---------------------------------------------
    // 7. FAVORITES DESTINATION
    // ---------------------------------------------
    composable(AueraaDestinations.Favorites.route) {
      FavoritesScreen(
        favoritePackages = favoritePackages,
        comparisonIds = comparisonIds,
        onSelectPackage = { packageId ->
          navController.navigate(AueraaDestinations.PackageDetail.createRoute(packageId))
        },
        onSendEnquiry = { pkg ->
          navController.navigate(AueraaDestinations.Enquiry.createRoute(pkg.name, pkg.category))
        },
        onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
        onToggleComparison = { id -> viewModel.toggleComparison(id) },
        onSharePackage = { pkg -> viewModel.sharePackage(context, pkg) },
        onOpenWhatsApp = { pkgName -> viewModel.openWhatsApp(context, pkgName) },
        onExplorePackages = { navController.navigate(AueraaDestinations.Packages.route) }
      )
    }

    // ---------------------------------------------
    // 8. ABOUT DESTINATION
    // ---------------------------------------------
    composable(AueraaDestinations.About.route) {
      AboutScreen(
        onNavigateToContact = { navController.navigate(AueraaDestinations.Contact.route) },
        onNavigateToPackages = { navController.navigate(AueraaDestinations.Packages.route) }
      )
    }

    // ---------------------------------------------
    // 9. CONTACT DESTINATION
    // ---------------------------------------------
    composable(AueraaDestinations.Contact.route) {
      ContactScreen(
        settings = adminSettings,
        onOpenWhatsApp = { pkgName -> viewModel.openWhatsApp(context, pkgName) },
        onNavigateToEnquiry = { navController.navigate(AueraaDestinations.Enquiry.createRoute()) }
      )
    }

    // ---------------------------------------------
    // 10. TERMS DESTINATION
    // ---------------------------------------------
    composable(AueraaDestinations.Terms.route) {
      TermsScreen(settings = adminSettings)
    }

    // ---------------------------------------------
    // 11. CUSTOM PACKAGE QUOTE DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.CustomPackage.route,
      arguments = AueraaDestinations.CustomPackage.arguments
    ) { backStackEntry ->
      val token = backStackEntry.arguments?.getString("token") ?: ""
      val customPkg = allCustomPackages.find { it.shareToken == token }

      CustomPackageViewScreen(
        customPackage = customPkg,
        onBack = { navController.popBackStack() },
        onAcceptPackage = { cp ->
          viewModel.openWhatsApp(
            context,
            customMessage = "I would like to accept the customized proposal for ${cp.packageName} (${cp.shareToken})"
          )
        },
        onSendEnquiry = { cp ->
          navController.navigate(AueraaDestinations.Enquiry.createRoute(cp.packageName, cp.category))
        },
        onOpenWhatsApp = { msg ->
          viewModel.openWhatsApp(context, customMessage = msg)
        }
      )
    }

    // ---------------------------------------------
    // 12. ADMIN LOGIN DESTINATION
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.AdminLogin.route,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(TRANSITION_DURATION + 40, easing = TransitionEasing),
            initialOffset = { it / 3 }
          )
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing))
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing))
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 3 }
          )
      }
    ) {
      AdminLoginScreen(
        onCancel = {
          navController.popBackStack()
        },
        onAuthenticate = { pin ->
          viewModel.authenticateAdmin(pin)
        },
        onAuthenticationSuccess = {
          navController.navigate(AueraaDestinations.Admin.route) {
            popUpTo(AueraaDestinations.AdminLogin.route) { inclusive = true }
          }
        }
      )
    }

    // ---------------------------------------------
    // 13. ADMIN DASHBOARD DESTINATION (AUTHENTICATION GUARDED)
    // ---------------------------------------------
    composable(
      route = AueraaDestinations.Admin.route,
      enterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION + 30, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(TRANSITION_DURATION + 30, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      exitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      },
      popEnterTransition = {
        fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing)) +
          slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(TRANSITION_DURATION, easing = TransitionEasing),
            initialOffset = { it / 4 }
          )
      },
      popExitTransition = {
        fadeOut(animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing)) +
          slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(TRANSITION_DURATION_EXIT, easing = TransitionEasing),
            targetOffset = { it / 4 }
          )
      }
    ) {
      AdminAuthGuard(
        isAuthenticated = isAdminAuthenticated,
        onNavigateToLogin = {
          navController.navigate(AueraaDestinations.AdminLogin.route) {
            popUpTo(AueraaDestinations.Admin.route) { inclusive = true }
          }
        }
      ) {
        AdminDashboardScreen(
          packages = allPackages,
          enquiries = filteredEnquiries,
          customPackages = allCustomPackages,
          settings = adminSettings,
          searchQuery = adminEnquirySearch,
          onSearchChange = { viewModel.adminEnquirySearch.value = it },
          selectedStatus = adminEnquiryStatusFilter,
          onStatusChange = { viewModel.adminEnquiryStatusFilter.value = it },
          selectedService = adminEnquiryServiceFilter,
          onServiceChange = { viewModel.adminEnquiryServiceFilter.value = it },
          onUpdateStatus = { enq, st -> viewModel.updateEnquiryStatus(enq, st) },
          onUpdatePayment = { enq, pay -> viewModel.updateEnquiryPayment(enq, pay) },
          onUpdateNotes = { enq, n, fu -> viewModel.updateEnquiryNotes(enq, n, fu) },
          onDeleteEnquiry = { enq -> viewModel.deleteEnquiry(enq) },
          onSavePackage = { pkg -> viewModel.saveOrUpdatePackage(pkg) },
          onDeletePackage = { pkg -> viewModel.deletePackage(pkg) },
          onDuplicatePackage = { pkg, onDone -> viewModel.duplicatePackage(pkg, onDone) },
          onCreateCustomPackage = { cp, onDone -> viewModel.createCustomPackage(cp, onDone) },
          onOpenCustomPackagePreview = { token ->
            navController.navigate(AueraaDestinations.CustomPackage.createRoute(token))
          },
          onDeleteCustomPackage = { cp -> viewModel.deleteCustomPackage(cp) },
          onSaveSettings = { s -> viewModel.updateAdminSettings(s) },
          onResetCatalog = { viewModel.resetCatalogToDefaults() },
          onLogout = {
            viewModel.logoutAdmin()
            navController.navigate(AueraaDestinations.Home.route) {
              popUpTo(0) { inclusive = true }
            }
          },
          onOpenWhatsApp = { msg -> viewModel.openWhatsApp(context, customMessage = msg) }
        )
      }
    }
  }
}
