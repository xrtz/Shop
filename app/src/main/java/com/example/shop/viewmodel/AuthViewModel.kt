package com.example.shop.viewmodel

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.shop.R
import com.example.shop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel(){
    private val auth = Firebase.auth

    private val firestore = Firebase.firestore
    fun login(){}

    fun registration(email: String, name: String, password: String, onResult : (Boolean, String?)-> Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                var userId = it.result?.user?.uid
                val userModel = UserModel(name, email, userId!!)
                firestore.collection("users").document(userId).set(userModel).addOnCompleteListener {
                    task->
                    if (task.isSuccessful){
                        onResult(true, null)
                    }
                    else{
                        onResult(false, R.string.message_firebase_error.toString())
                    }
                }
            }
            else{
                onResult(false, it.exception?.localizedMessage)
            }
        }
    }
}