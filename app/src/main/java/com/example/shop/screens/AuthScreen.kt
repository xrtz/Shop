package com.example.shop.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shop.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = stringResource(R.string.welcome_on_auth),
            style = TextStyle(
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
        )
        OutlinedButton(onClick = {
            navController.navigate("login")
        },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
        {
            Text(text = stringResource(R.string.button_login_name), style = TextStyle(fontSize = 20.sp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = {
            navController.navigate("reg")
        },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
        {
            Text(text = stringResource(R.string.button_reg_name), style = TextStyle(fontSize = 20.sp))
        }
    }
}