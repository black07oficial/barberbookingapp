package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BarberTab
import com.example.ui.BarberViewModel
import com.example.ui.components.BarberImage
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.LightOrange
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.RatingGold
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

@Composable
fun FavoritesScreen(
    viewModel: BarberViewModel,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favoritesFlow.collectAsState()
    val favoritedServices = viewModel.services.filter { favorites.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simple Top Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoftWhite)
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Meus Favoritos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (favoritedServices.isEmpty()) {
            // Elegant Empty state
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(LightOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = PrimaryOrange,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Nenhum favorito selecionado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Curte seus cortes de cabelo, serviços de barba e tratamentos favoritos e eles vão aparecer centralizados aqui!",
                    fontSize = 13.sp,
                    color = TextSoftGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.selectTab(BarberTab.SEARCH) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Buscar Cortes", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // High fidelity scrollable cards of favorites
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favoritedServices) { service ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("favorite_service_card_${service.id}")
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
                                    .size(64.dp)
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
                                    fontSize = 9.sp,
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
                                    text = "R$ %.2f - %s".format(service.price, service.duration),
                                    fontSize = 12.sp,
                                    color = TextSoftGrey
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = RatingGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${service.rating} (${service.reviewCount} avaliações)",
                                        fontSize = 11.sp,
                                        color = TextSoftGrey
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Heart remove toggle and book combo buttons
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(
                                    onClick = { viewModel.toggleFavorite(service.id) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(DividerGrey.copy(0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Desfavoritar",
                                        tint = Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                IconButton(
                                    onClick = { viewModel.showBookingSheet(service) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(PrimaryOrange, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Agendar",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
