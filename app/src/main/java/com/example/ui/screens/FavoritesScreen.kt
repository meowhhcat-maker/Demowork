package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackageEntity
import com.example.ui.components.PackageCard
import com.example.ui.theme.BrandCyanPrimary

@Composable
fun FavoritesScreen(
  favoritePackages: List<PackageEntity>,
  comparisonIds: List<String>,
  onSelectPackage: (String) -> Unit,
  onSendEnquiry: (PackageEntity) -> Unit,
  onToggleFavorite: (String, Boolean) -> Unit,
  onToggleComparison: (String) -> Unit,
  onSharePackage: (PackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit,
  onExplorePackages: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("favorites_screen")
  ) {
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Saved Packages",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "${favoritePackages.size} packages bookmarked for quick review",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (favoritePackages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.BookmarkBorder,
            contentDescription = null,
            tint = BrandCyanPrimary,
            modifier = Modifier.size(56.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No saved packages yet",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Tap the bookmark icon on any package to save it to this list.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onExplorePackages,
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary)
          ) {
            Text("Browse Catalog", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(favoritePackages) { pkg ->
          val inComp = comparisonIds.contains(pkg.id)

          PackageCard(
            pkg = pkg,
            isFavorite = true,
            isInComparison = inComp,
            onViewDetails = { onSelectPackage(pkg.id) },
            onSendEnquiry = { onSendEnquiry(pkg) },
            onToggleFavorite = { onToggleFavorite(pkg.id, true) },
            onToggleComparison = { onToggleComparison(pkg.id) },
            onShare = { onSharePackage(pkg) },
            onWhatsApp = { onOpenWhatsApp(pkg.name) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(60.dp))
        }
      }
    }
  }
}
