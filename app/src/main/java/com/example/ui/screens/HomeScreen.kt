package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PackageEntity
import com.example.ui.components.PackageCard
import com.example.ui.components.WhatsAppButton
import com.example.ui.theme.*

@Composable
fun HomeScreen(
  packages: List<PackageEntity>,
  favorites: List<PackageEntity>,
  comparisonIds: List<String>,
  onNavigateToCategory: (String) -> Unit,
  onNavigateToPackages: () -> Unit,
  onNavigateToEnquiry: () -> Unit,
  onSelectPackage: (String) -> Unit,
  onToggleFavorite: (String, Boolean) -> Unit,
  onToggleComparison: (String) -> Unit,
  onSharePackage: (PackageEntity) -> Unit,
  onOpenWhatsApp: (String?) -> Unit,
  onNavigateToAbout: () -> Unit,
  onNavigateToContact: () -> Unit,
  onNavigateToTerms: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  val contentCount = packages.count { it.category == "Content Creation" && it.active }
  val marketingCount = packages.count { it.category == "Digital Marketing" && it.active }
  val webCount = packages.count { it.category == "Website Development" && it.active }
  val featuredPackages = packages.filter { it.featured && it.active }.take(6)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .testTag("home_screen")
  ) {
    // ---------------------------------------------
    // HERO SECTION
    // ---------------------------------------------
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(340.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.bg_hero_banner),
        contentDescription = "AUERAA Agency Hero",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      // Gradient Overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                DarkBackground.copy(alpha = 0.5f),
                DarkBackground.copy(alpha = 0.85f),
                DarkBackground
              )
            )
          )
      )

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Bottom
      ) {
        Surface(
          color = BrandCyanPrimary.copy(alpha = 0.2f),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, BrandCyanPrimary.copy(alpha = 0.5f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(BrandEmeraldPrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "AUERAA.DIGITAL AGENCY",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = BrandCyanLight
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Grow Your Business With Better Digital Solutions",
          fontSize = 26.sp,
          fontWeight = FontWeight.Black,
          lineHeight = 32.sp,
          color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Creative Content • Digital Marketing • Professional Websites",
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFFCBD5E1)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onNavigateToPackages,
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = Modifier.weight(1f).testTag("hero_explore_packages")
          ) {
            Text(
              text = "Explore Packages",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color.Black
            )
          }

          OutlinedButton(
            onClick = onNavigateToEnquiry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = Modifier.weight(1f).testTag("hero_send_enquiry")
          ) {
            Text(
              text = "Send Enquiry",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    }

    // ---------------------------------------------
    // AGENCY STATS BAR
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        StatItem(number = "150+", label = "Projects Done")
        StatItem(number = "4.9★", label = "Client Rating")
        StatItem(number = "60+", label = "Live Packages")
        StatItem(number = "24/7", label = "Fast Support")
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // ---------------------------------------------
    // SERVICES SECTION (3 MAIN CATEGORIES)
    // ---------------------------------------------
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
      Text(
        text = "OUR CORE SERVICES",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = BrandCyanPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "End-to-End Digital Capabilities",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(16.dp))

      // Category 1: Content Creation
      ServiceCategoryCard(
        title = "Content Creation",
        tagline = "Social Media Graphics, Trending Reels & Video Assets",
        packageCount = "$contentCount Packages",
        icon = Icons.Default.VideoCameraBack,
        accentColor = BrandVioletPrimary,
        onClick = { onNavigateToCategory("Content Creation") }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Category 2: Digital Marketing
      ServiceCategoryCard(
        title = "Digital Marketing",
        tagline = "Meta Ads, Google Ads, SEO & Retention Funnels",
        packageCount = "$marketingCount Packages",
        icon = Icons.Default.TrendingUp,
        accentColor = BrandCyanPrimary,
        onClick = { onNavigateToCategory("Digital Marketing") }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Category 3: Website Development
      ServiceCategoryCard(
        title = "Website Development",
        tagline = "Custom Responsive Websites, Landing Pages & E-Commerce",
        packageCount = "$webCount Packages",
        icon = Icons.Default.Web,
        accentColor = BrandEmeraldPrimary,
        onClick = { onNavigateToCategory("Website Development") }
      )
    }

    Spacer(modifier = Modifier.height(30.dp))

    // ---------------------------------------------
    // FEATURED PACKAGES
    // ---------------------------------------------
    if (featuredPackages.isNotEmpty()) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "POPULAR SOLUTIONS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = BrandAmber
            )
            Text(
              text = "Featured Agency Packages",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
          }

          TextButton(onClick = onNavigateToPackages) {
            Text("See All", color = BrandCyanPrimary, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = BrandCyanPrimary, modifier = Modifier.size(16.dp))
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          items(featuredPackages) { pkg ->
            val isFav = favorites.any { it.id == pkg.id }
            val inComp = comparisonIds.contains(pkg.id)

            Box(modifier = Modifier.width(320.dp)) {
              PackageCard(
                pkg = pkg,
                isFavorite = isFav,
                isInComparison = inComp,
                onViewDetails = { onSelectPackage(pkg.id) },
                onSendEnquiry = { onNavigateToEnquiry() },
                onToggleFavorite = { onToggleFavorite(pkg.id, isFav) },
                onToggleComparison = { onToggleComparison(pkg.id) },
                onShare = { onSharePackage(pkg) },
                onWhatsApp = { onOpenWhatsApp(pkg.name) }
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))

    // ---------------------------------------------
    // WHY CHOOSE AUERAA.DIGITAL
    // ---------------------------------------------
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    ) {
      Text(
        text = "WHY AUERAA.DIGITAL",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = BrandCyanPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Engineered for Sustainable ROI",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(16.dp))

      val whyChooseItems = listOf(
        Pair("Professional Design", "Modern, visually striking graphics and UI built to convert attention into revenue."),
        Pair("Affordable Packages", "Transparent Indian Rupee pricing with zero hidden fees or unexpected extras."),
        Pair("Customized Solutions", "Every business receives bespoke strategies calibrated to its industry niche."),
        Pair("Business-Focused Strategy", "We prioritize qualified lead flow, pipeline growth, and measurable sales volume."),
        Pair("Responsive Support", "Direct WhatsApp & email communication with zero bureaucratic delays."),
        Pair("Scalable Services", "Seamlessly upgrade from one-off content packs to full 360° agency retainers.")
      )

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        whyChooseItems.forEach { (title, desc) ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(BrandCyanPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = BrandCyanPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = title,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = desc,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  lineHeight = 17.sp
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))

    // ---------------------------------------------
    // PROJECT IN MIND CTA BANNER
    // ---------------------------------------------
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surfaceVariant,
      border = BorderStroke(1.dp, BrandCyanPrimary.copy(alpha = 0.4f)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.Lightbulb,
          contentDescription = null,
          tint = BrandAmber,
          modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Have a project in mind?",
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Let's build a customized roadmap for your business. Our strategists reply within 2 business hours.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(18.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onNavigateToEnquiry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
            modifier = Modifier.weight(1f).testTag("cta_send_enquiry_btn")
          ) {
            Text("Send Your Enquiry", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          WhatsAppButton(
            text = "WhatsApp",
            onClick = { onOpenWhatsApp(null) },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // ---------------------------------------------
    // FOOTER LINKS
    // ---------------------------------------------
    Surface(
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "About",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { onNavigateToAbout() }
          )
          Text(text = "•", color = MaterialTheme.colorScheme.outline)
          Text(
            text = "Contact",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { onNavigateToContact() }
          )
          Text(text = "•", color = MaterialTheme.colorScheme.outline)
          Text(
            text = "Terms & Conditions",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { onNavigateToTerms() }
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "© 2026 AUERAA.DIGITAL. All rights reserved.",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(80.dp))
  }
}

@Composable
fun StatItem(number: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = number,
      fontWeight = FontWeight.Black,
      fontSize = 18.sp,
      color = BrandCyanPrimary
    )
    Text(
      text = label,
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun ServiceCategoryCard(
  title: String,
  tagline: String,
  packageCount: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(3.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("service_card_${title.replace(" ", "_")}")
  ) {
    Row(
      modifier = Modifier.padding(18.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Surface(
            color = accentColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = packageCount,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = accentColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = tagline,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "View Packages",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}
