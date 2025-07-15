package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.viewmodel.DiseaseViewModel
import com.example.diseasedetector.ui.util.RayCard
import com.example.diseasedetector.ui.util.SplitFloatingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: DiseaseViewModel) {
    val isChatSelected by viewModel.isChatSelected.collectAsState()
    val organs = viewModel.organList
    LaunchedEffect(Unit) {
        viewModel.initializeIfNeeded(isChatStart = false)
    }
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
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(10.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("X-ray analysis", fontSize = 30.sp, fontWeight = FontWeight.W600)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(organs) { organ ->
                    RayCard(
                        img = organ.imageResId,
                        title = organ.name,
                        onClick = {
                            navController.navigate("organ/${organ.id}")
                        })
                }
            }

        }
    }
}