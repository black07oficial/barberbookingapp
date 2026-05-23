package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.BarberViewModel
import com.example.ui.components.BarberImage
import com.example.ui.models.SalonService
import com.example.ui.theme.DarkBlack
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.LightOrange
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.RatingGold
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

@Composable
fun SearchScreen(
    viewModel: BarberViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredServices by viewModel.filteredServicesState.collectAsState()
    val favorites by viewModel.favoritesFlow.collectAsState()

    // Filter to represent Sleek haircut card views on Phone 3 (Offers section)
    val dealList = viewModel.services.filter { it.category == "Haircuts" && it.id.contains("sleek") }

    // Represent result helper collections (circular scroll entries "Hair Colour", "Makeup", "Spa")
    val resultCategoryCircles = listOf(
        Pair("Hair Colour", "hair_color"),
        Pair("Makeup", "makeup_glow"),
        Pair("Spa", "spa_stone")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("search_screen_content"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 90.dp)
    ) {
        // 1. Search input with Filter Tuning and Calendaring row Flanking
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Main Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearch(it) },
                    placeholder = { Text("Search...", color = TextSoftGrey, fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_page_input"),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSoftGrey
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

                Spacer(modifier = Modifier.width(10.dp))

                // Filter Tuning Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftWhite)
                        .border(1.dp, DividerGrey, RoundedCornerShape(14.dp))
                        .clickable { viewModel.selectCategory("All") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = TextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Calendar Selection Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftWhite)
                        .border(1.dp, DividerGrey, RoundedCornerShape(14.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = TextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 2. Horizontal Category Select Pills (All, Haircuts, Facial, Hairdo...)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                viewModel.categories.forEach { category ->
                    val isSelected = category.id == selectedCategory
                    val pillBg = if (isSelected) PrimaryOrange else SoftWhite
                    val pillText = if (isSelected) SoftWhite else TextSoftGrey
                    val borderLine = if (isSelected) PrimaryOrange else DividerGrey

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(pillBg)
                            .border(1.dp, borderLine, RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectCategory(category.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = pillText
                        )
                    }
                }
            }
        }

        // 3. Campaign section: "Eid offers", "See all"
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Eid offers",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "See all",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryOrange,
                        modifier = Modifier.clickable { viewModel.selectCategory("All") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontally scrolling cards representing "Sleek haircut" list
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    dealList.forEach { service ->
                        DealDisplayCard(
                            service = service,
                            onClick = { viewModel.showBookingSheet(service) }
                        )
                    }
                }
            }
        }

        // 4. "Results (2512)", "See all" and circular quick filters
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Results (${filteredServices.size})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "See all",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryOrange,
                        modifier = Modifier.clickable { viewModel.updateSearch("") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Beautiful Circular/Curved scrollable Row flanking: Hair Colour (Orange background selection), Makeup, Spa
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    resultCategoryCircles.forEach { rowCategory ->
                        val isOrangeBg = rowCategory.second == "hair_color"
                        val cardBgColor = if (isOrangeBg) PrimaryOrange else SoftWhite
                        val titleCol = if (isOrangeBg) SoftWhite else TextDark

                        Column(
                            modifier = Modifier
                                .width(96.dp)
                                .height(138.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(cardBgColor)
                                .clickable {
                                    val mappedCat = when (rowCategory.second) {
                                        "hair_color" -> "Hair Colour"
                                        "makeup_glow" -> "Makeup"
                                        "spa_stone" -> "Spa"
                                        else -> "All"
                                    }
                                    viewModel.selectCategory(mappedCat)
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(if (isOrangeBg) Color.White.copy(alpha = 0.25f) else DividerGrey.copy(alpha = 0.40f))
                            ) {
                                BarberImage(
                                    imageUrl = null,
                                    imageType = rowCategory.second,
                                    contentDescription = rowCategory.first,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = rowCategory.first,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = titleCol,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 5. Grid/Vertical list of actual filtered matches from database
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cuts & Treatments",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }

        if (filteredServices.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No matches found.",
                        fontSize = 14.sp,
                        color = TextSoftGrey,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            items(filteredServices) { service ->
                val isFav = favorites.contains(service.id)
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("service_result_card_${service.id}")
                            .clickable { viewModel.showBookingSheet(service) },
                        colors = CardDefaults.cardColors(containerColor = SoftWhite),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                BarberImage(
                                    imageUrl = null,
                                    imageType = service.imageType,
                                    contentDescription = service.name,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = service.category.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryOrange
                                )
                                Text(
                                    text = service.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = service.duration,
                                    fontSize = 12.sp,
                                    color = TextSoftGrey
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = RatingGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${service.rating} | $%.2f".format(service.price),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Heart Fav icon right inside search list
                            IconButton(
                                onClick = { viewModel.toggleFavorite(service.id) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DividerGrey.copy(0.3f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (isFav) Color.Red else TextSoftGrey,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DealDisplayCard(
    service: SalonService,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick)
            .testTag("deal_display_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = SoftWhite),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                BarberImage(
                    imageUrl = null,
                    imageType = service.imageType,
                    contentDescription = service.name,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = service.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = service.duration,
                    fontSize = 12.sp,
                    color = TextSoftGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RatingGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${service.rating} | ${service.reviewCount} Views",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSoftGrey
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(DarkBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Book",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
