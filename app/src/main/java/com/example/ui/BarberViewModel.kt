package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.BookingEntity
import com.example.data.database.UserEntity
import com.example.data.repository.BarberRepository
import com.example.ui.models.SalonService
import com.example.ui.models.ServiceCategory
import com.example.ui.models.Stylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BarberTab {
    HOME, SEARCH, BOOKINGS, FAVORITES, PROFILE
}

class BarberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarberRepository

    // Base flows from database
    val userProfileFlow: StateFlow<UserEntity?>
    val bookingsFlow: StateFlow<List<BookingEntity>>
    val favoritesFlow: StateFlow<Set<String>>

    private val prefs = application.getSharedPreferences("barber_onboarding_prefs", android.content.Context.MODE_PRIVATE)
    private val _onboardingCompleted = MutableStateFlow(prefs.getBoolean("completed", false))
    val onboardingCompleted = _onboardingCompleted.asStateFlow()

    // UI Interactive States
    private val _currentTab = MutableStateFlow(BarberTab.HOME)
    val currentTab = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Temporary storage for booking dialog selection
    private val _selectedServiceForBooking = MutableStateFlow<SalonService?>(null)
    val selectedServiceForBooking = _selectedServiceForBooking.asStateFlow()

    // Show booking confirmation dialog alert
    private val _bookingNotificationMessage = MutableStateFlow<String?>(null)
    val bookingNotificationMessage = _bookingNotificationMessage.asStateFlow()

    // Static Pre-populated Collections
    val categories = listOf(
        ServiceCategory("All", "All", "All"),
        ServiceCategory("Haircuts", "Haircuts", "Haircut"),
        ServiceCategory("Nail", "Nail", "Brush"),
        ServiceCategory("Facial", "Facial", "Spa"),
        ServiceCategory("Hair Colour", "Hair Colour", "ColorLens"),
        ServiceCategory("Makeup", "Makeup", "AutoAwesome"),
        ServiceCategory("Spa", "Spa", "Waves")
    )

    val stylists = listOf(
        Stylist("Alex Garcia", "Senior Master Barber", 4.9, "https://images.unsplash.com/photo-1517841905240-472988babdf9"),
        Stylist("Ryan Cooper", "Modern Hair Stylist", 4.8, "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6"),
        Stylist("Jessie Lopez", "Nails & Beauty Therapist", 4.7, "https://images.unsplash.com/photo-1494790108377-be9c29b29330")
    )

    val services = listOf(
        SalonService(
            id = "sleek_haircut_1",
            name = "Sleek haircut",
            description = "Precision premium classic trim designed for everyday elegance.",
            duration = "30 minute service",
            category = "Haircuts",
            rating = 5.6, // Literal matching mockup
            reviewCount = 160,
            price = 35.00,
            stylists = listOf("Alex Garcia", "Ryan Cooper"),
            imageType = "hair_sleek_1"
        ),
        SalonService(
            id = "sleek_haircut_2",
            name = "Sleek haircut",
            description = "Crisp razor fade with classic styling and scissor detail.",
            duration = "30 minute service",
            category = "Haircuts",
            rating = 3.5, // Literal matching mockup
            reviewCount = 512,
            price = 32.00,
            stylists = listOf("Ryan Cooper", "Alex Garcia"),
            imageType = "hair_sleek_2"
        ),
        SalonService(
            id = "high_taper_fade",
            name = "High taper fade",
            description = "Nice step 2 taper fade curated for your stylish steeze.",
            duration = "45 minute service",
            category = "Haircuts",
            rating = 4.9,
            reviewCount = 210,
            price = 40.00,
            stylists = listOf("Alex Garcia"),
            imageType = "hair_taper"
        ),
        SalonService(
            id = "nail_art_pro",
            name = "Elegant Nail Styling",
            description = "Deluxe manicuring detailing with natural base and hand nail art.",
            duration = "40 minute service",
            category = "Nail",
            rating = 4.8,
            reviewCount = 94,
            price = 28.00,
            stylists = listOf("Jessie Lopez"),
            imageType = "nail_art"
        ),
        SalonService(
            id = "facial_hydrating",
            name = "Hydrating Facial",
            description = "Gentle cleansing therapy, moisturizing steam treatment, & organic herbal massager.",
            duration = "50 minute service",
            category = "Facial",
            rating = 4.7,
            reviewCount = 142,
            price = 45.00,
            stylists = listOf("Jessie Lopez"),
            imageType = "facial_mask"
        ),
        SalonService(
            id = "hair_colour_vibe",
            name = "Creative Hair Colour",
            description = "Full customized ombre coloring or silver blonde highlights.",
            duration = "120 minute service",
            category = "Hair Colour",
            rating = 5.0,
            reviewCount = 75,
            price = 85.00,
            stylists = listOf("Ryan Cooper"),
            imageType = "hair_color"
        ),
        SalonService(
            id = "makeup_glowing",
            name = "Glowing Makeup",
            description = "Perfect contour, glow highlighter, and bold cosmetics for formal occasions.",
            duration = "60 minute service",
            category = "Makeup",
            rating = 4.6,
            reviewCount = 118,
            price = 55.00,
            stylists = listOf("Jessie Lopez"),
            imageType = "makeup_glow"
        ),
        SalonService(
            id = "spa_massage_stone",
            name = "Hot Stone Relaxation",
            description = "Thermal basalt stone body therapy session combined with therapeutic essential oils.",
            duration = "90 minute service",
            category = "Spa",
            rating = 4.9,
            reviewCount = 195,
            price = 95.00,
            stylists = listOf("Alex Garcia", "Jessie Lopez"),
            imageType = "spa_stone"
        )
    )

    // Dynamic reactive flow for filtering services
    val filteredServicesState: StateFlow<List<SalonService>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BarberRepository(database.barberDao())

        // Wire profile and bookings
        userProfileFlow = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        bookingsFlow = repository.allBookings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        favoritesFlow = repository.allFavorites.combine(MutableStateFlow(emptySet<String>())) { list, _ ->
            list.map { it.serviceId }.toSet()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

        // Filter algorithm combining selectedCategory and searchQuery
        filteredServicesState = combine(_selectedCategory, _searchQuery) { category, query ->
            services.filter { item ->
                val categoryMatches = category == "All" || item.category == category
                val searchMatches = query.isBlank() || 
                    item.name.contains(query, ignoreCase = true) || 
                    item.description.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true)
                categoryMatches && searchMatches
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = services
        )

        // Initialize user profile in database with layout values if empty
        viewModelScope.launch {
            val existing = repository.userProfile.firstOrNull()
            if (existing == null) {
                repository.saveUserProfile(
                    UserEntity(
                        id = 1,
                        name = "Mike Smith",
                        email = "smithmike@42exp.com",
                        imageUrl = "avatar_mike"
                    )
                )
            }
        }
    }

    fun selectTab(tab: BarberTab) {
        _currentTab.value = tab
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(serviceId: String) {
        viewModelScope.launch {
            val currentFavs = favoritesFlow.value
            val nextFavState = !currentFavs.contains(serviceId)
            repository.setFavorite(serviceId, nextFavState)
        }
    }

    fun showBookingSheet(service: SalonService?) {
        _selectedServiceForBooking.value = service
    }

    fun bookAppointment(
        serviceName: String,
        category: String,
        date: String,
        timeSlot: String,
        barberName: String,
        price: Double
    ) {
        viewModelScope.launch {
            repository.insertBooking(
                BookingEntity(
                    serviceName = serviceName,
                    category = category,
                    date = date,
                    timeSlot = timeSlot,
                    barberName = barberName,
                    price = price,
                    status = "Upcoming"
                )
            )
            _selectedServiceForBooking.value = null
            _bookingNotificationMessage.value = "Appointment scheduled with $barberName successfully!"
        }
    }

    fun cancelBooking(bookingId: Int) {
        viewModelScope.launch {
            repository.deleteBooking(bookingId)
            _bookingNotificationMessage.value = "Appointment cancelled."
        }
    }

    fun updateUserProfile(name: String, email: String) {
        viewModelScope.launch {
            repository.saveUserProfile(
                UserEntity(
                    id = 1,
                    name = name,
                    email = email,
                    imageUrl = "avatar_mike"
                )
            )
            _bookingNotificationMessage.value = "Profile updated successfully!"
        }
    }

    fun dismissNotification() {
        _bookingNotificationMessage.value = null
    }

    fun completeOnboarding() {
        prefs.edit().putBoolean("completed", true).apply()
        _onboardingCompleted.value = true
    }

    fun resetOnboarding() {
        prefs.edit().putBoolean("completed", false).apply()
        _onboardingCompleted.value = false
    }
}
