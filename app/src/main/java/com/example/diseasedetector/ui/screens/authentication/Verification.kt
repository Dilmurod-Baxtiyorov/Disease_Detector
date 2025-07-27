package com.example.diseasedetector.ui.screens.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.ui.screens.statescreens.Error
import com.example.diseasedetector.ui.screens.statescreens.Loading
import com.example.diseasedetector.ui.state.UiEvent
import com.example.diseasedetector.ui.theme.Purple
import com.example.diseasedetector.viewmodel.VerificationViewModel
import com.example.diseasedetector.ui.state.UiState
import kotlinx.coroutines.delay

@Composable
fun Verification(
    vm: VerificationViewModel,
    navController: NavHostController,
    phoneNumber: String
){
    val uiState by vm.uiState
    val context = LocalContext.current

    val code by vm.code.collectAsState()

    LaunchedEffect(vm.timer) {
        delay(1000)
        vm.timeOut()
    }

    LaunchedEffect(Unit) {
        vm.event.collect{event->
            when(event){
                is UiEvent.Navigate -> navController.navigate(event.route)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        when(uiState){
            is UiState.Idle, UiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.Start
                    ){
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(32.dp))
                        }
                    }
                    Image(painterResource(R.drawable.password), null, contentScale = ContentScale.Crop, modifier = Modifier.size(220.dp))
                    Text("Enter the code", fontSize = 36.sp, fontWeight = FontWeight.W600, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth(0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("A 6-digit code will be sent to the number you entered $phoneNumber", fontSize = 16.sp, fontWeight = FontWeight.W300, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth(0.8f))
                    Spacer(modifier = Modifier.height(48.dp))
                    BasicTextField(
                        value = code,
                        onValueChange = vm::onCodeChange,
                        singleLine = true,
                        enabled = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                            }
                        ),
                        decorationBox = {
                            Row(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(70.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            )
                            {
                                repeat(6) { index ->
                                    Card(
                                        modifier = Modifier
                                            .size(38.dp, 50.dp),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 5.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Text(
                                                text = code.getOrNull(index)?.toString() ?: "",
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(64.dp))
                    Button(
                        onClick = {
                            vm.signup()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(52.dp)
                    ) {
                        Text("Submit", fontSize = 18.sp, fontWeight = FontWeight.W400)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    if(vm.timer > 0 && vm.sms == 1){
                        Text(
                            text = "Resend code in ${vm.timer}s",
                            color = Color.Gray
                        )
                    }else if(vm.timer > 0 && vm.sms > 2){
                        Text(
                            text = "${vm.timer}s until timeout",
                            color = Color.Gray
                        )
                    }
                    else if(vm.sms == 1){
                        ClickableText(
                            text = AnnotatedString( "Resend code", SpanStyle(color = Purple))
                        ){
                            vm.verifyPhoneNumber(context)
                            Toast.makeText(context, "RESEND CODE", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        ClickableText(
                            text = AnnotatedString("Email/password verification", SpanStyle(color = Purple))
                        ) {

                        }
                    }
                }
            }
            is UiState.Loading -> {
                Loading()
            }
            is UiState.Error -> {
                Error((uiState as UiState.Error).msg)
            }
        }
    }
}