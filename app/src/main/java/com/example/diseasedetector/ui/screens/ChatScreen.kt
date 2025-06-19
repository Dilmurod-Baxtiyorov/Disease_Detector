package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.ui.util.SplitFloatingButton

@Composable
fun ChatScreen(navController: NavHostController, viewModel: DiseaseViewModel) {
    val isChatSelected by viewModel.isChatSelected.collectAsState()
    Scaffold(
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