package com.example.diseasedetector.ui.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.diseasedetector.ui.util.ChoiceBtn
import com.example.diseasedetector.ui.util.ImageActionBtn
import com.example.diseasedetector.viewmodel.DiseaseViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(navController: NavHostController, organId: Int, viewModel: DiseaseViewModel) {
    val organ = viewModel.organList.find { it.id == organId }
    var selectedDisease by remember { mutableStateOf<String?>(null) }
    var isTakePictureSelected by remember { mutableStateOf(false) }
    var isUploadPictureSelected by remember { mutableStateOf(false) }

    LaunchedEffect(organId) {
        selectedDisease = null
        isTakePictureSelected = false
        isUploadPictureSelected = false
        viewModel.clearImageUri()
    }
    val selectedUri by viewModel.selectedImageUri.collectAsState()
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current

    val photoUri = remember {
        if (!isInPreview) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                File.createTempFile("IMG_", ".jpg", context.cacheDir)
            )
        } else {
            null
        }
    }

    var showText by remember { mutableStateOf(false) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                viewModel.setImageUri(uri)
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setImageUri(it) }
    }

    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                if (organ != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("main") }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
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
                .verticalScroll(scrollState)
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
                            onClick = {
                                selectedDisease = disease
                            }
                        )
                    }

                    ImageActionBtn(
                        isTakePictureSelected = isTakePictureSelected,
                        isUploadPictureSelected = isUploadPictureSelected,
                        onTakePictureClick = {
                            if (selectedDisease == null) {
                                showText = true
                            } else {
                                showText = false
                                isTakePictureSelected = true
                                isUploadPictureSelected = false
                                photoUri?.let {
                                    takePictureLauncher.launch(photoUri)
                                }
                            }
                        },
                        onUploadPictureClick = {
                            if (selectedDisease == null) {
                                showText = true
                            } else {
                                showText = false
                                isTakePictureSelected = false
                                isUploadPictureSelected = true
                                pickImageLauncher.launch("image/*")
                            }
                        }
                    )

                    if (showText) {
                        Text(
                            "* Please select a disease",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            color = Color.Red,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W500
                        )
                    }

                    selectedUri?.let { uri ->
                        if (selectedDisease == "Pneumonia") {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                                    .height(215.dp)
                                    .clip(RoundedCornerShape(7.dp))
                            )

                            LaunchedEffect(uri) {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                if (bitmap != null) {
                                    viewModel.classifyPneumonia(context, bitmap)
                                }
                            }

                            val result by viewModel.pneumoniaResult.collectAsState()
                            result?.let {
                                Text(
                                    text = "Prediction: $it",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    modifier = Modifier.padding(8.dp),
                                    color = if (it.contains("Pneumonia", true)) Color.Red else Color.Green
                                )
                            }
                        }else if (selectedDisease == "Tuberculosis") {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                                    .height(215.dp)
                                    .clip(RoundedCornerShape(7.dp))
                            )

                            LaunchedEffect(uri) {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                if (bitmap != null) {
                                    viewModel.classifyTuberculosis(context, bitmap)
                                }
                            }

                            val result by viewModel.tbResult.collectAsState()
                            result?.let {
                                Text(
                                    text = "Prediction: $it",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    modifier = Modifier.padding(8.dp),
                                    color = if (it.contains("Tuberculosis", true)) Color.Red else Color.Green
                                )
                            }
                        }
                    }

                }
            } else {
                Text("Organ not found", modifier = Modifier.padding(16.dp))
            }
        }
    }
}