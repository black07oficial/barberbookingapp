package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.ui.theme.LightOrange
import com.example.ui.theme.PrimaryOrange

@Composable
fun BarberImage(
    imageUrl: String?,
    imageType: String?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    // Generate a sleek barber-themed fallback gradient brush
    val fallbackGradient = Brush.verticalGradient(
        colors = when (imageType) {
            "hair_sleek_1" -> listOf(Color(0xFF4A3425), Color(0xFFF3842C))
            "hair_sleek_2" -> listOf(Color(0xFF2E241F), Color(0xFFFFB300))
            "hair_taper" -> listOf(Color(0xFF352B24), Color(0xFFE97D25))
            "nail_art" -> listOf(Color(0xFFFFADAD), Color(0xFFF3842C))
            "facial_mask" -> listOf(Color(0xFFB1D8C1), Color(0xFF429F6C))
            "hair_color" -> listOf(Color(0xFF6B2D5C), Color(0xFFE97D25))
            "makeup_glow" -> listOf(Color(0xFFFFE3E3), Color(0xFFF3842C))
            "spa_stone" -> listOf(Color(0xFF425A52), Color(0xFF2E2E3A))
            else -> listOf(LightOrange, PrimaryOrange)
        }
    )

    // Dynamically map image types to high-quality Unsplash URLs for outstanding, rich visuals (mockup accurate)
    val activeUrl = if (!imageUrl.isNullOrBlank() && imageUrl.startsWith("http")) {
        imageUrl
    } else {
        when (imageType) {
            "avatar_mike" -> "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&auto=format&fit=crop&q=80"
            "hair_sleek_1" -> "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=500&auto=format&fit=crop&q=80"
            "hair_sleek_2" -> "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=500&auto=format&fit=crop&q=80"
            "hair_taper" -> "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=500&auto=format&fit=crop&q=80"
            "nail_art" -> "https://images.unsplash.com/photo-1632345031435-8797b2d58045?w=500&auto=format&fit=crop&q=80"
            "facial_mask" -> "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=500&auto=format&fit=crop&q=80"
            "hair_color" -> "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=500&auto=format&fit=crop&q=80"
            "makeup_glow" -> "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=500&auto=format&fit=crop&q=80"
            "spa_stone" -> "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=500&auto=format&fit=crop&q=80"
            else -> null
        }
    }

    Box(modifier = modifier) {
        if (!activeUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = activeUrl,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(fallbackGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    // Fallback visual
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(fallbackGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                imageType?.contains("facial") == true -> Icons.Default.Spa
                                else -> Icons.Default.Face
                            },
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            )
        } else {
            // Placeholder visuals with beautiful layout graphics
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fallbackGradient),
                contentAlignment = Alignment.Center
            ) {
                // High contrast silhouette/icon
                Icon(
                    imageVector = when (imageType) {
                        "nail_art" -> Icons.Default.Spa
                        "facial_mask" -> Icons.Default.Spa
                        "spa_stone" -> Icons.Default.HourglassEmpty
                        else -> Icons.Default.Face
                    },
                    modifier = Modifier.fillMaxSize(0.4f),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}
