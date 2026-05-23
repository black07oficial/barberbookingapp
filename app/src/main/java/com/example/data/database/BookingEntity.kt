package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val category: String,
    val date: String,
    val timeSlot: String,
    val barberName: String,
    val price: Double,
    val status: String = "Upcoming", // "Upcoming" or "Completed"
    val timestamp: Long = System.currentTimeMillis()
)
