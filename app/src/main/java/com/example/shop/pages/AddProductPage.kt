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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.GlobalNavigation
import com.example.shop.model.CategoryModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.example.shop.repository.Repository
import com.example.shop.viewmodel.AdminViewModel
import com.example.shop.viewmodel.AdminViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID

@Composable
fun AddProductPage(modifier: Modifier = Modifier) {

    val vm: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(Repository())
    )

    val categories by vm.categories.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadCategories()
    }

    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var actualPrice by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<CategoryModel?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        TextField(name, { name = it }, placeholder = { Text("Название") })
        TextField(desc, { desc = it }, placeholder = { Text("Описание") })
        TextField(price, { price = it }, placeholder = { Text("Цена") })
        TextField(actualPrice, { actualPrice = it }, placeholder = { Text("Актуальная цена") })
        TextField(image, { image = it }, placeholder = { Text("Картинка") })

        Spacer(Modifier.height(12.dp))

        categories.forEach { cat ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = cat == selected,
                        onClick = { selected = cat },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = cat == selected, onClick = null)
                Text(cat.name)
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                selected?.let {
                    vm.addProduct(
                        name, desc, it.name.lowercase(),
                        actualPrice, price, image
                    ) {
                        GlobalNavigation.navController.popBackStack()
                        GlobalNavigation.navController.navigate("admin-home")
                    }
                }
            }
        ) {
            Text("+")
        }
    }
}
