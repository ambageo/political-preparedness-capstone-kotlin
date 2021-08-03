package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val args = navArgs<VoterInfoFragmentArgs>()

        //DONE: Add ViewModel values and create ViewModel
        val viewModelFactory = VoterInfoViewModelFactory(args, requireActivity().application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        //DONE: Add binding values
        val binding: FragmentVoterInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //DONE: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        viewModel.hasVoterInfo.observe(viewLifecycleOwner, Observer { voterInfo ->
            if (voterInfo == false) {
                hideVoterInfo(binding)
                Snackbar.make(requireView(), R.string.voter_info_error, Snackbar.LENGTH_LONG).show()
            } else {
                showVoterInfo(binding)
            }
        })

        //DONE: Handle loading of URLs
        viewModel.votingLocationUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let{
                launchUrl(url)
                viewModel.votingLocationCompleted()
            }
        })

        viewModel.ballotInformationUrl.observe(viewLifecycleOwner, { url ->
            url?.let{
                launchUrl(url)
                viewModel.ballotInformationCompleted()
            }
        })

        //DONE: Handle save button UI state
        //DONE: cont'd Handle save button clicks
        viewModel.isElectionFollowed.observe(viewLifecycleOwner, { isElectionFollowed ->
           when (isElectionFollowed){
               true -> binding.followElectionButton.text = getString(R.string.unfollow_election)
               else -> binding.followElectionButton.text = getString(R.string.follow_election)
           }

        })

        binding.followElectionButton.setOnClickListener {
            viewModel.toggleFollowElection()
        }

        return binding.root
    }

    private fun showVoterInfo(binding: FragmentVoterInfoBinding) {
        binding.stateHeader.visibility = View.VISIBLE
        binding.followElectionButton.visibility = View.VISIBLE
        binding.stateLocations.visibility = View.VISIBLE
        binding.stateBallot.visibility = View.VISIBLE
    }

    private fun hideVoterInfo(binding: FragmentVoterInfoBinding) {
        binding.stateHeader.visibility = View.GONE
        binding.followElectionButton.visibility = View.GONE
        binding.stateLocations.visibility = View.GONE
        binding.stateBallot.visibility = View.GONE
    }

    //DONE: Create method to load URL intents
    private fun launchUrl(url: String) {
        val urlIntent = Intent(ACTION_VIEW, Uri.parse(url))
        startActivity(urlIntent)
    }



}