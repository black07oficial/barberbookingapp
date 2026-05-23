package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.BarberTab
import com.example.ui.BarberViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.BookingDialog
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.BookingsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainAppShell()
      }
    }
  }
}

@Composable
fun MainAppShell(viewModel: BarberViewModel = viewModel()) {
  val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val selectedServiceForBooking by viewModel.selectedServiceForBooking.collectAsState()
  val notificationMessage by viewModel.bookingNotificationMessage.collectAsState()

  // Automatic dismiss logic for the booking confirmation banner
  LaunchedEffect(notificationMessage) {
    if (notificationMessage != null) {
      delay(3000)
      viewModel.dismissNotification()
    }
  }

  if (!onboardingCompleted) {
    OnboardingScreen(
        onFinished = { viewModel.completeOnboarding() }
    )
  } else {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_main_container"),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
          BottomNavBar(
              selectedTab = currentTab,
              onTabSelected = { tab -> viewModel.selectTab(tab) }
          )
        }
    ) { innerPadding ->
      Box(
          modifier = Modifier
              .fillMaxSize()
              .padding(bottom = innerPadding.calculateBottomPadding())
              .statusBarsPadding()
      ) {
        // Swappable Main Area Views based on Active Tab
        when (currentTab) {
          BarberTab.HOME -> {
            HomeScreen(viewModel = viewModel)
          }
          BarberTab.SEARCH -> {
            SearchScreen(viewModel = viewModel)
          }
          BarberTab.BOOKINGS -> {
            BookingsScreen(viewModel = viewModel)
          }
          BarberTab.FAVORITES -> {
            FavoritesScreen(viewModel = viewModel)
          }
          BarberTab.PROFILE -> {
            ProfileScreen(viewModel = viewModel)
          }
        }

        // 1. Success Notification Banner Overlay (sliding from top edge)
        AnimatedVisibility(
            visible = notificationMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
          notificationMessage?.let { msg ->
            Surface(
                color = SoftWhite,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerGrey, RoundedCornerShape(16.dp))
                    .testTag("top_alert_notification")
            ) {
              Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Sucesso",
                      tint = Color(0xFF43A047),
                      modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                      text = msg,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold,
                      color = TextDark,
                      modifier = Modifier.fillMaxWidth(0.85f)
                  )
                }
                IconButton(
                    onClick = { viewModel.dismissNotification() },
                    modifier = Modifier.size(24.dp)
                ) {
                  Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Fechar alerta",
                      tint = TextDark,
                      modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }

        // 2. Custom interactive Dialog box for booking date / time choice
        selectedServiceForBooking?.let { service ->
          BookingDialog(
              service = service,
              viewModel = viewModel,
              onDismiss = { viewModel.showBookingSheet(null) }
          )
        }
      }
    }
  }
}

