package com.openjobs.openjobs

import java.util.*


data class WorkerRequest(
    var creatorUid : String? = null,
    var numSkilledWorkers : Int = 0,
    var numUnskilledWorkers : Int = 0,
    var requestState : String? = null,
    var date : Date = Date()
)