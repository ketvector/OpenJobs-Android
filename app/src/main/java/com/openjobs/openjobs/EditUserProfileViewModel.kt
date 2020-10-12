package com.openjobs.openjobs

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditUserProfileViewModel : ViewModel() {

    val TAG = "EditUserProfileViewModel"
    val submitRequestStatus = MutableLiveData<Boolean>()

    @SuppressLint("LongLogTag")
    fun submitProfile(name: String?, address: String?) {
        val userProfile = UserProfile(
            creatorId = FirebaseAuth.getInstance().currentUser?.uid,
            name = name,
            address = address
        )

        val db = Firebase.firestore
        db
            .collection(DatabaseConstants.USER_PROFILE_COLLECTION)
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "UNKNOWN")
            .set(userProfile)
            .addOnSuccessListener {
                submitRequestStatus.value = true
                Log.d(TAG, "success edit user profile")
            }
            .addOnFailureListener {
                submitRequestStatus.value = false
                Log.e(TAG, "failure edit user profile")
            }
    }
}