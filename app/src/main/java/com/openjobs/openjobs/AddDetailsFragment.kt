package com.openjobs.openjobs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.add_details_fragment.*
import java.util.*

class AddDetailsFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = AddDetailsFragment()
    }

    private lateinit var viewModel: CreateWorkerRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(CreateWorkerRequestViewModel::class.java)
        viewModel.getUserProfile()
        viewModel.address.observe(viewLifecycleOwner, Observer { editTextTextPostalAddress.editText?.setText(it) })
        submitButton.setOnClickListener(this)
        // TODO: Use the ViewModel
    }

    private fun getCurrentSelectedDate() : Date {
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
        if(v?.id == submitButton.id){
            val date = getCurrentSelectedDate()
            val address = editTextTextPostalAddress.editText?.text?.toString()
            viewModel.userGivenAddress = address
            viewModel.userGivenDate = date
            viewModel.submitRequest() // TODO : Listen for result and take appropriate action
            val action = AddDetailsFragmentDirections.actionAddDetailsFragmentToConfirmationFragment()
            findNavController().navigate(action)
        }
    }

}