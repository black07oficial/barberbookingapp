package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkBlack
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.LightOrange
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val gradientColors: List<Color>
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPageIndex by remember { mutableStateOf(0) }

    val pages = listOf(
        OnboardingPage(
            title = "Estilo & Precisão",
            subtitle = "Eleve o seu visual com barbeiros especializados, técnicas modernas e um atendimento sob medida para sua personalidade.",
            imageUrl = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800&auto=format&fit=crop&q=80",
            gradientColors = listOf(Color(0xFF4A3425), Color(0xFFF3842C))
        ),
        OnboardingPage(
            title = "Agendamento Rápido",
            subtitle = "Escolha cortes premium, barba, rituais faciais ou manicure. Reserve seu horário em poucos cliques sem qualquer burocracia.",
            imageUrl = "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=800&auto=format&fit=crop&q=80",
            gradientColors = listOf(Color(0xFF2E241F), Color(0xFFFFB300))
        ),
        OnboardingPage(
            title = "Experiência Premium",
            subtitle = "Sinta-se renovado em um espaço aconchegante e exclusivo criado inteiramente para o seu bem-estar diário.",
            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800&auto=format&fit=crop&q=80",
            gradientColors = listOf(Color(0xFF2E2E3A), Color(0xFF4A5568))
        )
    )

    val currentPage = pages[currentPageIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .statusBarsPadding()
            .testTag("onboarding_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP BAR: Skip Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentPageIndex < pages.lastIndex) {
                Text(
                    text = "Pular",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSoftGrey,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onFinished() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("onboarding_skip")
                )
            } else {
                Spacer(modifier = Modifier.height(26.dp))
            }
        }

        // SWAPPABLE HERO ILLUSTRATION & TEXT INFO (animated slider concept)
        AnimatedContent(
            targetState = currentPageIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // High-End Curve Card for Unsplash Visual Overlay
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.55f)
                        .testTag("onboarding_image_card_$pageIndex"),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = page.imageUrl,
                            contentDescription = page.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Dynamic Gradient Darkener Bottom Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                                        startY = 180f
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Typography pairings
                Text(
                    text = page.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.testTag("onboarding_title_$pageIndex")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = page.subtitle,
                    fontSize = 15.sp,
                    color = TextSoftGrey,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .testTag("onboarding_subtitle_$pageIndex")
                )
            }
        }

        // BOTTOM ACTION ROW: Dot indicators & Navigation Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elegant dots slider indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.forEachIndexed { index, _ ->
                    val isCurrent = index == currentPageIndex
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isCurrent) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) PrimaryOrange else DividerGrey)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Premium Rounded Action Button
            Button(
                onClick = {
                    if (currentPageIndex < pages.lastIndex) {
                        currentPageIndex++
                    } else {
                        onFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("onboarding_next_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlack),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (currentPageIndex == pages.lastIndex) "Começar Agora" else "Avançar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = PrimaryOrange,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
