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
import java.util.*
import kotlin.collections.ArrayList

class CreateWorkerRequestViewModel : ViewModel() {

    val submitRequestResult = MutableLiveData<Boolean>()
    private val TAG = "CreateWorkerRequestViewModel"
    private var userProfileRequestListener : ListenerRegistration? = null
    val address = MutableLiveData<String?>()
    var workerOptionsList = MutableLiveData<List<WorkerOption>>()
    lateinit var selections : Map<String,Int>
    var userGivenAddress : String ? = null
    var userGivenDate : Date ? = null



    @SuppressLint("LongLogTag")
    fun submitRequest(){
        val db = Firebase.firestore
        val workerRequest = WorkerRequest()
        workerRequest.creatorUid = FirebaseAuth.getInstance().currentUser?.uid
        workerRequest.requestState = DatabaseConstants.WORKER_REQUESTS_STATE_APPLIED
        userGivenDate?.let{
            workerRequest.date = it
        }
        workerRequest.address = userGivenAddress
        workerRequest.listOfWorkerOptions = selections


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

        userProfileRequestListener =  db
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

    @SuppressLint("LongLogTag")
    fun getWorkerOptions(){
        val db = Firebase.firestore

        db
            .collection(DatabaseConstants.WORKER_OPTIONS_COLLECTION)
            .get()
            .addOnSuccessListener { collection ->
                val list = ArrayList<WorkerOption>()
                for(document in collection){
                    list.add(document.toObject<WorkerOption>())
                }
                workerOptionsList.value = list
            }
            .addOnFailureListener { e -> Log.e(TAG, e.toString()) }

    }

    override fun onCleared() {
        userProfileRequestListener?.remove()
        super.onCleared()
    }

}