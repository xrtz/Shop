package com.example.shop

import android.content.Context
import android.widget.Toast
import com.example.shop.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.lang.reflect.Field
import java.util.UUID

object Util {
    fun addToCard(context: Context, productId: String){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?: 0
                val updetedQuantity = currentQuantity + 1
                val updatedCart = mapOf("cartItems.$productId" to updetedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(context, "+", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "x", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun removeFromCard(context: Context, productId: String, removeAll: Boolean = false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId]?: 0
                val updetedQuantity = currentQuantity - 1
                val updatedCart =
                    if (updetedQuantity <= 0 || removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updetedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(context, "-", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "x", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun clearBusketAndAddToOrder(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener{
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val order = OrderModel(
                    id = UUID.randomUUID().toString(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                    date = Timestamp.now()
                    ,
                    items = currentCart,
                    address = it.result.get("address") as String,
                    status = "ordered"
                )
                Firebase.firestore.collection("orders")
                    .document(order.id).set(order).addOnCompleteListener {
                        if (it.isSuccessful){
                            userDoc.update("cartItems",FieldValue.delete())
                        }
                    }
            }
        }
    }
}