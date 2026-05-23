package com.example.ui.models

data class SalonService(
    val id: String,
    val name: String,
    val description: String,
    val duration: String,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val price: Double,
    val stylists: List<String>,
    // Local fallback drawable ID or generic gradient helper identifier
    val imageType: String 
)

data class Stylist(
    val name: String,
    val role: String,
    val rating: Double,
    val imageUrl: String
)

data class ServiceCategory(
    val id: String,
    val name: String,
    val iconType: String
)
