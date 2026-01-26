package com.example.shop.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.shop.GlobalNavigation
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID

@Composable
fun AddProductPage(modifier: Modifier = Modifier) {
    var productName by remember {
        mutableStateOf("")
    }
    var descriptions by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var actualPrice by remember {
        mutableStateOf("")
    }
    var image by remember {
        mutableStateOf("")
    }
    val pr = CategoryModel()
    var radioOption by remember{
        mutableStateOf<List<CategoryModel>>(listOf(pr))
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("categories").get().addOnCompleteListener {
                if (it.isSuccessful){
                    val resultList = it.result.documents.mapNotNull {
                            doc-> doc.toObject(CategoryModel::class.java)
                    }
                    radioOption = resultList
                }
            }
    }
    var selected by remember {
        mutableStateOf(radioOption[0])
    }
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = productName,
            placeholder = { Text("Введите название продукта") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {productName = it})
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = descriptions,placeholder = { Text("Введите описание продукта") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {descriptions = it})
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = price, placeholder = { Text("Введите цену продукта") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {price = it})
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = actualPrice, placeholder = { Text("Введите актуальную цену продукта") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {actualPrice = it})
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = image, placeholder = { Text("Введите ссылку на картинку") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {image = it})
        Spacer(modifier = Modifier.height(10.dp))
        Column(Modifier.selectableGroup()) {
            radioOption.forEach { status ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (status.name == selected.name),
                            onClick = { selected = status },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (status.name == selected.name),
                        onClick = null
                    )
                    Text(status.name, Modifier.padding(start = 16.dp))
                }

            }
        }
        Spacer(modifier=Modifier.height(20.dp))
        OutlinedButton(onClick = {
            val ProductModel = ProductModel(UUID.randomUUID().toString(), productName, descriptions, selected.name.lowercase(), actualPrice, price, listOf(image))
            Firebase.firestore
                .collection("data")
                .document("stock")
                .collection("products")
                .document(ProductModel.id).set(ProductModel)
                .addOnCompleteListener {
                    val navController = GlobalNavigation.navController
                    navController.popBackStack()
                    navController.navigate("admin-home")
                }
                                 },
            modifier = Modifier.fillMaxWidth()) { Text(text = "+")}

    }
}