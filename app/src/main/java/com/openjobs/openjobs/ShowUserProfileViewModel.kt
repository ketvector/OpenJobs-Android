package com.openjobs.openjobs

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ShowUserProfileViewModel : ViewModel() {

    val name = MutableLiveData<String?>()
    val address = MutableLiveData<String?>()
    val phone = MutableLiveData<String?>()

    val TAG = "ShowUserProfileViewModel"
    var requestListener : ListenerRegistration? = null

    @SuppressLint("LongLogTag")
    fun getUserProfile(){

        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        phone.value = FirebaseAuth.getInstance().currentUser?.phoneNumber
        val db = Firebase.firestore

        requestListener =  db
            .collection(DatabaseConstants.USER_PROFILE_COLLECTION)
            .document(userUid ?: "NOEXIST")
            .addSnapshotListener{snapshot, e ->
                Log.d(TAG,"got snapshot")

                if(e != null){
                    Log.e(TAG, "Listen failed")
                    return@addSnapshotListener
                }

                if(snapshot != null && snapshot.exists()){
                    Log.d(TAG, snapshot.toString())
                    val userProfile = snapshot.toObject<UserProfile>()
                    name.value = userProfile?.name
                    address.value = userProfile?.address
                }
            }
    }

    override fun onCleared() {
        requestListener?.remove()
        super.onCleared()
    }
}