package com.example.shop.pages

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.GlobalNavigation
import com.example.shop.Util
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.CheckoutViewModel
import com.example.shop.viewmodel.CheckoutViewModelFactory
import com.example.shop.viewmodel.ShopViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun CheckoutPage(viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(
    Repository()
)
)) {
    val state by viewModel.state.collectAsState()

    val user = state.user
    val subTotal = state.subTotal
    val fullPrice = state.fullPrice
    val discount = state.discount

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Checkout", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (user != null) {
            Text("Deliver to: ", fontWeight = FontWeight.Bold)
            Text("${user.name}")
            Text("${user.address}")
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("Total", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(fullPrice.toString(), fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("Discount", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(discount.toString(), fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Text("-> $subTotal", fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        OutlinedButton(
            onClick = {
                viewModel.completeOrder()
                Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show()
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("To Pay", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}
