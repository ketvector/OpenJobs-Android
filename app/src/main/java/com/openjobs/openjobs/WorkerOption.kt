package com.openjobs.openjobs

data class WorkerOption(var id : String = "-1",
                        var label : String = "label",
                        var price : Int = 0 ,
                        var unit : String? = "unit",
                        var showAtTop : Boolean = false,
                        var showAtBottom : Boolean = false){
}