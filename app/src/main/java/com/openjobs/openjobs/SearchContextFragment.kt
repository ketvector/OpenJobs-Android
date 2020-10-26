package com.openjobs.openjobs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.search_context_fragment.*
import java.util.*

class SearchContextFragment : Fragment() , AdapterView.OnItemSelectedListener, View.OnClickListener {

    companion object {
        fun newInstance() = SearchContextFragment()
    }

    private lateinit var viewModel: CreateWorkerRequestViewModel
    private lateinit var locations : List<SimpleLocation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_context_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(CreateWorkerRequestViewModel::class.java)
        viewModel.locations.observe(viewLifecycleOwner, Observer { it ->
            locations = it
            val onlyNames = locations.map { location -> location.name  }
            spinner.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,onlyNames)
                .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            spinner.onItemSelectedListener = this@SearchContextFragment
            })
        viewModel.getAvailableLocations()
        submitButton.setOnClickListener(this)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.currentLocation = locations.get(position)
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
            viewModel.userGivenDate = date
            val action = SearchContextFragmentDirections.actionSearchContextFragmentToSelectWorkersFragment()
            findNavController().navigate(action)
        }
    }

}