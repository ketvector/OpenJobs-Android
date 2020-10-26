package com.openjobs.openjobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_worker_list_item.view.*

class SelectWorkerListAdapter : RecyclerView.Adapter<SelectWorkerListAdapter.ViewHolder>(), WorkerOptionCountChangedListener {
    var workerOptions : List<WorkerOption>? = null
    var selectionCounts : MutableMap<String,Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.select_worker_list_item, parent, false)
        )
    }

    fun setList(workerOptions : List<WorkerOption>){
        this.workerOptions = workerOptions
        this.selectionCounts = mutableMapOf()
        for(option in workerOptions){
            selectionCounts[option.id] = 0
        }
    }

    override fun getItemCount(): Int {
        return workerOptions?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workerOptions?.get(position), this)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), NumberPickerCountChangeListener{

        var currentWorkerOption : WorkerOption? = null
        var currentListener : WorkerOptionCountChangedListener? = null

        override fun onCountChanged(newCount: Int) {
            currentWorkerOption?.let {
                currentListener?.onNewCount(id = it.id,count = newCount)
            }

        }

        fun bind(workerOption: WorkerOption?, listener : WorkerOptionCountChangedListener){
            currentWorkerOption = workerOption
            currentListener = listener
            itemView.workerType.text = workerOption?.label
            itemView.priceView.text = getPriceText(workerOption)
            itemView.numWorkerPicker.setListener(this)
        }

        private fun getPriceText(workerOption: WorkerOption?) : String?{
            workerOption?.let {
                if(it.price > 0){
                    return it.price.toString() + "/" + it.unit
                }
                else{
                    return it.unit
                }

            }
            return null
        }
    }

    override fun onNewCount(id: String, count: Int) {
        selectionCounts.let {
            it[id] = count
        }
    }


}

interface WorkerOptionCountChangedListener{
    fun onNewCount(id: String, count : Int)
}