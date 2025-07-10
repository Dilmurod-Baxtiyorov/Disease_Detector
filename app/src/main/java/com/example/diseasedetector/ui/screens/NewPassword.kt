package com.example.diseasedetector.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.ui.state.UiEvent
import com.example.diseasedetector.ui.theme.Purple
import com.example.diseasedetector.viewmodel.NewPasswordViewModel
import com.example.handybook.state.UiState

@Composable
fun NewPassword(
    navController: NavHostController,
    vm: NewPasswordViewModel,
    phoneNumber: String
){
    val context = LocalContext.current
    val uiState = vm.uiState.value

    val newPassword by vm.newPassword.collectAsState()
    val confirmPassword by vm.confirmPassword.collectAsState()

    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            when(event) {
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
            }
        }
    }

    when(uiState){
        is UiState.Success, UiState.Idle ->{
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(painterResource(R.drawable.password), null, contentScale = ContentScale.Crop, modifier = Modifier.size(240.dp))
                Spacer(modifier = Modifier.height(24.dp))
                Text("Reset Password", fontSize = 36.sp, fontWeight = FontWeight.W600, modifier = Modifier.fillMaxWidth(0.8f), textAlign = TextAlign.Start)
                Text("Create new password for your account", fontSize = 16.sp, fontWeight = FontWeight.W300, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth(0.8f))
                Spacer(modifier = Modifier.height(48.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = vm::onNewPasswordChange,
                    placeholder = {
                        Text("New password", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock, null, tint = Color.Gray, modifier = Modifier
                                .size(40.dp)
                                .padding(start = 12.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = vm::onConfirmPasswordChange,
                    placeholder = {
                        Text("Confirm new password", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Lock, null, tint = Color.Gray, modifier = Modifier
                                .size(40.dp)
                                .padding(start = 12.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                if(vm.warning == "password"){
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        letterSpacing = 0.5.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(64.dp))
                Button(
                    onClick = {
                        vm.changePassword(context, phoneNumber)
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
                    Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.W400)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.height(46.dp))
            }
        }
        is UiState.Loading -> {
            Loading()
        }
        is UiState.Error ->{
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(uiState.msg)
            }
        }
    }
}