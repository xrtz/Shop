package com.example.shop.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun CheckoutPage(modifier: Modifier = Modifier) {
    val userModel = remember{
        mutableStateOf(UserModel())
    }
    val productList = remember{
        mutableStateListOf(ProductModel())
    }
    val subTotal = remember{
        mutableStateOf(0f)
    }
    val withoutDiscont = remember{
        mutableStateOf(0f)
    }
    val diffSum = remember{
        mutableStateOf(0f)
    }
    fun calculateSubTotal()
    {
        productList.forEach{
            if (it.actualPrice.isNotEmpty()){
                val qty = userModel.value.cartItems[it.id]?: 0
                subTotal.value += qty * it.actualPrice.toFloat()
            }
        }
    }
    fun withoutDiscontCalculate()
    {
        productList.forEach{
            if (it.price.isNotEmpty()){
                val qty = userModel.value.cartItems[it.id]?: 0
                withoutDiscont.value += qty * it.price.toFloat()
            }
        }
    }
    fun calculateDiff(){
        diffSum.value = withoutDiscont.value - subTotal.value
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null){
                        userModel.value = result
                        Firebase.firestore.collection("data").document("stock")
                            .collection("products")
                            .whereIn("id", userModel.value.cartItems.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    val resultProducts = task.result.toObjects(ProductModel::class.java)
                                    productList.addAll(resultProducts)
                                    calculateSubTotal()
                                    withoutDiscontCalculate()
                                    calculateDiff()

                                }
                            }
                    }
                }
            }
    }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Checkout", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Deliver to: ", fontWeight = FontWeight.Bold)
        Text(text = "${userModel.value.name}")
        Text(text = "${userModel.value.address}")
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Total", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = withoutDiscont.value.toString(), fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Discount", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = diffSum.value.toString(), fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "To Pay", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(text = "${subTotal.value}", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}