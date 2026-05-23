package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BarberTab
import com.example.ui.theme.DarkBlack
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextSoftGrey

@Composable
fun BottomNavBar(
    selectedTab: BarberTab,
    onTabSelected: (BarberTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Main Navigation bar housing
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(24.dp))
                .testTag("app_bottom_nav_bar"),
            color = SoftWhite,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Tab
                NavBarItem(
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    label = "Início",
                    isSelected = selectedTab == BarberTab.HOME,
                    onClick = { onTabSelected(BarberTab.HOME) },
                    modifier = Modifier.testTag("nav_home_tab")
                )

                // Search Tab
                NavBarItem(
                    selectedIcon = Icons.Filled.Search,
                    unselectedIcon = Icons.Outlined.Search,
                    label = "Buscar",
                    isSelected = selectedTab == BarberTab.SEARCH,
                    onClick = { onTabSelected(BarberTab.SEARCH) },
                    modifier = Modifier.testTag("nav_search_tab")
                )

                // Special Center Spot spacing helper (represented by the elevated FAB below)
                Box(modifier = Modifier.size(54.dp))

                // Bookings Tab
                NavBarItem(
                    selectedIcon = Icons.Filled.CalendarMonth,
                    unselectedIcon = Icons.Outlined.CalendarMonth,
                    label = "Agendas",
                    isSelected = selectedTab == BarberTab.BOOKINGS,
                    onClick = { onTabSelected(BarberTab.BOOKINGS) },
                    modifier = Modifier.testTag("nav_bookings_tab")
                )

                // Profile Tab
                NavBarItem(
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    label = "Perfil",
                    isSelected = selectedTab == BarberTab.PROFILE,
                    onClick = { onTabSelected(BarberTab.PROFILE) },
                    modifier = Modifier.testTag("nav_profile_tab")
                )
            }
        }

        // Floating central circular action button holding the favorites/heart trigger
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-18).dp)
        ) {
            val isFavSelected = selectedTab == BarberTab.FAVORITES
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(DarkBlack)
                    .clickable { onTabSelected(BarberTab.FAVORITES) }
                    .testTag("nav_favorites_tab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavSelected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favoritos",
                    tint = if (isFavSelected) PrimaryOrange else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = if (isSelected) PrimaryOrange else TextSoftGrey,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) PrimaryOrange else TextSoftGrey
        )
    }
}
