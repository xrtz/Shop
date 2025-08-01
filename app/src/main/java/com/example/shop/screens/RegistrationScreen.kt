package com.example.shop.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shop.R
import com.example.shop.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(modifier: Modifier = Modifier,navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember{
        mutableStateOf(false)
    }
    var context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = stringResource(R.string.text_reg_screen),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = name, onValueChange =
        {
            name = it
        },
            label = { Text(text = stringResource(R.string.name))},
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = email, onValueChange =
        {
            email = it
        },
            label = { Text(text = stringResource(R.string.email))},
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = password, onValueChange =
        {
            password = it
        },
            label = { Text(text = stringResource(R.string.password))},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = {
            isLoading = true
            authViewModel.registration(email, name, password){
                success, errorMessage->
                if (success){
                    isLoading = false
                    navController.navigate("home"){
                        popUpTo("auth"){inclusive = true}
                    }
                }
                else{
                    isLoading = false
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
        {
            Text(text = stringResource(R.string.button_reg_name), style = TextStyle(fontSize = 20.sp))
        }
    }
}