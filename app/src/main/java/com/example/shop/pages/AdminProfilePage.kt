package com.example.shop.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.GlobalNavigation
import com.example.shop.R
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.ShopViewModel
import com.example.shop.viewmodel.ShopViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

@Composable
fun AdminProfilePage(modifier: Modifier = Modifier) {
    val vm: ShopViewModel = viewModel(
        factory = ShopViewModelFactory(Repository())
    )
    val user by vm.user.collectAsState()

    LaunchedEffect(Unit) {
        vm.getUser()
    }
    Column (modifier = modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Your profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Image(painter = painterResource(id = R.drawable.profile), contentDescription = "profile pic",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(16.dp)))
        Text(text = user?.name ?: "", fontSize = 26.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Email: \n ${user?.email}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = {
            FirebaseAuth.getInstance().signOut()
            val navController = GlobalNavigation.navController
            navController.popBackStack()
            navController.navigate("auth")
        }, modifier = Modifier.fillMaxWidth()
            .align(Alignment.CenterHorizontally)) {
            Text(text = "Sign out", fontSize = 18.sp)
        }

    }
}