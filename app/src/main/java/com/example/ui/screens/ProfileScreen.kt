package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.BarberViewModel
import com.example.ui.components.BarberImage
import com.example.ui.theme.DividerGrey
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextSoftGrey

@Composable
fun ProfileScreen(
    viewModel: BarberViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfileFlow.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen_content"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 90.dp)
    ) {
        // 1. Profile Avatar, Name, Email and "Edit Profile" Button Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large picture container
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(DividerGrey)
                ) {
                    BarberImage(
                        imageUrl = null,
                        imageType = "avatar_mike",
                        contentDescription = "Foto de Mike Smith",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = profile?.name ?: "Mike Smith",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = profile?.email ?: "smithmike@42exp.com",
                    fontSize = 14.sp,
                    color = TextSoftGrey,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Orange "Edit Profile" Pill Button
                Button(
                    onClick = { showEditDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("edit_profile_button")
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // 2. Profile Setting Group Header and Cards (exactly matching mockup Phone 1)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Profile Setting",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Combined card container housing the options
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        ProfileOptionRow(
                            icon = Icons.Default.Group,
                            title = "Friend & Social",
                            onClick = {}
                        )
                        ProfileOptionRow(
                            icon = Icons.Default.Feedback,
                            title = "Feedback",
                            onClick = {}
                        )
                        ProfileOptionRow(
                            icon = Icons.Default.CardGiftcard,
                            title = "Gift Card",
                            onClick = {},
                            showDivider = false
                        )
                    }
                }
            }
        }

        // 3. Others Setting Group (exactly matching mockup Phone 1)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Others Setting",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Combined card container
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        ProfileOptionRow(
                            icon = Icons.Default.CreditCard,
                            title = "Payment",
                            onClick = {}
                        )
                        ProfileOptionRow(
                            icon = Icons.Default.ImportContacts,
                            title = "About Books",
                            onClick = {},
                            showDivider = true
                        )
                        ProfileOptionRow(
                            icon = Icons.Default.Info,
                            title = "Welcome Screens",
                            onClick = { viewModel.resetOnboarding() },
                            showDivider = false
                        )
                    }
                }
            }
        }
    }

    // Inline dialog overlay to edit details inside database
    if (showEditDialog) {
        var tempName by remember { mutableStateOf(profile?.name ?: "") }
        var tempEmail by remember { mutableStateOf(profile?.email ?: "") }

        Dialog(onDismissRequest = { showEditDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SoftWhite,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("edit_profile_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Edit Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        IconButton(onClick = { showEditDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_edit"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark,
                            focusedBorderColor = PrimaryOrange,
                            unfocusedBorderColor = DividerGrey
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_email_edit"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark,
                            focusedBorderColor = PrimaryOrange,
                            unfocusedBorderColor = DividerGrey
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.updateUserProfile(tempName, tempEmail)
                            showEditDialog = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_profile_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Salvar Alterações",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileOptionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TextSoftGrey,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver",
                tint = TextSoftGrey,
                modifier = Modifier.size(20.dp)
            )
        }

        if (showDivider) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DividerGrey.copy(alpha = 0.5f))
            )
        }
    }
}
