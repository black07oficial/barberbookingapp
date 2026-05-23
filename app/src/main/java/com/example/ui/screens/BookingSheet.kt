package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.BarberViewModel
import com.example.ui.components.BarberImage
import com.example.ui.models.SalonService
import com.example.ui.models.Stylist
import com.example.ui.theme.DarkBlack
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.RatingGold
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

@Composable
fun BookingDialog(
    service: SalonService,
    viewModel: BarberViewModel,
    onDismiss: () -> Unit
) {
    // Local state for scheduler choices
    val availableDates = listOf(
        Pair("Seg", "25 Mai"),
        Pair("Ter", "26 Mai"),
        Pair("Qua", "27 Mai"),
        Pair("Qui", "28 Mai"),
        Pair("Sex", "29 Mai"),
        Pair("Sáb", "30 Mai")
    )
    var selectedDateIdx by remember { mutableStateOf(0) }

    val matchingStylists = viewModel.stylists.filter { stylist ->
        service.stylists.contains(stylist.name)
    }.ifEmpty { viewModel.stylists }

    var selectedStylist by remember { mutableStateOf(matchingStylists.first()) }

    val timeSlots = listOf("09:00", "10:30", "13:00", "14:30", "16:00", "17:30")
    var selectedTimeSlot by remember { mutableStateOf(timeSlots[0]) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, DividerGrey, RoundedCornerShape(24.dp))
                .testTag("booking_dialog"),
            color = SoftWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Agendar Serviço",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryOrange
                        )
                        Text(
                            text = service.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .background(DividerGrey.copy(alpha = 0.4f), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = TextDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BarberImage(
                            imageUrl = null,
                            imageType = service.imageType,
                            contentDescription = service.name,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryOrange
                            )
                            Text(
                                text = service.duration,
                                fontSize = 13.sp,
                                color = TextDark
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = RatingGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${service.rating} (${service.reviewCount} avaliações)",
                                    fontSize = 12.sp,
                                    color = TextSoftGrey
                                )
                            }
                        }
                        Text(
                            text = "R$ %.2f".format(service.price),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Select Stylist/Barber
                Text(
                    text = "Escolha o Profissional",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    matchingStylists.forEach { stylist ->
                        val isSelected = stylist == selectedStylist
                        val cardBg = if (isSelected) PrimaryOrange else SoftWhite
                        val textCol = if (isSelected) SoftWhite else TextDark
                        val borderCol = if (isSelected) PrimaryOrange else DividerGrey

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardBg)
                                .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                                .clickable { selectedStylist = stylist }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isSelected) SoftWhite else PrimaryOrange,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        if (isSelected) Color.White.copy(alpha = 0.2f) else DividerGrey,
                                        CircleShape
                                    )
                                    .padding(4.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = stylist.name.split(" ").firstOrNull() ?: "",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textCol
                                )
                                Text(
                                    text = "★ ${stylist.rating}",
                                    fontSize = 11.sp,
                                    color = if (isSelected) SoftWhite.copy(0.9f) else TextSoftGrey
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Select Date
                Text(
                    text = "Selecione a Data",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableDates.size) { index ->
                        val item = availableDates[index]
                        val isSelected = index == selectedDateIdx
                        val boxBg = if (isSelected) PrimaryOrange else DividerGrey.copy(alpha = 0.3f)
                        val textCol = if (isSelected) SoftWhite else TextDark
                        val labelCol = if (isSelected) SoftWhite.copy(alpha = 0.8f) else TextSoftGrey

                        Column(
                            modifier = Modifier
                                .width(70.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(boxBg)
                                .clickable { selectedDateIdx = index }
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.first,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                color = labelCol
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.second.split(" ").first(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textCol
                            )
                            Text(
                                text = item.second.split(" ").lastOrNull() ?: "",
                                fontSize = 11.sp,
                                color = labelCol
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Select Time
                Text(
                    text = "Horário",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeSlots.chunked(3).forEach { rowSlots ->
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowSlots.forEach { slot ->
                                val isSelected = slot == selectedTimeSlot
                                val slotBg = if (isSelected) DarkBlack else DividerGrey.copy(alpha = 0.3f)
                                val slotText = if (isSelected) SoftWhite else TextDark

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(slotBg)
                                        .clickable { selectedTimeSlot = slot }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = if (isSelected) PrimaryOrange else TextSoftGrey,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = slot,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = slotText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Confirm Booking Button
                Button(
                    onClick = {
                        val fullDate = availableDates[selectedDateIdx].second
                        viewModel.bookAppointment(
                            serviceName = service.name,
                            category = service.category,
                            date = fullDate,
                            timeSlot = selectedTimeSlot,
                            barberName = selectedStylist.name,
                            price = service.price
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("confirm_booking_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Confirmar Agendamento",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
