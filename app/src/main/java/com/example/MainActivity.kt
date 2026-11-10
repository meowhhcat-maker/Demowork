package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.*
import com.example.ui.navigation.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AgencyViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: AgencyViewModel = viewModel()
      val navController = rememberNavController()

      // Current destination from NavController backstack
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentDestination = navBackStackEntry?.destination?.route ?: AueraaDestinations.Home.route

      // States
      val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
      val notifications by viewModel.notifications.collectAsStateWithLifecycle()
      val unreadNotificationsCount = notifications.count { !it.isRead }
      val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()

      // Dialog states
      var showAdminLoginDialog by remember { mutableStateOf(false) }
      var showNotificationsDialog by remember { mutableStateOf(false) }

      MyApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            AueraaTopBar(
              currentRoute = currentDestination,
              unreadNotifications = unreadNotificationsCount,
              isAdmin = isAdminAuthenticated,
              isDarkTheme = isDarkTheme,
              onNavigate = { route ->
                when (route) {
                  "home" -> navController.navigate(AueraaDestinations.Home.route) {
                    popUpTo(AueraaDestinations.Home.route) { inclusive = false }
                    launchSingleTop = true
                  }
                  "packages" -> navController.navigate(AueraaDestinations.Packages.route)
                  "enquiry" -> navController.navigate(AueraaDestinations.Enquiry.createRoute())
                  "services" -> navController.navigate(AueraaDestinations.Services.createRoute())
                  else -> navController.navigate(route)
                }
              },
              onOpenNotifications = { showNotificationsDialog = true },
              onOpenAdmin = {
                navController.navigateToAdminGuarded(isAdminAuthenticated)
              },
              onToggleTheme = { viewModel.toggleTheme() }
            )
          },
          bottomBar = {
            AueraaBottomBar(
              currentRoute = currentDestination,
              onNavigate = { route ->
                when (route) {
                  "home" -> {
                    navController.navigate(AueraaDestinations.Home.route) {
                      popUpTo(AueraaDestinations.Home.route) { saveState = true }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                  "services" -> {
                    navController.navigate(AueraaDestinations.Services.createRoute()) {
                      popUpTo(AueraaDestinations.Home.route) { saveState = true }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                  "packages" -> {
                    navController.navigate(AueraaDestinations.Packages.route) {
                      popUpTo(AueraaDestinations.Home.route) { saveState = true }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                  "compare" -> {
                    navController.navigate(AueraaDestinations.Compare.route) {
                      popUpTo(AueraaDestinations.Home.route) { saveState = true }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                  "favorites" -> {
                    navController.navigate(AueraaDestinations.Favorites.route) {
                      popUpTo(AueraaDestinations.Home.route) { saveState = true }
                      launchSingleTop = true
                      restoreState = true
                    }
                  }
                  "admin" -> {
                    // Protected by authentication guard
                    navController.navigateToAdminGuarded(isAdminAuthenticated)
                  }
                }
              }
            )
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            // Main Jetpack Compose Navigation Graph
            AueraaNavGraph(
              navController = navController,
              viewModel = viewModel,
              modifier = Modifier.fillMaxSize()
            )
          }
        }

        // Quick TopBar Admin PIN Unlock Modal
        if (showAdminLoginDialog) {
          AdminLoginDialog(
            onDismiss = { showAdminLoginDialog = false },
            onAuthenticate = { pin ->
              val ok = viewModel.authenticateAdmin(pin)
              if (ok) {
                showAdminLoginDialog = false
                navController.navigate(AueraaDestinations.Admin.route)
              }
              ok
            }
          )
        }

        // Agency Notification Center Modal
        if (showNotificationsDialog) {
          NotificationDialog(
            notifications = notifications,
            onDismiss = { showNotificationsDialog = false },
            onNotificationClick = { notif ->
              viewModel.markNotificationRead(notif.id)
              showNotificationsDialog = false
              navController.navigateToAdminGuarded(isAdminAuthenticated)
            },
            onClearAll = { viewModel.clearAllNotifications() }
          )
        }
      }
    }
  }
}
