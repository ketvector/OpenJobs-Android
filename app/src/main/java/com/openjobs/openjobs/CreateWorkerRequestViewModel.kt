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
import java.lang.StringBuilder
import java.text.SimpleDateFormat
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
    var userGivenShortDescription : String? = null
    var userGivenAdditionalMessage : String? = null


    val locations = MutableLiveData<List<SimpleLocation>>()
    lateinit var currentLocation : SimpleLocation

    fun getConfirmationMessage() : String ?{
        return "Workers count\n" + getNumWorkersText() + "\n\n" + "Location : " + currentLocation.name + "\nDate : " + getDateString(userGivenDate)
    }

    private fun getDateString(date : Date?) : String?{
        date?.let {
            val format = SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault())
            return format.format(date)
        }
        return null
    }

    private fun getNumWorkersText() : String?{
        workerOptionsList.value?.let {list ->
            val stringBuilder = StringBuilder()
            for(workerOption in list){
                selections[workerOption.id]?.let {count ->
                    if(count > 0){
                        stringBuilder.append(workerOption.label + ": " + count + getRateMessage(workerOption) + "\n")
                    }
                }
            }
            return stringBuilder.toString()
        }
        return null
    }

    private fun getRateMessage(workerOption: WorkerOption) : String{
        if(workerOption.price > 0){
           return " (at Rs " + workerOption.price + "/" + workerOption.unit.toString() + " per worker)"
        }
        else {
            return workerOption.unit?:""
        }

    }

    @SuppressLint("LongLogTag")
    fun getAvailableLocations(){

        val db = Firebase.firestore
        db.collection(DatabaseConstants.SIMPLE_LOCATIONS_COLLECTION).get().addOnSuccessListener {
            val list : ArrayList<SimpleLocation> = ArrayList()
            for(location in it){
                list.add(location.toObject())
            }
            locations.value = list
        }.addOnFailureListener { e -> Log.e(TAG,e.toString()) }
    }


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
        workerRequest.shortDescription = userGivenShortDescription
        workerRequest.meetMessage = currentLocation.defaultMeetMessage
        workerRequest.additionalMessage = userGivenAdditionalMessage

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
            .whereEqualTo("locationId",currentLocation.id)
            .orderBy("label")
            .get()
            .addOnSuccessListener { collection ->
                val mainList = ArrayList<WorkerOption>()
                val topList = ArrayList<WorkerOption>()
                val bottomList = ArrayList<WorkerOption>()

                for(document in collection){
                    val workerOption = document.toObject<WorkerOption>()
                    if(workerOption.showAtTop){
                        topList.add(workerOption)
                    }else if(workerOption.showAtBottom){
                        bottomList.add(workerOption)
                    }else{
                        mainList.add(workerOption)
                    }
                }

                val finalList = topList + mainList + bottomList
                workerOptionsList.value = finalList
            }
            .addOnFailureListener { e -> Log.e(TAG, e.toString()) }

    }

    override fun onCleared() {
        userProfileRequestListener?.remove()
        super.onCleared()
    }

}