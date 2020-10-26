package com.openjobs.openjobs

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.Nullable

class HorizontalNumberPicker(context: Context?, @Nullable attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val et_number: EditText?
    var min = 0
    var max = 20

    private var listener : NumberPickerCountChangeListener? = null

    fun setListener(listener: NumberPickerCountChangeListener){
        this.listener = listener
    }

    /***
     * HANDLERS
     */
    private inner class AddHandler(val diff: Int) : OnClickListener {
        override fun onClick(v: View?) {
            var newValue = value + diff
            if (newValue < min) {
                newValue = min
            } else if (newValue > max) {
                newValue = max
            }
            et_number!!.setText(newValue.toString())
            listener?.onCountChanged(newValue)
        }

    }

    /***
     * GETTERS & SETTERS
     */
    var value: Int
        get() {
            if (et_number != null) {
                try {
                    val valueCurrent = et_number.text.toString().toInt()
                    return valueCurrent
                } catch (ex: NumberFormatException) {
                    Log.e("HorizontalNumberPicker", ex.toString())
                }
            }
            return 0
        }
        set(value) {
            et_number?.setText(value.toString())
        }

    init {
        View.inflate(context, R.layout.numberpicker_horizontal, this)
        et_number = findViewById(R.id.et_number)
        val btn_less: Button = findViewById(R.id.btn_less)
        btn_less.setOnClickListener(AddHandler(-1))
        val btn_more: Button = findViewById(R.id.btn_more)
        btn_more.setOnClickListener(AddHandler(1))
    }
}