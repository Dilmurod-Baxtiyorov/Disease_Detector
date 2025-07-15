package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.ui.util.ChoiceBtn
import com.example.diseasedetector.ui.util.ImageActionBtn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(navController: NavHostController, organId: Int, viewModel: DiseaseViewModel) {
    val organ = viewModel.organList.find { it.id == organId }
    var selectedDisease by remember { mutableStateOf<String?>(null) }
    var isTakePictureSelected by remember { mutableStateOf(false) }
    var isUploadPictureSelected by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                if (organ != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("main") }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            "${organ.name} Analysis",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                actionIconContentColor = Color.Black
            )
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (organ != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        "For which disease would you\nlike the X-ray analyzed?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W300
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    organ.diseases.forEach { disease ->
                        ChoiceBtn(
                            isSelected = selectedDisease == disease,
                            disease = disease,
                            onClick = { selectedDisease = disease }
                        )
                    }

                    ImageActionBtn(
                        isTakePictureSelected = isTakePictureSelected,
                        isUploadPictureSelected = isUploadPictureSelected,
                        onTakePictureClick = {
                            isTakePictureSelected = true
                            isUploadPictureSelected = false
                        },
                        onUploadPictureClick = {
                            isTakePictureSelected = false
                            isUploadPictureSelected = true
                        }
                    )
                }
            } else {
                Text("Organ not found", modifier = Modifier.padding(16.dp))
            }
        }
    }
}