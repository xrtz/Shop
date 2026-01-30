package com.example.shop.repository

import com.example.shop.Util
import com.example.shop.model.CategoryModel
import com.example.shop.model.OrderModel
import com.example.shop.model.ProductModel
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Repository {

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    private fun userId(): String =
        auth.currentUser?.uid ?: error("User not logged")

    fun cartItemsFlow(): Flow<Map<String, Int>> = callbackFlow {
        val docRef = firestore.collection("users").document(userId())
        val listener = docRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val rawCart = snapshot.get("cartItems") as? Map<String, Number> ?: emptyMap()
                trySend(rawCart.mapValues { it.value.toInt() })
            }
        }
        awaitClose { listener.remove() }
    }
    fun userFlow(): Flow<UserModel> = callbackFlow {
        val docRef = firestore.collection("users").document(userId())
        val listener = docRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                snapshot.toObject(UserModel::class.java)?.let { trySend(it) }
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun getProducts(): List<ProductModel> {
        val snapshot = firestore.collection("data")
            .document("stock")
            .collection("products")
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(ProductModel::class.java)
        }
    }

    suspend fun updateAddress(address: String) {
        firestore.collection("users")
            .document(userId())
            .update("address", address)
            .await()
    }

    suspend fun addToCart(productId: String) {
        val userDoc = firestore.collection("users").document(userId())
        val snap = userDoc.get().await()
        val cart = snap.get("cartItems") as? Map<String, Long> ?: emptyMap()
        val newQty = (cart[productId] ?: 0) + 1
        userDoc.update("cartItems.$productId", newQty).await()
    }

    suspend fun removeFromCart(productId: String, removeAll: Boolean = false) {
        val userDoc = firestore.collection("users").document(userId())
        val snap = userDoc.get().await()
        val cart = snap.get("cartItems") as? Map<String, Long> ?: emptyMap()
        val newQty = (cart[productId] ?: 0) - 1

        val update =
            if (newQty <= 0 || removeAll)
                mapOf("cartItems.$productId" to FieldValue.delete())
            else
                mapOf("cartItems.$productId" to newQty)

        userDoc.update(update).await()
    }

    suspend fun toggleFavorite(productId: String) {
        val userDoc = firestore.collection("users").document(userId())
        val snap = userDoc.get().await()
        val favs = snap.get("favItems") as? List<String> ?: emptyList()

        val updated = favs.toMutableList().apply {
            if (contains(productId)) remove(productId) else add(productId)
        }

        userDoc.update("favItems", updated).await()
    }

    suspend fun getCategories(): List<CategoryModel> {
        val snapshot = firestore.collection("data")
            .document("stock")
            .collection("categories")
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(CategoryModel::class.java)
        }
    }

    suspend fun addProduct(product: ProductModel) {
        firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(product.id)
            .set(product)
            .await()
    }


    suspend fun getUserOrders(): List<OrderModel> {
        val uid = userId()
        val snapshot = firestore.collection("orders")
            .whereEqualTo("userId", uid)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(OrderModel::class.java)
        }
    }

    suspend fun getUserCart(): Map<String, Int> {
        val uid = userId()

        val snapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        val rawCart = snapshot.get("cartItems") as? Map<String, Number> ?: emptyMap()

        return rawCart.mapValues { it.value.toInt() }
    }
    suspend fun getUser(): UserModel {
        val uid = userId()
        val snapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserModel::class.java)
            ?: throw IllegalStateException("User not found")

    }



}
