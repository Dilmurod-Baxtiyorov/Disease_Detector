package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.viewmodel.DiseaseViewModel
import com.example.diseasedetector.ui.util.SplitFloatingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, viewModel: DiseaseViewModel) {
    val isChatSelected by viewModel.isChatSelected.collectAsState()
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.docc_logo_horizonal),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .width(147.dp)
                            .height(50.dp)
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "User",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    actionIconContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            SplitFloatingButton(
                isChatSelected = isChatSelected == true,
                onChatClick = {
                    navController.navigate("chat")
                    viewModel.setChatSelected(true)
                },
                onAnalysisClick = {
                    navController.navigate("main")
                    viewModel.setChatSelected(false)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Hello 👋",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please enter your health changes and symptoms below.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(475.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(0.3.dp, Color(0x80000000), RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .width(168.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF8D7BFD))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Lorem ipsum is simply dummy text of the printing and typesetting.",
                                color = Color.White,
                                fontSize = 10.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = "Bot",
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(166.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFFC2C2C2))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Lorem Ipsum is simply dummy text of the printing and typesetting.",
                                color = Color.Black,
                                fontSize = 10.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(200.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .border(0.3.dp, Color(0x80000000), RoundedCornerShape(15.dp)),
                            value = "",
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    text = "Write a message...",
                                    color = Color.Gray
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    disabledContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.send),
                                    contentDescription = "Send",
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}