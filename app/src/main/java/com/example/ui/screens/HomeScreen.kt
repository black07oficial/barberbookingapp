package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BarberTab
import com.example.ui.BarberViewModel
import com.example.ui.components.BarberImage
import com.example.ui.theme.DarkBlack
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.LightOrange
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

@Composable
fun HomeScreen(
    viewModel: BarberViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfileFlow.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Filtered lists represent standard UI
    val offerHighlightServices = viewModel.services.take(2)
    val curatedService = viewModel.services.firstOrNull { it.id == "high_taper_fade" } ?: viewModel.services.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 90.dp)
    ) {
        // 1. Personal Greeting Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DividerGrey)
                            .clickable { viewModel.selectTab(BarberTab.PROFILE) }
                    ) {
                        BarberImage(
                            imageUrl = null,
                            imageType = "avatar_mike",
                            contentDescription = "Avatar do usuário",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Hello ${profile?.name?.split(" ")?.firstOrNull()?.lowercase() ?: "smith"}",
                            fontSize = 13.sp,
                            color = TextSoftGrey,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Good morning!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }

                // Notification Bell icon with active tiny badging
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SoftWhite)
                        .clickable { }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificação",
                        tint = TextDark
                    )
                    // Notification badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 2.dp, end = 2.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }
            }
        }

        // 2. Search Field Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.updateSearch(it)
                        // Auto route to Search Screen if user types in search box!
                        if (it.isNotBlank()) {
                            viewModel.selectTab(BarberTab.SEARCH)
                        }
                    },
                    placeholder = { Text("Search...", color = TextSoftGrey, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = TextSoftGrey
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = TextSoftGrey,
                            modifier = Modifier.clickable { viewModel.selectTab(BarberTab.SEARCH) }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SoftWhite,
                        unfocusedContainerColor = SoftWhite,
                        focusedBorderColor = DividerGrey,
                        unfocusedBorderColor = DividerGrey,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    singleLine = true
                )
            }
        }

        // 3. Category Horizontal Scroll Custom Row (matching Phone 2)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Category: Haircuts (Selected Custom Style Accent)
                    CategoryItemCard(
                        name = "Haircuts",
                        imageType = "hair_sleek_1",
                        isSelected = selectedCategory == "Haircuts",
                        onClick = {
                            viewModel.selectCategory("Haircuts")
                            viewModel.selectTab(BarberTab.SEARCH)
                        }
                    )

                    // Category: Nail
                    CategoryItemCard(
                        name = "Nail",
                        imageType = "nail_art",
                        isSelected = selectedCategory == "Nail",
                        onClick = {
                            viewModel.selectCategory("Nail")
                            viewModel.selectTab(BarberTab.SEARCH)
                        }
                    )

                    // Category: Facial
                    CategoryItemCard(
                        name = "Facial",
                        imageType = "facial_mask",
                        isSelected = selectedCategory == "Facial",
                        onClick = {
                            viewModel.selectCategory("Facial")
                            viewModel.selectTab(BarberTab.SEARCH)
                        }
                    )
                }
            }
        }

        // 4. "Eid offers" Campaign Section Banner
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Eid offers",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "See all",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryOrange,
                        modifier = Modifier.clickable {
                            viewModel.selectCategory("All")
                            viewModel.selectTab(BarberTab.SEARCH)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Orange Promo Banner Card (Exactly matching Phone 2 banner)
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("promo_banner_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1.1f)) {
                            // Category Tag pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SoftWhite.copy(alpha = 0.25f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Haircut",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "30% Free",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                            Text(
                                text = "Aug 12-Aug 27",
                                fontSize = 12.sp,
                                color = SoftWhite.copy(alpha = 0.85f)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            // Get Offer Button with trailing chevron
                            Button(
                                onClick = { viewModel.showBookingSheet(viewModel.services.first()) },
                                colors = ButtonDefaults.buttonColors(containerColor = SoftWhite),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Get Offer Now",
                                        color = TextDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(DarkBlack, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Right side banner image of hair graphic
                        Box(
                            modifier = Modifier
                                .weight(0.9f)
                                .height(115.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SoftWhite.copy(alpha = 0.15f))
                        ) {
                            BarberImage(
                                imageUrl = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1",
                                imageType = "hair_sleek_1",
                                contentDescription = "Destaques",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        // 5. Featured curated list (High taper fade card from mockup)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("curated_special_card")
                        .clickable { viewModel.showBookingSheet(curatedService) },
                    colors = CardDefaults.cardColors(containerColor = SoftWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = curatedService.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = curatedService.description,
                                fontSize = 12.sp,
                                color = TextSoftGrey,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Next arrow circular trigger
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DarkBlack)
                                .clickable { viewModel.showBookingSheet(curatedService) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Agendar",
                                tint = SoftWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItemCard(
    name: String,
    imageType: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerBg = if (isSelected) PrimaryOrange else SoftWhite
    val titleCol = if (isSelected) SoftWhite else TextDark

    Column(
        modifier = Modifier
            .width(96.dp)
            .height(138.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(containerBg)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.White.copy(alpha = 0.25f) else DividerGrey.copy(alpha = 0.40f))
        ) {
            BarberImage(
                imageUrl = null,
                imageType = imageType,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = titleCol,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
