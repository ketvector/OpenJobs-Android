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
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.edit_user_profile_fragment.*

class EditUserProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: EditUserProfileViewModel
    val args : EditUserProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditUserProfileViewModel::class.java)
        editTextName.editText?.setText(args.name)
        editTextDefaultAddress.editText?.setText(args.address)
        submitButton.setOnClickListener(this)
        viewModel.submitRequestStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigateUp()
            }
            else{
                Toast.makeText(requireContext(),"Error updating profile",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(v: View?) {
        if(v?.id == submitButton.id){
            val name = editTextName.editText?.text.toString()
            val address = editTextDefaultAddress.editText?.text.toString()
            viewModel.submitProfile(name,address)
        }
    }

}