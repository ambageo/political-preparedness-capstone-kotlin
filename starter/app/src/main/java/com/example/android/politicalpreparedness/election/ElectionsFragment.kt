package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //DONE: Add binding values
        val binding: FragmentElectionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.lifecycleOwner = this

        //DONE: Add ViewModel values and create ViewModel
        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        //TODO: Initiate recycler adapters
        val upcomingElectionsAdapter = ElectionListAdapter()
        binding.upcomingElectionsList.adapter = upcomingElectionsAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { electionsList ->
            electionsList?.let{
            Log.d("ggg", "upcoming elections: ${electionsList.size}" )
            Log.d("ggg", electionsList[0].name)
                upcomingElectionsAdapter.submitList(electionsList)
        }
        })



        //TODO: Link elections to voter info



        //TODO: Populate recycler adapters


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}