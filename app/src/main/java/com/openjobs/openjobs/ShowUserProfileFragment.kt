package com.openjobs.openjobs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.show_user_profile_fragment.*

class ShowUserProfileFragment : Fragment() , View.OnClickListener{
    private lateinit var viewModel: ShowUserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShowUserProfileViewModel::class.java)
        viewModel.getUserProfile()
        viewModel.name.observe(viewLifecycleOwner, Observer { name.text = it })
        viewModel.address.observe(viewLifecycleOwner, Observer { address.text = it })
        viewModel.phone.observe(viewLifecycleOwner, Observer { phoneNumber.text = it })
        editButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id == editButton.id){
            val action = ShowUserProfileFragmentDirections.actionProfileFragmentToEditUserProfileFragment(name = name.text.toString()
                , address =  address.text.toString() )
            findNavController().navigate(action)
        }
    }
}