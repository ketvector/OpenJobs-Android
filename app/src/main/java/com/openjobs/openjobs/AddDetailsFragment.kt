package com.openjobs.openjobs

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
    }


    override fun onClick(v: View?) {
        if(v?.id == submitButton.id){
            val address = editTextTextPostalAddress.editText?.text?.toString()
            viewModel.userGivenAddress = address
            val shortDescription = extraInformation.editText?.text?.toString()
            viewModel.userGivenShortDescription = shortDescription
            val additionalMessage = additionalMessage.editText?.text?.toString()
            viewModel.userGivenAdditionalMessage = additionalMessage
            AlertDialog
                .Builder(requireContext())
                .setTitle("Confirm Your Request")
                .setMessage(viewModel.getConfirmationMessage())
                .setPositiveButton("Confirm", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        viewModel.submitRequest() // TODO : Listen for result and take appropriate action
                        val action = AddDetailsFragmentDirections.actionAddDetailsFragmentToConfirmationFragment()
                        findNavController().navigate(action)
                    }
                })
                .setNegativeButton("Deny", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Log.e("AddDetailsFragment", "Denied by user")
                    }
                }).show()

        }
    }

}