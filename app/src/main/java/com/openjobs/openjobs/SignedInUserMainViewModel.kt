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

class SignedInUserMainViewModel : ViewModel() {

    val TAG = "SignedInUserMainViewModel"
    val listOfWorkerRequests = MutableLiveData<List<WorkerRequestDocumentWrapper>>()
    var requestListener : ListenerRegistration? = null
    var availableWorkerOptions = MutableLiveData<List<WorkerOption>>()

    @SuppressLint("LongLogTag")
    fun getWorkerRequestsForThisUser(){

        val db = Firebase.firestore
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid



        requestListener =  db
            .collection(DatabaseConstants.WORKER_REQUESTS_COLLECTION)
            .whereEqualTo("creatorUid" , currentUserUid)
            .orderBy("date")
            .addSnapshotListener{snapshot, e ->
                Log.d(TAG,"got snapshot")

                val list = ArrayList<WorkerRequestDocumentWrapper>()

                if(e != null){
                    Log.e(TAG, "Listen failed")
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    Log.d(TAG, snapshot.size().toString())
                    for(document in snapshot){
                        list.add(WorkerRequestDocumentWrapper(workerRequest = document.toObject<WorkerRequest>(), docId = document.id))
                    }
                }

                listOfWorkerRequests.value = list
            }


    }

    @SuppressLint("LongLogTag")
    fun getIdToNameMap(){
        val db = Firebase.firestore

        db
            .collection(DatabaseConstants.WORKER_OPTIONS_COLLECTION)
            .get()
            .addOnSuccessListener {collection ->
                val list = ArrayList<WorkerOption>()
                for(document in collection){
                    val workerOption = document.toObject<WorkerOption>()
                    list.add(workerOption)
                }
                availableWorkerOptions.value = list
            }
            .addOnFailureListener { e -> Log.e(TAG,e.toString()) }
    }

    @SuppressLint("LongLogTag")
    fun deleteDocument(docId : String?){
        docId?.let {
            val db = Firebase.firestore

            db
                .collection(DatabaseConstants.WORKER_REQUESTS_COLLECTION)
                .document(docId)
                .delete()
                .addOnSuccessListener { Log.d(TAG,"deleted successfully") }
                .addOnFailureListener{ Log.e(TAG, "delete failed")}
        }

    }

    override fun onCleared() {
        requestListener?.remove()
        super.onCleared()
    }
}