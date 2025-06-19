package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.model.DiseaseViewModel
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
        Column(modifier = Modifier.padding(innerPadding)) {


        }
    }
}