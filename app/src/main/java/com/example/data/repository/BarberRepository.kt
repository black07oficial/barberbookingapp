package com.example.data.repository

import com.example.data.database.BarberDao
import com.example.data.database.BookingEntity
import com.example.data.database.FavoriteEntity
import com.example.data.database.UserEntity
import kotlinx.coroutines.flow.Flow

class BarberRepository(private val barberDao: BarberDao) {

    val userProfile: Flow<UserEntity?> = barberDao.getUserProfile()
    val allBookings: Flow<List<BookingEntity>> = barberDao.getAllBookings()
    val allFavorites: Flow<List<FavoriteEntity>> = barberDao.getAllFavorites()

    suspend fun saveUserProfile(user: UserEntity) {
        barberDao.saveUserProfile(user)
    }

    suspend fun insertBooking(booking: BookingEntity) {
        barberDao.insertBooking(booking)
    }

    suspend fun deleteBooking(bookingId: Int) {
        barberDao.deleteBooking(bookingId)
    }

    suspend fun setFavorite(serviceId: String, isFav: Boolean) {
        if (isFav) {
            barberDao.setFavorite(FavoriteEntity(serviceId, true))
        } else {
            barberDao.removeFavorite(serviceId)
        }
    }
}
