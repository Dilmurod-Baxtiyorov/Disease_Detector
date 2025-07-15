package com.example.diseasedetector.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.diseasedetector.R
import com.example.diseasedetector.navigation.Routes
import com.example.diseasedetector.ui.screens.statescreens.Error
import com.example.diseasedetector.ui.screens.statescreens.Loading
import com.example.diseasedetector.ui.state.UiEvent
import com.example.diseasedetector.ui.theme.Purple
import com.example.diseasedetector.viewmodel.AuthViewModel
import com.example.diseasedetector.viewmodel.LoginViewModel
import com.example.diseasedetector.ui.state.UiState

@Composable
fun Login(
    vm: LoginViewModel,
    authVM: AuthViewModel,
    navController: NavHostController
){
    val context = LocalContext.current
    val uiState =
        if(vm.uiState.value == UiState.Idle || vm.uiState.value == UiState.Success){
            authVM.uiState.collectAsStateWithLifecycle()
        }else{
            vm.uiState
        }

    val identifier by vm.identifier.collectAsState()
    val password by vm.password.collectAsState()


    LaunchedEffect(Unit) {
        authVM.event.collect{event ->
            when(event){
                is UiEvent.Navigate -> navController.navigate(event.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.event.collect{event ->
            when(event){
                is UiEvent.Navigate -> navController.navigate(event.route)
            }
        }
    }

    when(uiState.value){
        is UiState.Idle, UiState.Success ->{
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Welcome!", fontSize = 40.sp, fontWeight = FontWeight.W600)
                Spacer(modifier = Modifier.height(48.dp))
                Image(painterResource(R.drawable.login), null, contentScale = ContentScale.Crop, modifier = Modifier.size(200.dp))
                Spacer(modifier = Modifier.height(4.dp))
                if(vm.warning == "email"){
                    Text(
                        text = "We have sent email to you\nFollow the link in the email to reset password\n",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        letterSpacing = 0.5.sp
                    )
                }
                OutlinedTextField(
                    value = identifier,
                    onValueChange = vm::onIdentifierChange,
                    placeholder = {
                        Text("Phone number/Email", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Call, null, tint = Color.Gray, modifier = Modifier
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
                if(authVM.warning == "exist"){
                    Text(
                        text = "This phone number is not registered",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        letterSpacing = 0.5.sp)
                }else {
                    when (vm.warning) {
                        "empty" -> {
                            Text(
                                text = "Enter your phone number",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                letterSpacing = 0.5.sp
                            )
                        }
                        "wrong" -> {
                            Text(
                                text = "This email is not registered",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                letterSpacing = 0.5.sp
                            )
                        }

                        "phone" -> {
                            Text(
                                text = "Incorrect phone number format",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = vm::onPasswordChange,
                    placeholder = {
                        Text("Password(only for email)", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, null, tint = Color.Gray, modifier = Modifier
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
                        text = "You entered wrong password",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        letterSpacing = 0.5.sp
                    )
                }else if(vm.warning == "blank"){
                    Text(
                        text = "Enter your password",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        letterSpacing = 0.5.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Forgot your password?",
                    color = Purple,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .clickable {
                            if (vm.checkIdentifier()){
                                if(identifier.contains("@")){
                                    vm.resetPassword(email = identifier)
                                }
                            }
                        }
                        .fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        if(vm.checkIdentifier()){
                            if(identifier.contains("@")){
                                if(vm.checkPassword()){
                                    vm.login()
                                }
                            }else{
                                if(vm.checkPhoneNumber()){
                                    authVM.verifyPhoneNumber(context, identifier)
                                }
                            }
                        }
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
                Row {
                    Text(text = "New here?")

                    Text(
                        text = " Sign up..",
                        color = Purple,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.Signup.name)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        is UiState.Loading -> {
            Loading()
        }
        is UiState.Error -> {
            Error((uiState.value as UiState.Error).msg)
        }
    }
}