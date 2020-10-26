package com.openjobs.openjobs

import java.util.*


data class WorkerRequest(
    var creatorUid : String? = null,
    var requestState : String? = null,
    var date : Date = Date(),
    var address : String? = null,
    var listOfWorkerOptions : Map<String,Int>? = null,
    var shortDescription : String? = null,
    var meetMessage : String? = null,
    var additionalMessage : String ? = null
)