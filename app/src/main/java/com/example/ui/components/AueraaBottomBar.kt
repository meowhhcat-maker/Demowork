package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandCyanPrimary

data class NavItem(
  val route: String,
  val label: String,
  val iconFilled: androidx.compose.ui.graphics.vector.ImageVector,
  val iconOutlined: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AueraaBottomBar(
  currentRoute: String,
  onNavigate: (String) -> Unit
) {
  val items = listOf(
    NavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("services", "Services", Icons.Filled.GridView, Icons.Outlined.GridView),
    NavItem("packages", "Packages", Icons.Filled.Search, Icons.Outlined.Search),
    NavItem("compare", "Compare", Icons.Filled.CompareArrows, Icons.Outlined.CompareArrows),
    NavItem("favorites", "Saved", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    NavItem("admin", "Admin", Icons.Filled.AdminPanelSettings, Icons.Outlined.AdminPanelSettings)
  )

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp,
    modifier = Modifier
      .windowInsetsPadding(WindowInsets.navigationBars)
      .testTag("aueraa_bottom_bar")
  ) {
    items.forEach { item ->
      val selected = currentRoute == item.route ||
        (item.route == "services" && currentRoute.startsWith("services")) ||
        (item.route == "packages" && currentRoute.startsWith("package_detail")) ||
        (item.route == "admin" && (currentRoute.startsWith("admin") || currentRoute == "admin_login"))

      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(item.route) },
        icon = {
          Icon(
            imageVector = if (selected) item.iconFilled else item.iconOutlined,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = item.label,
            fontSize = 11.sp
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = BrandCyanPrimary,
          selectedTextColor = BrandCyanPrimary,
          indicatorColor = BrandCyanPrimary.copy(alpha = 0.15f),
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag("nav_item_${item.route}")
      )
    }
  }
}
