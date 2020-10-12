package com.openjobs.openjobs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.create_worker_request_fragment.*
import kotlinx.android.synthetic.main.show_user_profile_fragment.*
import java.util.*

class CreateWorkerRequestFragment : Fragment() , View.OnClickListener {

    private lateinit var viewModel: CreateWorkerRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_worker_request_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateWorkerRequestViewModel::class.java)
        viewModel.getUserProfile()
        submitButton.setOnClickListener(this)
        viewModel.address.observe(viewLifecycleOwner, Observer { editTextTextPostalAddress.editText?.setText(it) })
        viewModel.submitRequestResult.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                val action = CreateWorkerRequestFragmentDirections.actionCreateWorkerRequestFragmentToConfirmationFragment()
                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(),"Error submitting request, please try again",Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun getCurrentSelectedDate() : Date{
        return when(dateRadioGroup.checkedRadioButtonId){
            todayRadio.id -> Date()
            tommorowRadio.id -> getTommorowsDate()
            else -> Date()
        }
    }

    private fun getTommorowsDate(): Date
    {
        val calendar = GregorianCalendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR,1)
        calendar.set(Calendar.HOUR_OF_DAY,7)
        return calendar.time
    }

    override fun onClick(v: View?) {
        if(v?.id == submitButton.id ){
            var isDataValid = true

            val numSkilledWorkers : Int  = numSkilledWorkersPicker.value

            val numUnskilledWorkers : Int  = numUnskilledWorkersPicker.value

            if((numSkilledWorkers < 0 || numUnskilledWorkers < 0) || (numSkilledWorkers == 0 && numUnskilledWorkers == 0)){
                isDataValid = false
            }

            val date = getCurrentSelectedDate()

            if(isDataValid){
                viewModel.submitRequest(workerRequest = WorkerRequest(numSkilledWorkers = numSkilledWorkers,
                    numUnskilledWorkers = numUnskilledWorkers,
                    date = date))
            }else{
                Toast.makeText(requireContext(), R.string.request_workers_form_invalid_data, Toast.LENGTH_LONG).show()
            }


        }
    }

}