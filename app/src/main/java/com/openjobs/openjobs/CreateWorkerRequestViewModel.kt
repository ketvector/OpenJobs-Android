package com.openjobs.openjobs

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateWorkerRequestViewModel : ViewModel() {

    val submitRequestResult = MutableLiveData<Boolean>()
    private val TAG = "CreateWorkerRequestViewModel"
    var requestListener : ListenerRegistration? = null
    val address = MutableLiveData<String?>()



    @SuppressLint("LongLogTag")
    fun submitRequest(workerRequest : WorkerRequest){
        val db = Firebase.firestore
        workerRequest.creatorUid = FirebaseAuth.getInstance().currentUser?.uid
        workerRequest.requestState = DatabaseConstants.WORKER_REQUESTS_STATE_APPLIED

        db.
            collection(DatabaseConstants.WORKER_REQUESTS_COLLECTION)
            .add(workerRequest)
            .addOnSuccessListener{
                submitRequestResult.value = true
            }
            .addOnFailureListener{e ->
                Log.e(TAG,"error submitting request",e)
                submitRequestResult.value = false
            }
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(){

        val userUid = FirebaseAuth.getInstance().currentUser?.uid
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
                    address.value = userProfile?.address
                }
            }
    }

    override fun onCleared() {
        requestListener?.remove()
        super.onCleared()
    }

}