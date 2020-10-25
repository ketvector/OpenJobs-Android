package com.openjobs.openjobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.worker_request_list_item.view.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class WorkerRequestsListAdapter(val onClickListener: WorkerRequestCardClickListener) : RecyclerView.Adapter<WorkerRequestsListAdapter.ViewHolder>() {

    private var workerRequestList : List<WorkerRequestDocumentWrapper>? = null
    private var idToNameMap : Map<String,String>? = null

    fun setList(list : List<WorkerRequestDocumentWrapper>){
        workerRequestList = list
        notifyDataSetChanged()
    }

    fun setIdToNameMap(map : Map<String,String>){
        idToNameMap = map
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.worker_request_list_item, parent, false), onClickListener
        )
    }

    override fun getItemCount(): Int {
        return workerRequestList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workerRequestList?.get(position),idToNameMap)
    }

    class ViewHolder(itemView: View,
                     val onClickListener: WorkerRequestCardClickListener) : RecyclerView.ViewHolder(itemView) {

        var idToNameMap : Map<String,String>? = null

        fun bind(workerRequestWrapper: WorkerRequestDocumentWrapper?, idToNameMap : Map<String,String>?) {
            this.idToNameMap = idToNameMap
            val workerRequest = workerRequestWrapper?.workerRequest
            itemView.deleteButton.setOnClickListener{
                onClickListener.onDeleteButtonClicked(workerRequestWrapper?.docId)
            }
            itemView.date.text = "Date: " + getDateString(workerRequest?.date)
            itemView.status.text = "Status: " + getStatusText(workerRequest)
            itemView.count.text = getNumberOfWorkersText(workerRequest)
        }

        private fun getNumberOfWorkersText(workerRequest: WorkerRequest?) :String? {
            workerRequest?.let {
                val selections = workerRequest.listOfWorkerOptions
                val builder = StringBuilder()
                if (selections != null) {
                    for((id,count) in selections){
                        idToNameMap?.let {
                            val name = it[id]
                            builder.append(name + ": " + count + "\n")
                        }
                    }
                }
                return builder.toString()
            }
            return null
        }

        private fun getDateString(date : Date?) : String?{
            date?.let {
                val format = SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault())
                return format.format(date)
            }
            return null
        }

        private fun getStatusText(workerRequest: WorkerRequest?) : String{
            var status = "Unknown"
            workerRequest?.let{
                status = when(it.requestState){
                    DatabaseConstants.WORKER_REQUESTS_STATE_APPLIED ->  "Processing ..."
                    DatabaseConstants.WORKER_REQUESTS_STATE_PENDING ->  "Processing ..."
                    DatabaseConstants.WORKER_REQUESTS_STATE_APPROVED -> "Approved"
                    DatabaseConstants.WORKER_REQUESTS_STATE_DELETED -> "Deleted"
                    DatabaseConstants.WORKER_REQUESTS_STATE_DENIED -> "Denied"
                    else -> "Unknown"
                }
            }
            return status
        }
    }
}