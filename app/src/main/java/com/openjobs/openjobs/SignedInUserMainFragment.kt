package com.openjobs.openjobs

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.signed_in_user_main_fragment.*

class SignedInUserMainFragment : Fragment(), View.OnClickListener, WorkerRequestCardClickListener {

    private lateinit var viewModel: SignedInUserMainViewModel
    private lateinit var workerRequestListAdapter: WorkerRequestsListAdapter
    private var isFirstTime = true
    private val TAG = "SignedInUserMainFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signed_in_user_main_fragment, container, false)
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener(this)
        viewModel = ViewModelProviders.of(this).get(SignedInUserMainViewModel::class.java)
        workerRequestListAdapter = WorkerRequestsListAdapter(this)
        workerRequestsList.adapter = workerRequestListAdapter
        viewModel.listOfWorkerRequests.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
            workerRequestListAdapter.setList(it)
            if(it.isEmpty()){
                emptyView.visibility = View.VISIBLE
            }else{
                emptyView.visibility = View.GONE
            }
            if(!(activity?.application as MyApplication).hasSeenSearchSreen && emptyView.visibility == View.VISIBLE){
                val action = SignedInUserMainFragmentDirections.actionSignedInUserMainFragmentToCreateWorkerRequestFragment()
                findNavController().navigate(action)
            }
            (activity?.application as MyApplication).hasSeenSearchSreen = true

        })
        viewModel.getWorkerRequestsForThisUser()
    }


    override fun onClick(v: View?) {
        if(v?.id == fab.id){
            val action = SignedInUserMainFragmentDirections.actionSignedInUserMainFragmentToCreateWorkerRequestFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDeleteButtonClicked(docId: String?) {
        viewModel.deleteDocument(docId)
    }

}